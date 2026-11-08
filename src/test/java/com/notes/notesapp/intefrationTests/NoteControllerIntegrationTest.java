package com.notes.notesapp.intefrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notes.notesapp.enums.NoteTags;
import com.notes.notesapp.model.Note;
import com.notes.notesapp.model.dto.NoteRequest;
import com.notes.notesapp.repository.NoteRepository;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.transitions.Mongod;
import de.flapdoodle.embed.mongo.transitions.RunningMongodProcess;
import de.flapdoodle.reverse.TransitionWalker;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Integration tests for NoteController - spins up embedded MongoDB and full Spring context */
@SpringBootTest
class NoteControllerIntegrationTest {

    //Embedded MongoDB instance shared across all tests
    static final TransitionWalker.ReachedState<RunningMongodProcess> MONGOD =
            Mongod.instance().start(Version.Main.V7_0);

    //Point the app at the embedded MongoDB instead of a real instance
    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        var address = MONGOD.current().getServerAddress();
        registry.add("mongodb.uri",
                () -> "mongodb://" + address.getHost() + ":" + address.getPort() + "/testDB");
    }

    @AfterAll
    static void stopMongo() {
        MONGOD.close();
    }

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private NoteRepository noteRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        noteRepository.deleteAll();
    }


    // Helper to save a note directly to the DB
    private Note persistNote(String id, String title, String text, NoteTags tags) {
        return noteRepository.save(
                Note.builder()
                        .id(id)
                        .title(title)
                        .text(text)
                        .tags(tags)
                        .createdDate(LocalDateTime.now())
                        .build()
        );
    }

    // POST /api/notes

    // check whether note created successfully
    @Test
    void createNote_validRequest_returns200AndPersistsNote() throws Exception {
        NoteRequest request = new NoteRequest("My Title", "My note text", NoteTags.PERSONAL);

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Note created successfully"));

        List<Note> notes = noteRepository.findAll();
        assertThat(notes).hasSize(1);
        assertThat(notes.get(0).getTitle()).isEqualTo("My Title");
        assertThat(notes.get(0).getText()).isEqualTo("My note text");
        assertThat(notes.get(0).getTags()).isEqualTo(NoteTags.PERSONAL);
        assertThat(notes.get(0).getCreatedDate()).isNotNull();
    }

    //create note with both fields blank

    @Test
    void createNote_bothFieldsBlank_returns400WithBothErrors() throws Exception {
        NoteRequest request = new NoteRequest("", "", NoteTags.PERSONAL);

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.title").exists())
                .andExpect(jsonPath("$.errors.noteText").exists());
    }


    // GET /api/notes

    // test whether notes returned correctly
    @Test
    void getNotes_multipleNotes_returns200AndAllResults() throws Exception {
        persistNote(null, "Note A", "Text A", NoteTags.PERSONAL);
        persistNote(null, "Note B", "Text B", NoteTags.BUSINESS);
        persistNote(null, "Note C", "Text C", NoteTags.IMPORTANT);

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    // test whether notes returned correctly when filtering by tag
    @Test
    void getNotes_filterByMultipleTags_returnsAllMatchingNotes() throws Exception {
        persistNote(null, "Personal Note",  "Personal text",  NoteTags.PERSONAL);
        persistNote(null, "Business Note",  "Business text",  NoteTags.BUSINESS);
        persistNote(null, "Important Note", "Important text", NoteTags.IMPORTANT);

        mockMvc.perform(get("/api/notes")
                        .param("tags", "PERSONAL")
                        .param("tags", "BUSINESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    // test whether pagination works correctly
    @Test
    void getNotes_defaultPagination_limitsTo10Results() throws Exception {
        for (int i = 1; i <= 15; i++) {
            persistNote(null, "Note " + i, "Text " + i, NoteTags.PERSONAL);
        }

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(15))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.content.length()").value(10));
    }

    // GET /api/notes/{noteId}

    // test whether searching for a note by ID is successful
    @Test
    void getNoteById_existingNote_returns200WithNoteBody() throws Exception {
        persistNote("1", "Found Note", "Note content", NoteTags.BUSINESS);

        mockMvc.perform(get("/api/notes/{noteId}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Found Note"))
                .andExpect(jsonPath("$.text").value("Note content"))
                .andExpect(jsonPath("$.tags").value("BUSINESS"));
    }

    // test search for non-existing note
    @Test
    void getNoteById_nonExistingNote_throws() {
        assertThrows(Exception.class, () ->
                mockMvc.perform(get("/api/notes/{noteId}", "999")));
    }

    //PUT /api/notes/{noteId}

    // test whether update node with all fields is successful

    @Test
    void updateNote_existingNote_returns200AndUpdatesAllFields() throws Exception {
        persistNote("1", "Original Title", "Original text", NoteTags.PERSONAL);

        NoteRequest update = new NoteRequest("Updated Title", "Updated text", NoteTags.IMPORTANT);

        mockMvc.perform(put("/api/notes/{noteId}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(content().string("Note updated successfully"));


        mockMvc.perform(get("/api/notes/{noteId}", "1"))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.text").value("Updated text"))
                .andExpect(jsonPath("$.tags").value("IMPORTANT"));
    }

    // test whether update node with provided fields is successful
    @Test
    void updateNote_existingNote_onlyUpdatesProvidedFields() throws Exception {
        persistNote("1", "Keep This Title", "Keep this text", NoteTags.PERSONAL);


        NoteRequest partialUpdate = new NoteRequest("New Title", "   ", null);

        mockMvc.perform(put("/api/notes/{noteId}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialUpdate)))
                .andExpect(status().isOk());


        mockMvc.perform(get("/api/notes/{noteId}", "1"))
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.text").value("Keep this text"))
                .andExpect(jsonPath("$.tags").value("PERSONAL"));
    }



    // DELETE /api/notes/{noteId}
    // test whether delete node is successful
    @Test
    void deleteNote_existingNote_returns200AndRemovesIt() throws Exception {
        persistNote("1", "Note To Delete", "Delete me", NoteTags.PERSONAL);
        assertThat(noteRepository.count()).isEqualTo(1);

        mockMvc.perform(delete("/api/notes/{noteId}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Note deleted successfully"));

        assertThat(noteRepository.count()).isZero();
    }


    // GET /api/notes/stats/{noteId}

    //Verifies word frequency map is returned correctly
    @Test
    void getStats_existingNote_returns200WithWordCounts() throws Exception {
        persistNote("1", "Stats Note", "This is a test and this test is important", NoteTags.PERSONAL);

        mockMvc.perform(get("/api/notes/stats/{noteId}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.this").value(2))
                .andExpect(jsonPath("$.is").value(2))
                .andExpect(jsonPath("$.test").value(2))
                .andExpect(jsonPath("$.a").value(1))
                .andExpect(jsonPath("$.and").value(1))
                .andExpect(jsonPath("$.important").value(1));
    }


}

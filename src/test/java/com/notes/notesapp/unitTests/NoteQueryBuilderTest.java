package com.notes.notesapp.unitTests;

import com.notes.notesapp.enums.NoteTags;
import com.notes.notesapp.model.dto.NoteSearchParameters;
import com.notes.notesapp.repository.NoteQueryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static com.mongodb.assertions.Assertions.*;

public class NoteQueryBuilderTest {
    private NoteQueryBuilder noteQueryBuilder;
    @BeforeEach
    void setUp(){
        noteQueryBuilder = new NoteQueryBuilder();
    }
    @Test
    void shouldAddFilterByParams(){
        NoteSearchParameters noteSearchParameters = new NoteSearchParameters(
                List.of(NoteTags.BUSINESS, NoteTags.IMPORTANT)
        );
        Query query = noteQueryBuilder.buildQuery(noteSearchParameters);

        assertNotNull(query);
        assertTrue(query.getQueryObject().containsKey("tags"));

    }
}

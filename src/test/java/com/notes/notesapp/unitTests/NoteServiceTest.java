package com.notes.notesapp.unitTests;

import com.notes.notesapp.enums.NoteTags;
import com.notes.notesapp.model.Note;
import com.notes.notesapp.model.dto.NoteRequest;
import com.notes.notesapp.model.dto.NoteSearchParameters;
import com.notes.notesapp.model.dto.NoteResponse;
import com.notes.notesapp.repository.NoteRepository;
import com.notes.notesapp.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Unit tests for NoteService uses Mockito to isolate service logic from MongoDB
public class NoteServiceTest {

    @Mock
    NoteRepository noteRepository;

    @InjectMocks
    NoteService noteService;

    Note testNote1;
    Note testNote2;
    Note testNote3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testNote1 = new Note();
        testNote1.setId("1");
        testNote1.setTitle("TestTitle1");
        testNote1.setCreatedDate(LocalDateTime.of(2024, 1, 1, 10, 0));
        testNote1.setText("This is a test note, and it has no sense");
        testNote1.setTags(null);

        testNote2 = new Note();
        testNote2.setId("2");
        testNote2.setTitle("TestTitle2");
        testNote2.setCreatedDate(LocalDateTime.of(2024, 6, 15, 12, 0));
        testNote2.setText("This is a test note, and this test has no sense, but this test is important");
        testNote2.setTags(NoteTags.BUSINESS);

        testNote3 = new Note();
        testNote3.setId("3");
        testNote3.setTitle("TestTitle3");
        testNote3.setCreatedDate(LocalDateTime.of(2023, 3, 20, 9, 0));
        testNote3.setText("Hey, this is my test node , node ");
        testNote3.setTags(NoteTags.BUSINESS);
    }

    // Verifies descending sort: most frequent words come first
    @Test
    void getStats_wordsWithHigherCountAppearFirst() {
        Map<String, Integer> result = noteService.getStats(testNote2.getText());

        assertEquals(3, result.get("this"));
        assertEquals(3, result.get("test"));
        assertEquals(2, result.get("is"));
        assertEquals(1, result.get("important"));


        List<Integer> values = new ArrayList<>(result.values());
        for (int i = 0; i < values.size() - 1; i++) {
            assertTrue(values.get(i) >= values.get(i + 1),
                    "Values should be in descending order");
        }
    }
}

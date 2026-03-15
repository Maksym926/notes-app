package com.notes.notesapp.unitTests;


import com.notes.notesapp.enums.NoteTags;
import com.notes.notesapp.model.Note;
import com.notes.notesapp.repository.NoteRepository;
import com.notes.notesapp.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class NoteServiceTest {

    @Mock NoteRepository noteRepository;

    @InjectMocks NoteService noteService;


    Note testNote1;
    Note testNote2;
    Note testNote3;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        testNote1 = new Note();
        testNote1.setTitle("TestTitile1");
        testNote1.setDate(LocalDateTime.of(2024, 1, 1, 10, 0));
        testNote1.setText("This is a test note, and it has no sense");
        testNote1.setTags(null);

        testNote2 = new Note();
        testNote2.setTitle("TestTitile2");
        testNote2.setDate(LocalDateTime.of(2024, 6, 15, 12, 0));
        testNote2.setText("This is a test note, and this test has no sense, but this test is important");
        testNote2.setTags(NoteTags.BUSINESS);

        testNote3 = new Note();
        testNote3.setTitle("TestTitile3");
        testNote3.setDate(LocalDateTime.of(2023, 3, 20, 9, 0));
        testNote3.setText("Hey, this is my test node , node ");
        testNote3.setTags(NoteTags.BUSINESS);




    }
    @Test
    public void  getStatsTest(){

        TreeMap<String, Integer> wordMap1Test = new TreeMap<>();

        wordMap1Test.put("this", 1);
        wordMap1Test.put("a", 1);
        wordMap1Test.put("and", 1);
        wordMap1Test.put("has", 1);
        wordMap1Test.put("is", 1);
        wordMap1Test.put("it", 1);
        wordMap1Test.put("no", 1);
        wordMap1Test.put("note", 1);
        wordMap1Test.put("sense", 1);
        wordMap1Test.put("test", 1);

        TreeMap<String, Integer> wordMap2Test = new TreeMap<>(Comparator.reverseOrder());

        wordMap2Test.put("a", 1);
        wordMap2Test.put("and", 1);
        wordMap2Test.put("has", 1);
        wordMap2Test.put("important", 1);
        wordMap2Test.put("is", 2);
        wordMap2Test.put("note", 1);
        wordMap2Test.put("no", 1);
        wordMap2Test.put("but", 1);
        wordMap2Test.put("test", 3);
        wordMap2Test.put("this", 3);
        wordMap2Test.put("sense", 1);

        assertEquals(wordMap1Test, noteService.getStats(testNote1.getText()));
        assertEquals(wordMap2Test, noteService.getStats(testNote2.getText()));
    }
    @Test
    public void sortNotesTest(){
        List<Note> unsorted = new ArrayList<>(List.of(testNote1, testNote2, testNote3));

        List<Note> sorted = noteService.sortNotes(unsorted);

        assertEquals(testNote2, sorted.get(0));
        assertEquals(testNote1, sorted.get(1));
        assertEquals(testNote3, sorted.get(2));
    }

}

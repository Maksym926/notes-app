package com.notes.notesapp.service;

import com.notes.notesapp.enums.NoteTags;
import com.notes.notesapp.model.Note;
import com.notes.notesapp.model.NoteSearchParameters;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.TreeMap;

public class NoteService {
    public TreeMap<String, Integer> getStats(String noteText){
        return null;
    }

    public List<Note> sortNotes(List<Note> unsortedNotes){
         return null;
    }
}

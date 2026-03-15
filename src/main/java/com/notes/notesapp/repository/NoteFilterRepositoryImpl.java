package com.notes.notesapp.repository;

import com.notes.notesapp.model.Note;
import com.notes.notesapp.model.NoteSearchParameters;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class NoteFilterRepositoryImpl implements NoteFilterRepository{



    @Override
    public List<Note> search(NoteSearchParameters params) {
        return List.of();
    }
}

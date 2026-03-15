package com.notes.notesapp.repository;

import com.notes.notesapp.model.Note;
import com.notes.notesapp.model.NoteSearchParameters;

import java.util.List;

public interface NoteFilterRepository {
    List<Note> search(NoteSearchParameters params);
}

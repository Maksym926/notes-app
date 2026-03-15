package com.notes.notesapp.repository;

import com.notes.notesapp.model.Note;
import com.notes.notesapp.model.dto.NoteSearchParameters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoteFilterRepository {
    Page<Note> search(NoteSearchParameters params, Pageable pageable);
}

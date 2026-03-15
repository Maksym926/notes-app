package com.notes.notesapp.repository;

import com.notes.notesapp.model.Note;
import com.notes.notesapp.model.NoteSearchParameters;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NoteRepository extends MongoRepository<Note, Long>, NoteFilterRepository {

}

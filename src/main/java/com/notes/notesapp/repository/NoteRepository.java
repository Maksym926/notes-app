package com.notes.notesapp.repository;

import com.notes.notesapp.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<Note, Long>, NoteFilterRepository {

}

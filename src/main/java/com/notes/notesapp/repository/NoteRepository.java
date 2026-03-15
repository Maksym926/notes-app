package com.notes.notesapp.repository;

import com.notes.notesapp.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends MongoRepository<Note, String>, NoteFilterRepository {

}

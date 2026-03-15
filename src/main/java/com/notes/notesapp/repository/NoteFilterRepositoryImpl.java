package com.notes.notesapp.repository;

import com.notes.notesapp.model.Note;
import com.notes.notesapp.model.dto.NoteSearchParameters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class NoteFilterRepositoryImpl implements NoteFilterRepository{

    private final MongoTemplate mongoTemplate;
    private final NoteQueryBuilder queryBuilder;

    public NoteFilterRepositoryImpl(MongoTemplate mongoTemplate, NoteQueryBuilder queryBuilder) {
        this.mongoTemplate = mongoTemplate;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public Page<Note> search(NoteSearchParameters params, Pageable pageable) {
        Query query = queryBuilder.buildQuery(params);

        long total = mongoTemplate.count(query, Note.class);

        query.with(pageable);
        List<Note> notes = mongoTemplate.find(query, Note.class);

        return new PageImpl<>(notes, pageable, total);
    }
}

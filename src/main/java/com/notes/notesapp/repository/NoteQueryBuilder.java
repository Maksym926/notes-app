package com.notes.notesapp.repository;

import com.notes.notesapp.model.dto.NoteSearchParameters;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class NoteQueryBuilder {
    public Query buildQuery(NoteSearchParameters params){
        Criteria criteria = new Criteria();

        if(params.tags() != null && !params.tags().isEmpty()){
            criteria.and("tags").in(params.tags());

        }
        return Query.query(criteria);
    }
}

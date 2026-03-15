package com.notes.notesapp.unitTests;

import com.notes.notesapp.enums.NoteTags;
import com.notes.notesapp.model.dto.NoteSearchParameters;
import com.notes.notesapp.repository.NoteQueryBuilder;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for NoteQueryBuilder verifies MongoDB query construction from search parameters
public class NoteQueryBuilderTest {
    private NoteQueryBuilder noteQueryBuilder;

    @BeforeEach
    void setUp() {
        noteQueryBuilder = new NoteQueryBuilder();
    }

    // Verifies that multiple tags produce a correct $in query
    @Test
    void shouldAddFilterByParams() {
        NoteSearchParameters params = new NoteSearchParameters(
                List.of(NoteTags.BUSINESS, NoteTags.IMPORTANT)
        );
        Query query = noteQueryBuilder.buildQuery(params);

        assertNotNull(query);
        Document queryObject = query.getQueryObject();
        assertTrue(queryObject.containsKey("tags"));

        // Verify the $in operator contains both tags
        Document tagsFilter = queryObject.get("tags", Document.class);
        assertNotNull(tagsFilter);
        List<?> inValues = tagsFilter.getList("$in", Object.class);
        assertNotNull(inValues);
        assertEquals(2, inValues.size());
    }

    //Null tags should not add any filter criteria
    @Test
    void shouldReturnEmptyQueryWhenTagsAreNull() {
        NoteSearchParameters params = new NoteSearchParameters(null);
        Query query = noteQueryBuilder.buildQuery(params);

        assertNotNull(query);
        assertFalse(query.getQueryObject().containsKey("tags"));
    }




}

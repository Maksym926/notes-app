package com.notes.notesapp.model;


import com.notes.notesapp.enums.NoteTags;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notes")
public class Note {
    @Id
    private String id;
    private String title;
    private LocalDateTime date;
    private String text;
    private NoteTags tags;
}

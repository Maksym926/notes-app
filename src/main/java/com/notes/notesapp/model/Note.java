package com.notes.notesapp.model;


import com.notes.notesapp.enums.NoteTags;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    @Id
    private Long id;
    private String title;
    private LocalDateTime date;
    private String text;
    private NoteTags tags;
}

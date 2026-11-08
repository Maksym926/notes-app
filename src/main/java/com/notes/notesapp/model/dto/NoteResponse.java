package com.notes.notesapp.model.dto;

import java.time.LocalDateTime;

// response view for listing notes
public record NoteResponse(
        String title,
        LocalDateTime createdDate
) {
}

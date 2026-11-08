package com.notes.notesapp.model.dto;

import com.notes.notesapp.enums.NoteTags;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

// request payload for create/update note
public record NoteRequest(
        @NotBlank(message = "Title cannot be blank")
        String title,
        @NotBlank(message = "Note text cannot be blank")
        String noteText,
        NoteTags tags
) {
}

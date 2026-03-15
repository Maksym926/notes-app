package com.notes.notesapp.model.dto;

import com.notes.notesapp.enums.NoteTags;

import java.util.List;

public record NoteSearchParameters(
        List<NoteTags> tags
) {

}

package com.notes.notesapp.controller;

import com.notes.notesapp.model.Note;
import com.notes.notesapp.model.dto.NoteRequest;
import com.notes.notesapp.model.dto.NoteResponse;
import com.notes.notesapp.model.dto.NoteSearchParameters;
import com.notes.notesapp.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// + rest controller for notes api
@RestController
@RequestMapping("/api")
public class NoteController {

    // + inject note service layer
    @Autowired
    private NoteService noteService;

    @PostMapping("/notes")
    public ResponseEntity<String> createNote(@Valid @RequestBody NoteRequest noteRequest){
        //create new note
        noteService.createNote(noteRequest);
        return ResponseEntity.ok("Note created successfully");
    }
    @PutMapping("/notes/{noteId}")
    public ResponseEntity<String> updateNote(@PathVariable("noteId") String id, @RequestBody NoteRequest noteRequest){
        // update note by id
        noteService.updateNote(id, noteRequest);
        return ResponseEntity.ok("Note updated successfully");
    }
    @DeleteMapping("/notes/{noteId}")
    public ResponseEntity<String> deleteNote(@PathVariable("noteId") String id){
        // remove note by id
        noteService.deleteNote(id);
        return ResponseEntity.ok("Note deleted successfully");
    }
    @GetMapping("/notes/{noteId}")
    public ResponseEntity<Note> getNoteById(@PathVariable("noteId") String id){
        // fetch note details
        return ResponseEntity.ok(noteService.getNoteById(id));
    }
    @GetMapping("/notes")
    public ResponseEntity<Page<NoteResponse>> getNotes(@PageableDefault(
            size = 10,
            sort = "createdDate",
            direction = Sort.Direction.DESC
    ) Pageable pageable, NoteSearchParameters params){
        // get notes with paging and filters
        return ResponseEntity.ok(noteService.getNotes(pageable, params));
    }

    @GetMapping("/notes/stats/{noteId}")
    public ResponseEntity<Map<String, Integer>> getStats(@PathVariable("noteId") String id){
        // calc word stats for note text
        return ResponseEntity.ok(noteService.getStats(noteService.getNoteById(id).getText()));
    }

}

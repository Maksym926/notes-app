package com.notes.notesapp.service;

import com.notes.notesapp.model.Note;
import com.notes.notesapp.model.dto.NoteRequest;
import com.notes.notesapp.model.dto.NoteResponse;
import com.notes.notesapp.model.dto.NoteSearchParameters;
import com.notes.notesapp.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public Map<String, Integer> getStats(String noteText){
        Map<String, Integer> counts = new TreeMap<>();
        if(noteText != null && !noteText.isEmpty()){
            String[] words = noteText.toLowerCase().split("\\s");
            for(String word : words){
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                if(!word.isEmpty()){
                    counts.put(word, counts.getOrDefault(word, 0) + 1);
                }
            }
        }
        LinkedHashMap<String, Integer> sorted = new LinkedHashMap<>();
        counts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(e -> sorted.put(e.getKey(), e.getValue()));
        return sorted;
    }

    public void createNote(NoteRequest noteRequest) {
        Note note = Note.builder()
                .title(noteRequest.title())
                .createdDate(LocalDateTime.now())
                .text(noteRequest.noteText())
                .tags(noteRequest.tags())
                .build();
        noteRepository.save(note);
    }
    public void updateNote(String id, NoteRequest noteRequest){
        Note note = getNoteById(id);
        if (noteRequest.title() != null && !noteRequest.title().isBlank()) {
            note.setTitle(noteRequest.title());
        }

        if (noteRequest.noteText() != null && !noteRequest.noteText().isBlank()) {
            note.setText(noteRequest.noteText());
        }

        if (noteRequest.tags() != null) {
            note.setTags(noteRequest.tags());
        }

        noteRepository.save(note);
    }

    public Note getNoteById(String id) {
        return noteRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Note not found")
        );
    }

    public void deleteNote(String id) {
        Note note = getNoteById(id);
        noteRepository.delete(note);
    }

    public Page<NoteResponse> getNotes(Pageable pageable, NoteSearchParameters params) {
        return noteRepository.search(params, pageable)
                .map(note -> new NoteResponse(note.getTitle(), note.getCreatedDate()));
    }
}

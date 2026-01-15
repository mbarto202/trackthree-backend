package com.trackthree.tracker;

import com.trackthree.tracker.model.TrackerEntry;
import com.trackthree.tracker.repository.TrackerEntryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracker")
public class TrackerController {

    private final TrackerEntryRepository repository;

    public TrackerController(TrackerEntryRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/log")
    public ResponseEntity<String> logEntry(@RequestBody TrackerEntry entry) {
        if (entry.getClientCode() == null || entry.getClientCode().isBlank()) {
            return ResponseEntity.badRequest().body("clientCode is required");
        }
        if (entry.getDate() == null) {
            return ResponseEntity.badRequest().body("date is required");
        }

        // Upsert: one row per (clientCode, date)
        var existing = repository.findByClientCodeAndDate(entry.getClientCode(), entry.getDate());
        if (existing.isPresent()) {
            TrackerEntry toUpdate = existing.get();
            toUpdate.setCalories(entry.getCalories());
            toUpdate.setProtein(entry.getProtein());
            toUpdate.setWater(entry.getWater());
            repository.save(toUpdate);
            return ResponseEntity.ok("Entry updated!");
        } else {
            repository.save(entry);
            return ResponseEntity.ok("Entry saved!");
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<TrackerEntry>> getHistory(@RequestParam String clientCode) {
        if (clientCode == null || clientCode.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repository.findAllByClientCodeOrderByDateDesc(clientCode));
    }
}

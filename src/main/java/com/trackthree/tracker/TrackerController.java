package com.trackthree.tracker;

import com.trackthree.tracker.model.TrackerEntry;
import com.trackthree.tracker.repository.TrackerEntryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;


import java.util.List;

@RestController
@RequestMapping("/api/tracker")
public class TrackerController {

    private final TrackerEntryRepository repository;
    private static final String ADMIN_CODE = "TT-BUZZ99";

    private static final Set<String> ALLOWED_CLIENT_CODES = Set.of(
    "TT-BUZZ99", // Admin
    "TT-DEMO00"
);


    public TrackerController(TrackerEntryRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/log")
    public ResponseEntity<String> logEntry(@RequestBody TrackerEntry entry) {
        if (entry.getClientCode() == null || entry.getClientCode().isBlank()) {
            return ResponseEntity.badRequest().body("clientCode is required");
        }
        // Reject unknown client codes
    if (!ALLOWED_CLIENT_CODES.contains(entry.getClientCode())) {
        return ResponseEntity.status(403).body("Invalid client code");
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

        if (!ALLOWED_CLIENT_CODES.contains(clientCode)) {
            return ResponseEntity.status(403).build();
        }

        if (ADMIN_CODE.equals(clientCode)) {
            return ResponseEntity.ok(repository.findAllByOrderByDateDesc());
        }

        return ResponseEntity.ok(repository.findAllByClientCodeOrderByDateDesc(clientCode));
    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validateClientCode(@RequestParam String clientCode) {

        if (clientCode == null || clientCode.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        if (!ALLOWED_CLIENT_CODES.contains(clientCode)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/history")
    public ResponseEntity<String> resetHistory(@RequestParam String clientCode) {
        if (clientCode == null || clientCode.isBlank()) {
            return ResponseEntity.badRequest().body("clientCode is required");
        }

        if (!ADMIN_CODE.equals(clientCode)) {
            return ResponseEntity.status(403).body("Admin access required");
        }

        repository.deleteAll();
        return ResponseEntity.ok("All history deleted.");
    }

    @DeleteMapping("/entry/{id}")
    public ResponseEntity<String> deleteEntry(
            @PathVariable Long id,
            @RequestParam String clientCode) {

        if (clientCode == null || clientCode.isBlank()) {
            return ResponseEntity.badRequest().body("clientCode is required");
        }

        if (!ADMIN_CODE.equals(clientCode)) {
            return ResponseEntity.status(403).body("Admin access required");
        }

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.ok("Entry deleted");
    }
}

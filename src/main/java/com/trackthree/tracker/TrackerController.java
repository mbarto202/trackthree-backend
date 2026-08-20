package com.trackthree.tracker;

import com.trackthree.tracker.model.Client;
import com.trackthree.tracker.model.TrackerEntry;
import com.trackthree.tracker.repository.ClientRepository;
import com.trackthree.tracker.repository.TrackerEntryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tracker")
public class TrackerController {

    private final TrackerEntryRepository repository;
    private final ClientRepository clientRepository;

    public TrackerController(TrackerEntryRepository repository, ClientRepository clientRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/log")
    public ResponseEntity<String> logEntry(@RequestBody TrackerEntry entry) {
        if (isMissingClientCode(entry.getClientCode())) {
            return ResponseEntity.badRequest().body("clientCode is required");
        }
        // Reject unknown client codes
        if (!isValidClientCode(entry.getClientCode())) {
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
        if (isMissingClientCode(clientCode)) {
            return ResponseEntity.badRequest().build();
        }

        if (!isValidClientCode(clientCode)) {
            return ResponseEntity.status(403).build();
        }

        if (isAdmin(clientCode)) {
            return ResponseEntity.ok(repository.findAllByOrderByDateDesc());
        }

        return ResponseEntity.ok(repository.findAllByClientCodeOrderByDateDesc(clientCode));
    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validateClientCode(@RequestParam String clientCode) {

        if (isMissingClientCode(clientCode)) {
            return ResponseEntity.badRequest().build();
        }

        if (!isValidClientCode(clientCode)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/history")
    public ResponseEntity<String> resetHistory(@RequestParam String clientCode) {
        if (isMissingClientCode(clientCode)) {
            return ResponseEntity.badRequest().body("clientCode is required");
        }

        if (!isAdmin(clientCode)) {
            return ResponseEntity.status(403).body("Admin access required");
        }

        repository.deleteAll();
        return ResponseEntity.ok("All history deleted.");
    }

    @DeleteMapping("/entry/{id}")
    public ResponseEntity<String> deleteEntry(
            @PathVariable Long id,
            @RequestParam String clientCode) {

        if (isMissingClientCode(clientCode)) {
            return ResponseEntity.badRequest().body("clientCode is required");
        }

        if (!isAdmin(clientCode)) {
            return ResponseEntity.status(403).body("Admin access required");
        }

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.ok("Entry deleted");
    }

    private boolean isMissingClientCode(String clientCode) {
        return clientCode == null || clientCode.isBlank();
    }

    private boolean isValidClientCode(String clientCode) {
        return clientRepository.existsById(clientCode);
    }

    private boolean isAdmin(String clientCode) {
        Optional<Client> client = clientRepository.findById(clientCode);
        return client.map(Client::isAdmin).orElse(false);
    }
}

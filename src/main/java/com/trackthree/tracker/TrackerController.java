package com.trackthree.tracker;

import com.trackthree.tracker.model.TrackerEntry;
import com.trackthree.tracker.repository.TrackerEntryRepository;

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
    public String logEntry(@RequestBody TrackerEntry entry) {
        repository.save(entry);
        return "Entry saved to database!";
    }

    @GetMapping("/history")
    public List<TrackerEntry> getHistory() {
        return repository.findAll();
    }
}

package com.trackthree.tracker;

import com.trackthree.model.TrackerEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tracker")
public class TrackerController {

    // Temporary in-memory store (later replace with DB)
    private final List<TrackerEntry> entries = new ArrayList<>();
    
    @PostMapping("/log")
    public String logEntry(@RequestBody TrackerEntry entry) {
        entries.add(entry);
        return "Entry logged successfully!";
    }

    @GetMapping("/history")
    public List<TrackerEntry> getHistory() {
        return entries;
    }
}
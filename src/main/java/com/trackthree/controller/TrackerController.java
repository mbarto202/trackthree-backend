/*
  ================================================
  🔧 Manual API Testing Instructions (Temporary)
  ================================================

  ✅ 1. Log a new tracker entry
  -----------------------------
  - Endpoint:   POST /api/tracker/log
  - URL:        http://localhost:8080/api/tracker/log
  - Headers:    Content-Type: application/json
  - Body (JSON):
    {
      "date": "2025-10-09",
      "calories": 2100,
      "protein": 140,
      "water": 96
    }

  - Expected Response:
    "Entry logged successfully!"

  ✅ 2. Retrieve all logged entries
  --------------------------------
  - Endpoint:   GET /api/tracker/history
  - URL:        http://localhost:8080/api/tracker/history

  - Expected Response (example):
    [
      {
        "date": "2025-10-09",
        "calories": 2100,
        "protein": 140,
        "water": 96
      }
    ]
*/


package com.trackthree.controller;

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
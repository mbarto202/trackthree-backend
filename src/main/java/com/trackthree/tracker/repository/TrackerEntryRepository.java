package com.trackthree.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trackthree.tracker.model.TrackerEntry;

public interface TrackerEntryRepository extends JpaRepository<TrackerEntry, Long> {
}

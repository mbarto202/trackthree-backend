package com.trackthree.tracker.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.trackthree.tracker.model.TrackerEntry;

public interface TrackerEntryRepository extends JpaRepository<TrackerEntry, Long> {
    Optional<TrackerEntry> findByClientCodeAndDate(String clientCode, LocalDate date);
    List<TrackerEntry> findAllByClientCodeOrderByDateDesc(String clientCode);
}

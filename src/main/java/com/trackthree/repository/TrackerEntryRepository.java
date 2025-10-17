package com.trackthree.repository;

import com.trackthree.model.TrackerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackerEntryRepository extends JpaRepository<TrackerEntry, Long> {
}

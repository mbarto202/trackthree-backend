package com.trackthree.tracker.repository;

import com.trackthree.tracker.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, String> {
    List<Client> findAllByAdminFalseOrderByCodeAsc();
}

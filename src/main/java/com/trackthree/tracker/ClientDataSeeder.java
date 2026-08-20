package com.trackthree.tracker;

import com.trackthree.tracker.model.Client;
import com.trackthree.tracker.repository.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ClientDataSeeder implements CommandLineRunner {

    private final ClientRepository clientRepository;

    public ClientDataSeeder(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void run(String... args) {
        addClientIfMissing("TT-BUZZ99", true);
        addClientIfMissing("TT-DEMO00", false);
    }

    private void addClientIfMissing(String code, boolean admin) {
        if (!clientRepository.existsById(code)) {
            clientRepository.save(new Client(code, admin));
        }
    }
}
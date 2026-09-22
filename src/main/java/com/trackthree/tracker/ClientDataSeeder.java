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
        addClientIfMissing("TT-BUZZ99", "Bartobuild Admin", true);
        addClientIfMissing("TT-DEMO00", "Demo Client", false);
    }

    private void addClientIfMissing(String code, String name, boolean admin) {
    var existingClient = clientRepository.findById(code);

    if (existingClient.isEmpty()) {
        clientRepository.save(new Client(code, name, admin));
        return;
    }

    Client client = existingClient.get();

    if (client.getName() == null || client.getName().isBlank()) {
        client.setName(name);
        clientRepository.save(client);
    }
}
}
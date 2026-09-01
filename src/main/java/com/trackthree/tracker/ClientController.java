package com.trackthree.tracker;

import com.trackthree.tracker.model.Client;
import com.trackthree.tracker.repository.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostMapping
    public ResponseEntity<?> createClient(
            @RequestHeader("X-Admin-Code") String adminCode) {

        boolean isAdmin = clientRepository.findById(adminCode)
                .map(Client::isAdmin)
                .orElse(false);

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }

        String clientCode = generateUniqueClientCode();
        Client client = new Client(clientCode, false);
        clientRepository.save(client);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(client);
    }

    private String generateUniqueClientCode() {
        String code;

        do {
            String randomPart = UUID.randomUUID()
                    .toString()
                    .substring(0, 6)
                    .toUpperCase();

            code = "TT-" + randomPart;
        } while (clientRepository.existsById(code));

        return code;
    }
}

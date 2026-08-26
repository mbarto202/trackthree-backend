package com.trackthree.tracker;

import com.trackthree.tracker.model.Client;
import com.trackthree.tracker.repository.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostMapping
    public ResponseEntity<?> createClient(
            @RequestHeader("X-Admin-Code") String adminCode,
            @RequestBody CreateClientRequest request) {

        boolean isAdmin = clientRepository.findById(adminCode)
                .map(Client::isAdmin)
                .orElse(false);

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }

        if (request.code() == null || request.code().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("code is required");
        }

        if (clientRepository.existsById(request.code())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Client code already exists");
        }

        Client client = new Client(request.code(), false);
        clientRepository.save(client);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(client);
    }

    public record CreateClientRequest(String code) {
    }
}

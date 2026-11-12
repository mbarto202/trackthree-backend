package com.trackthree.tracker;

import com.trackthree.tracker.model.Client;
import com.trackthree.tracker.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TrackerControllerValidationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
        clientRepository.save(new Client("TT-CLIENT01", "Test Client", false));
    }

    @Test
    void acceptsClientCodeStoredInDatabase() throws Exception {
        mockMvc.perform(get("/api/tracker/validate")
                .param("clientCode", "TT-CLIENT01"))
                .andExpect(status().isOk());
    }

    @Test
    void rejectsClientCodeNotStoredInDatabase() throws Exception {
        mockMvc.perform(get("/api/tracker/validate")
                .param("clientCode", "TT-UNKNOWN"))
                .andExpect(status().isForbidden());
    }

    @Test
    void rejectsBlankClientCode() throws Exception {
        mockMvc.perform(get("/api/tracker/validate")
                .param("clientCode", " "))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsInactiveClientCode() throws Exception {
        Client client = clientRepository.findById("TT-CLIENT01")
                .orElseThrow();

        client.setActive(false);
        clientRepository.save(client);

        mockMvc.perform(get("/api/tracker/validate")
                .param("clientCode", "TT-CLIENT01"))
                .andExpect(status().isForbidden());
    }

    @Test
    void inactiveClientCannotLoadHistory() throws Exception {
        Client client = clientRepository.findById("TT-CLIENT01")
                .orElseThrow();

        client.setActive(false);
        clientRepository.save(client);

        mockMvc.perform(get("/api/tracker/history")
                .param("clientCode", "TT-CLIENT01"))
                .andExpect(status().isForbidden());
    }

    @Test
    void inactiveClientCannotLogEntry() throws Exception {
        Client client = clientRepository.findById("TT-CLIENT01")
                .orElseThrow();

        client.setActive(false);
        clientRepository.save(client);

        mockMvc.perform(post("/api/tracker/log")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "clientCode": "TT-CLIENT01",
                          "date": "2026-11-11",
                          "calories": 2000,
                          "protein": 150,
                          "water": 64
                        }
                        """))
                .andExpect(status().isForbidden());
    }
}

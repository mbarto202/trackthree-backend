package com.trackthree.tracker;

import com.trackthree.tracker.model.Client;
import com.trackthree.tracker.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

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
        clientRepository.save(new Client("TT-CLIENT01", false));
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
}

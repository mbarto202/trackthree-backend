package com.trackthree.tracker;

import com.trackthree.tracker.model.Client;
import com.trackthree.tracker.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
        clientRepository.save(new Client("TT-ADMIN01", true));
        clientRepository.save(new Client("TT-CLIENT01", false));
    }

    @Test
    void adminCanCreateClient() throws Exception {
        mockMvc.perform(post("/api/clients")
                        .header("X-Admin-Code", "TT-ADMIN01")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"TT-CLIENT02\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("TT-CLIENT02"))
                .andExpect(jsonPath("$.admin").value(false));

        assertTrue(clientRepository.existsById("TT-CLIENT02"));
    }

    @Test
    void nonAdminCannotCreateClient() throws Exception {
        mockMvc.perform(post("/api/clients")
                        .header("X-Admin-Code", "TT-CLIENT01")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"TT-CLIENT02\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void blankClientCodeIsRejected() throws Exception {
        mockMvc.perform(post("/api/clients")
                        .header("X-Admin-Code", "TT-ADMIN01")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\" \"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void duplicateClientCodeIsRejected() throws Exception {
        mockMvc.perform(post("/api/clients")
                        .header("X-Admin-Code", "TT-ADMIN01")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"TT-CLIENT01\"}"))
                .andExpect(status().isConflict());
    }
}

package com.trackthree.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackthree.tracker.model.Client;
import com.trackthree.tracker.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
        clientRepository.save(new Client("TT-ADMIN01", true));
        clientRepository.save(new Client("TT-CLIENT01", false));
    }

    @Test
    void adminCanCreateClient() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/clients")
                        .header("X-Admin-Code", "TT-ADMIN01"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(matchesPattern("TT-[0-9A-F]{6}")))
                .andExpect(jsonPath("$.admin").value(false))
                .andReturn();

        String generatedCode = readGeneratedCode(result);
        assertTrue(clientRepository.existsById(generatedCode));
    }

    @Test
    void nonAdminCannotCreateClient() throws Exception {
        mockMvc.perform(post("/api/clients")
                        .header("X-Admin-Code", "TT-CLIENT01"))
                .andExpect(status().isForbidden());
    }

    @Test
    void missingAdminCodeIsRejected() throws Exception {
        mockMvc.perform(post("/api/clients"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eachClientGetsDifferentCode() throws Exception {
        MvcResult firstResult = createClientAsAdmin();
        MvcResult secondResult = createClientAsAdmin();

        String firstCode = readGeneratedCode(firstResult);
        String secondCode = readGeneratedCode(secondResult);

        assertNotEquals(firstCode, secondCode);
    }

    private MvcResult createClientAsAdmin() throws Exception {
        return mockMvc.perform(post("/api/clients")
                        .header("X-Admin-Code", "TT-ADMIN01"))
                .andExpect(status().isCreated())
                .andReturn();
    }

    private String readGeneratedCode(MvcResult result) throws Exception {
        String responseBody = result.getResponse().getContentAsString();
        return objectMapper.readTree(responseBody).get("code").asText();
    }
}

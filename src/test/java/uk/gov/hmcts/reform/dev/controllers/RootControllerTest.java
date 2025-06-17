package uk.gov.hmcts.reform.dev.controllers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RootControllerTest {

    @Test
    void testWelcomeEndpoint() throws Exception {
        RootController rootController = new RootController();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(rootController).build();

        mockMvc.perform(get("/")
                            .accept(MediaType.TEXT_PLAIN))
            .andExpect(status().isOk())
            .andExpect(content().string("Welcome to test-backend"));
    }

    @Test
    void testWelcomeMethodDirectly() {
        RootController rootController = new RootController();
        ResponseEntity<String> response = rootController.welcome();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Welcome to test-backend", response.getBody());
    }
}

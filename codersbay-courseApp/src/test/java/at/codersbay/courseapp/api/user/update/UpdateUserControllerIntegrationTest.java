package at.codersbay.courseapp.api.user.update;

import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UpdateUserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user = new User("maxmuster", "Max", "Mustermann",
                "max@mustermann.com", "pass123");
        savedUser = userRepository.save(user);
    }

    @Test
    void update_validData_returns200() throws Exception {
        String json = String.format("""
                {
                  "id": %d,
                  "firstName": "Maximilian",
                  "email": "neu@mustermann.com"
                }
                """, savedUser.getId());

        mockMvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void update_unknownUser_returns404() throws Exception {
        String json = """
                {
                  "id": 9999,
                  "firstName": "Test"
                }
                """;

        mockMvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_invalidId_returns400() throws Exception {
        String json = """
                {
                  "id": 0,
                  "firstName": "Test"
                }
                """;

        mockMvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        userRepository.flush();
        User user = new User("maxmuster", "Max", "Mustermann",
                "max@mustermann.com", "pass123");
        savedUser = userRepository.save(user);
    }

    @Test
    void update_validData_returns200() throws Exception {
        String json = """
            {
              "id": %d,
              "firstName": "Maximilian",
              "lastName": "Neuname",
              "email": "neu@mustermann.com"
            }
            """.formatted(savedUser.getId());

        mockMvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Maximilian"))
                .andExpect(jsonPath("$.lastName").value("Neuname"))
                .andExpect(jsonPath("$.email").value("neu@mustermann.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void update_onlyEmail_keepsOtherFieldsUnchanged_returns200() throws Exception {
        String json = """
            {
              "id": %d,
              "email": "neue@email.com"
            }
            """.formatted(savedUser.getId());

        mockMvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("neue@email.com"))
                .andExpect(jsonPath("$.firstName").value("Max"))
                .andExpect(jsonPath("$.lastName").value("Mustermann"))
                .andExpect(jsonPath("$.userName").value("maxmuster"));
    }

    @Test
    void update_onlyFirstName_keepsOtherFieldsUnchanged_returns200() throws Exception {
        String json = """
            {
              "id": %d,
              "firstName": "Maximilian"
            }
            """.formatted(savedUser.getId());

        mockMvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Maximilian"))
                .andExpect(jsonPath("$.lastName").value("Mustermann"))
                .andExpect(jsonPath("$.email").value("max@mustermann.com"));
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

    @Test
    void update_negativeId_returns400() throws Exception {
        String json = """
            {
              "id": -1,
              "firstName": "Test"
            }
            """;

        mockMvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_passwordNeverInResponse_returns200() throws Exception {
        String json = """
            {
              "id": %d,
              "firstName": "Maximilian"
            }
            """.formatted(savedUser.getId());

        mockMvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void update_changesPersistedInDatabase_returns200() throws Exception {
        String json = """
            {
              "id": %d,
              "firstName": "Persistenz"
            }
            """.formatted(savedUser.getId());

        mockMvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        // Danach per GET prüfen ob wirklich gespeichert
        mockMvc.perform(get("/api/user/" + savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Persistenz"))
                .andExpect(jsonPath("$.lastName").value("Mustermann"));
    }

// ---------- Doppelte Email ----------

    @Test
    void update_duplicateEmail_returnsConflictOrError() throws Exception {
        // Zweiten User anlegen
        userRepository.save(new User("anna", "Anna", "Beispiel",
                "anna@beispiel.com", "pass456"));

        // Versuche die Email des ersten Users auf die des zweiten zu ändern
        String json = """
            {
              "id": %d,
              "email": "anna@beispiel.com"
            }
            """.formatted(savedUser.getId());

        mockMvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict()).andDo(print());
    }
}

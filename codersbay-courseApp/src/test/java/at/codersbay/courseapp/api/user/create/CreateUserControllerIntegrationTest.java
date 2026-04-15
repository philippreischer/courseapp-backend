package at.codersbay.courseapp.api.user.create;

import at.codersbay.courseapp.api.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CreateUserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void registerUser_validRequest_returns201() throws Exception {
        String json = """
                {
                  "userName": "maxmuster",
                  "firstName": "Max",
                  "lastName": "Mustermann",
                  "email": "max@mustermann.com",
                  "password": "securePass123"
                }
                """;

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("maxmuster"))
                .andExpect(jsonPath("$.email").value("max@mustermann.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void registerUser_missingUserName_returns400() throws Exception {
        String json = """
                {
                  "firstName": "Max",
                  "lastName": "Mustermann",
                  "email": "max@mustermann.com",
                  "password": "securePass123"
                }
                """;

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_missingFirstName_returns400() throws Exception {
        String json = """
                {
                  "userName": "maxmuster",
                  "lastName": "Mustermann",
                  "email": "max@mustermann.com",
                  "password": "securePass123"
                }
                """;

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_missingLastName_returns400() throws Exception {
        String json = """
                {
                  "userName": "maxmuster",
                  "firstName": "Max",
                  "email": "max@mustermann.com",
                  "password": "securePass123"
                }
                """;

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_missingEmail_returns400() throws Exception {
        String json = """
                {
                  "userName": "maxmuster",
                  "firstName": "Max",
                  "lastName": "Mustermann",
                  "password": "securePass123"
                }
                """;

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_missingPassword_returns400() throws Exception {
        String json = """
                {
                  "userName": "maxmuster",
                  "firstName": "Max",
                  "lastName": "Mustermann",
                  "email": "max@mustermann.com"
                }
                """;

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_duplicateUsername_returns409() throws Exception {
        String json = """
                {
                  "userName": "maxmuster",
                  "firstName": "Max",
                  "lastName": "Mustermann",
                  "email": "max@mustermann.com",
                  "password": "securePass123"
                }
                """;

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());
    }


    @Test
    void registerUser_duplicateEmail_returns409() throws Exception {
        String json = """
            {
              "userName": "maxmuster",
              "firstName": "Max",
              "lastName": "Mustermann",
              "email": "max@mustermann.com",
              "password": "securePass123"
            }
            """;

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());
    }

}

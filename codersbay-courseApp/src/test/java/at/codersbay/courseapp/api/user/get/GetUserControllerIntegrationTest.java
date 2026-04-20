package at.codersbay.courseapp.api.user.get;

import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class GetUserControllerIntegrationTest {

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
    void getAll_returnsListOfUsers() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userName").value("maxmuster"))
                .andExpect(jsonPath("$[0].password").doesNotExist());
    }

    @Test
    void getById_existingUser_returns200() throws Exception {
        mockMvc.perform(get("/api/user/" + savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("maxmuster"))
                .andExpect(jsonPath("$.email").value("max@mustermann.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void getById_unknownId_returns404() throws Exception {
        mockMvc.perform(get("/api/user/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByUserName_existingUser_returns200() throws Exception {
        mockMvc.perform(get("/api/user/byUserName/maxmuster"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("maxmuster"));
    }

    @Test
    void getByUserName_unknownUser_returns404() throws Exception {
        mockMvc.perform(get("/api/user/byUserName/unknown"))
                .andExpect(status().isNotFound());
    }
}
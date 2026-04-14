package at.codersbay.courseapp.api.user.delete;


import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class DeleteUserControllerIntegrationTest {

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
    void deleteById_existingUser_returns202() throws Exception {

        mockMvc.perform(delete("/api/user/" + savedUser.getId()))
                .andExpect(status().isAccepted());
    }

    @Test
    void deleteById_unknownId_returns404() throws Exception {

        mockMvc.perform(delete("/api/user/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteByUserName_existingUser_returns202() throws Exception {

        mockMvc.perform(delete("/api/user/byUserName/maxmuster"))
                .andExpect(status().isAccepted());
    }

    @Test
    void deleteByUserName_unknownUser_returns404() throws Exception {

        mockMvc.perform(delete("/api/user/byUserName/unknown"))
                .andExpect(status().isNotFound());
    }

}

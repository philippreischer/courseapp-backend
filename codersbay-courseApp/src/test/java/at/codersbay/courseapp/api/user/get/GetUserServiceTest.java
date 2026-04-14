package at.codersbay.courseapp.api.user.get;

import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import at.codersbay.courseapp.api.user.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserService getUserService;

    @Test
    void getById_existingId_returnsUser() {

        User user = new User("Maxi", "Max", "Mustermann",
                "max@mustermann.com", "pass123");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = getUserService.getById(1L);

        assertThat(result.getUserName()).isEqualTo("Maxi");
        assertThat(result.getEmail()).isEqualTo("max@mustermann.com");
    }

    @Test
    void getById_userNotFound_throwsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> getUserService.getById(99L));
    }

    @Test
    void getByUserName_existingName_returnsUser() {
        User user = new User("Maxi", "Max", "Mustermann",
                "max@mustermann.com", "pass123");
        when(userRepository.findByUserName("Maxi")).thenReturn(Optional.of(user));

        User result = getUserService.getByUserName("Maxi");

        assertThat(result.getUserName()).isEqualTo("Maxi");
    }

    @Test
    void getByUserName_nameNotFound_throwsException() {
        when(userRepository.findByUserName("Unknown")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> getUserService.getByUserName("Unknown"));
    }

    @Test
    void getAll_multipleUsers_returnsList() {
        User user1 = new User("Maxi", "Max", "Mustermann",
                "max@mustermann.com", "pass123");
        User user2 = new User("Anna", "Anna", "Beispiel",
                "anna@beispiel.com", "pass456");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> result = getUserService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUserName()).isEqualTo("Maxi");
        assertThat(result.get(1).getUserName()).isEqualTo("Anna");
    }

    @Test
    void getAll_noUsers_returnsEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = getUserService.getAll();

        assertThat(result).isEmpty();
    }
}
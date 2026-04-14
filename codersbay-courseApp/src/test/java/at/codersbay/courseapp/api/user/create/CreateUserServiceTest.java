package at.codersbay.courseapp.api.user.create;

import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import at.codersbay.courseapp.api.user.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CreateUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateUserService createUserService;

    @Test
    void createUser_validInput_returnsCreatedUser() {
        // Arrange
        when(userRepository.findByUserName("Maxi")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("max@mustermann.com")).thenReturn(Optional.empty());

        User savedUser = new User("Maxi", "Max", "Mustermann",
                "max@mustermann.com", "pass123");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = createUserService.createUser(
                "Maxi", "Max", "Mustermann",
                "max@mustermann.com", "pass123");

        // Assert
        assertThat(result.getUserName()).isEqualTo("Maxi");
        assertThat(result.getEmail()).isEqualTo("max@mustermann.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_userNameExists_throwsException() {
        User existing = new User();
        when(userRepository.findByUserName("Maxi")).thenReturn(Optional.of(existing));

        assertThrows(UserAlreadyExistsException.class, () ->
                createUserService.createUser("Maxi", "Max", "Mustermann",
                        "max@mustermann.com", "pass123")
        );

        verify(userRepository, never()).save(any(User.class));
    }

}

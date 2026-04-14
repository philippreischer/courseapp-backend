package at.codersbay.courseapp.api.user.update;

import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import at.codersbay.courseapp.api.exceptions.InvalidUpdateException;
import at.codersbay.courseapp.api.user.exceptions.UserNotFoundException;
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
public class UpdateUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUserService updateUserService;

    @Test
    void update_validData_updatesUserSuccessfully() {
        User existingUser = new User("Maxi", "Max", "Mustermann",
                "max@mustermann.com", "pass123");
        existingUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(
                invocation -> invocation.getArgument(0));

        UpdateUserRequestDTO dto = new UpdateUserRequestDTO();
        dto.setId(1L);
        dto.setFirstName("Maximilian");
        dto.setLastName("Neu");
        dto.setEmail("neu@mustermann.com");

        User result = updateUserService.update(dto);

        assertThat(result.getFirstName()).isEqualTo("Maximilian");
        assertThat(result.getLastName()).isEqualTo("Neu");
        assertThat(result.getEmail()).isEqualTo("neu@mustermann.com");
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void update_onlyEmail_keepsOtherFieldsUnchanged() {
        User existingUser = new User("Maxi", "Max", "Mustermann",
                "max@mustermann.com", "pass123");
        existingUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(
                invocation -> invocation.getArgument(0));

        UpdateUserRequestDTO dto = new UpdateUserRequestDTO();
        dto.setId(1L);
        dto.setEmail("neu@mustermann.com");

        User result = updateUserService.update(dto);

        assertThat(result.getEmail()).isEqualTo("neu@mustermann.com");
        assertThat(result.getFirstName()).isEqualTo("Max");
        assertThat(result.getLastName()).isEqualTo("Mustermann");
    }

    @Test
    void update_userNotFound_throwsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        UpdateUserRequestDTO dto = new UpdateUserRequestDTO();
        dto.setId(99L);
        dto.setFirstName("Test");

        assertThrows(UserNotFoundException.class,
                () -> updateUserService.update(dto));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void update_nullDto_throwsException() {
        assertThrows(InvalidUpdateException.class,
                () -> updateUserService.update(null));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void update_nullId_throwsException() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO();
        dto.setId(null);

        assertThrows(InvalidUpdateException.class,
                () -> updateUserService.update(dto));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void update_invalidId_throwsException() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO();
        dto.setId(0L);

        assertThrows(InvalidUpdateException.class,
                () -> updateUserService.update(dto));

        verify(userRepository, never()).save(any(User.class));
    }
}

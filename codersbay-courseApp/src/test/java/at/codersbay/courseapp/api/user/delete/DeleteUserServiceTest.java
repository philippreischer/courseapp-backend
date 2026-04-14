package at.codersbay.courseapp.api.user.delete;

import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import at.codersbay.courseapp.api.user.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class DeleteUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeleteUserService deleteUserService;

    @Test
    void deleteById_existingId_deletesSuccessfully() {

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user))
                .thenReturn(Optional.empty());

        deleteUserService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_userNotFound_throwsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> deleteUserService.deleteById(99L));

        verify(userRepository, never()).deleteById(anyLong());
    }
}

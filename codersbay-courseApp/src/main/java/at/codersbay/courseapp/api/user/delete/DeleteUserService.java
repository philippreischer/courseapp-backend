package at.codersbay.courseapp.api.user.delete;

import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import at.codersbay.courseapp.api.user.exceptions.UserDeletionException;
import at.codersbay.courseapp.api.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteUserService {

    @Autowired
    private UserRepository userRepository;

    public void deleteById(long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(
                    "Could not find user by id '" + id + "'.");
        }

        userRepository.deleteById(id);

        if (userRepository.findById(id).isPresent()) {
            throw new UserDeletionException(
                    "Could not delete user by id '" + id + "'.");
        }
    }

    public void deleteByUserName(String userName) {
        Optional<User> optionalUser = userRepository.findByUserName(userName);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(
                    "Could not find user by username '" + userName + "'.");
        }

        User user = optionalUser.get();
        userRepository.deleteById(user.getId());

        if (userRepository.findByUserName(userName).isPresent()) {
            throw new UserDeletionException(
                    "Could not delete user by username '" + userName + "'.");
        }
    }
}

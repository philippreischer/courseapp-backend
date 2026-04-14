package at.codersbay.courseapp.api.user.create;

import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import at.codersbay.courseapp.api.user.exceptions.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateUserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(String userName, String firstName, String lastName,
                           String email, String password) {

        Optional<User> existingUserName = userRepository.findByUserName(userName);
        if (existingUserName.isPresent()) {
            throw new UserAlreadyExistsException("User ist bereits in Verwendung");
        }

        Optional<User> existingEmail = userRepository.findByEmail(email);
        if (existingEmail.isPresent()) {
            throw new UserAlreadyExistsException("Email ist bereits in Verwendung");
        }

        User user = new User(userName, firstName, lastName, email, password);
        return userRepository.save(user);
    }
}

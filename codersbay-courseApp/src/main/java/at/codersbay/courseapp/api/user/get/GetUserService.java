package at.codersbay.courseapp.api.user.get;

import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import at.codersbay.courseapp.api.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetUserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "could not find user by id '" + id + "'."));
    }

    public User getByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException(
                        "could not find user by username '" + userName + "'."));
    }
}

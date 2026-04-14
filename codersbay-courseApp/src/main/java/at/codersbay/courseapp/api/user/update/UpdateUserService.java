package at.codersbay.courseapp.api.user.update;

import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import at.codersbay.courseapp.api.exceptions.InvalidUpdateException;
import at.codersbay.courseapp.api.user.exceptions.UserNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateUserService {

    @Autowired
    private UserRepository userRepository;

    public User update(UpdateUserRequestDTO updateUserDTO) {

        if (updateUserDTO == null || updateUserDTO.getId() == null || updateUserDTO.getId() < 1) {
            throw new InvalidUpdateException("invalid update data");
        }

        Optional<User> optionalUser = userRepository.findById(updateUserDTO.getId());

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(
                    "could not find user by id '" + updateUserDTO.getId() + "'.");
        }

        User user = optionalUser.get();

        if (!StringUtils.isEmpty(updateUserDTO.getEmail())) {
            user.setEmail(updateUserDTO.getEmail());
        }

        if (!StringUtils.isEmpty(updateUserDTO.getFirstName())) {
            user.setFirstName(updateUserDTO.getFirstName());
        }

        if (!StringUtils.isEmpty(updateUserDTO.getLastName())) {
            user.setLastName(updateUserDTO.getLastName());
        }

        return userRepository.save(user);
    }
}

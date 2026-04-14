package at.codersbay.courseapp.api.user.update;

import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserResponseBody;
import at.codersbay.courseapp.api.exceptions.InvalidUpdateException;
import at.codersbay.courseapp.api.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UpdateUserController {

    @Autowired
    private UpdateUserService updateUserService;

    @PutMapping
    public ResponseEntity<UserResponseBody> update(
            @RequestBody UpdateUserRequestDTO updateUserDTO) {

        User user = updateUserService.update(updateUserDTO);
        return ResponseEntity.ok(new UserResponseBody(user));
    }
}
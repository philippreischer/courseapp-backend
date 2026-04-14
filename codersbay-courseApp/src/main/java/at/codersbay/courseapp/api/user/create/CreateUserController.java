package at.codersbay.courseapp.api.user.create;
import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class CreateUserController {

    @Autowired
    private CreateUserService createUserService;

    @PostMapping
    public ResponseEntity<UserResponseBody> create(
            @RequestBody CreateUserRequestDTO createUserDTO) {

        if (createUserDTO == null
                || StringUtils.isEmpty(createUserDTO.getUserName())
                || StringUtils.isEmpty(createUserDTO.getFirstName())
                || StringUtils.isEmpty(createUserDTO.getLastName())
                || StringUtils.isEmpty(createUserDTO.getEmail())
                || StringUtils.isEmpty(createUserDTO.getPassword())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = createUserService.createUser(
                createUserDTO.getUserName(),
                createUserDTO.getFirstName(),
                createUserDTO.getLastName(),
                createUserDTO.getEmail(),
                createUserDTO.getPassword()
        );
        return new ResponseEntity<>(new UserResponseBody(user), HttpStatus.CREATED);


    }
}

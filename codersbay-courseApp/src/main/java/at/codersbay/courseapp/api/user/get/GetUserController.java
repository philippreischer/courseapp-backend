package at.codersbay.courseapp.api.user.get;
import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserResponseBody;
import at.codersbay.courseapp.api.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class GetUserController {

    @Autowired
    private GetUserService getUserService;

    @GetMapping
    public ResponseEntity<List<UserResponseBody>> getAll() {
        List<UserResponseBody> users = getUserService.getAll().stream()
                .map(UserResponseBody::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseBody> getById(@PathVariable long id) {
        User user = getUserService.getById(id);
        return ResponseEntity.ok(new UserResponseBody(user));
    }

    @GetMapping("/byUserName/{userName}")
    public ResponseEntity<UserResponseBody> getByUsername(@PathVariable String userName) {
        User user = getUserService.getByUserName(userName);
        return ResponseEntity.ok(new UserResponseBody(user));
    }
}
package at.codersbay.courseapp.api.user.delete;

import at.codersbay.courseapp.api.response.ResponseBody;
import at.codersbay.courseapp.api.user.exceptions.UserDeletionException;
import at.codersbay.courseapp.api.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class DeleteUserController {

    @Autowired
    private DeleteUserService deleteUserService;

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody> delete(@PathVariable long id) {

        ResponseBody responseBody = new ResponseBody();
        deleteUserService.deleteById(id);
        responseBody.addMessage("Ok");

        return new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);

    }

    @DeleteMapping("/byUserName/{userName}")
    public ResponseEntity<ResponseBody> deleteByUsername(@PathVariable String userName) {

        ResponseBody responseBody = new ResponseBody();
        deleteUserService.deleteByUserName(userName);
        responseBody.addMessage("Ok");

        return new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);
    }
}
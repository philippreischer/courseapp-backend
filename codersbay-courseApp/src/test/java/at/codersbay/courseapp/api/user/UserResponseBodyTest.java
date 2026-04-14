package at.codersbay.courseapp.api.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserResponseBodyTest {

    @Test
    void userResponseBody_doesNotExposePassword() {
        User user = new User("Maxi", "Max", "Mustermann",
                "max@mustermann.com", "geheim123");

        UserResponseBody body = new UserResponseBody(user);

        assertThat(body.getClass().getDeclaredMethods())
                .extracting("name")
                .doesNotContain("getPassword");
    }
}

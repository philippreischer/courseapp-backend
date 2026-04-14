package at.codersbay.courseapp.api.user.update;

import at.codersbay.courseapp.api.user.create.CreateUserRequestDTO;

public class UpdateUserRequestDTO extends CreateUserRequestDTO {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

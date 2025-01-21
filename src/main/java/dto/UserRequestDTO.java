package dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {

    public String firstName;
    public String lastName;
    public String email;
    public String username;
    public String password;
    public String phoneNumber;
    public String address;

    public UserRequestDTO(String firstName, String lastName, String email, String username, String password, String phoneNumber, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public UserRequestDTO() {
    }
}
package cat.cultura.backend.dtos;

import cat.cultura.backend.entity.Role;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
public class UserDto {
    private Long id;
    private String username;
    private String nameAndSurname;
    private String email;
    private String creationDate;

    private Role role = Role.USER;

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private int points;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNameAndSurname() {
        return nameAndSurname;
    }

    public void setNameAndSurname(String nameAndSurname) {
        this.nameAndSurname = nameAndSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}

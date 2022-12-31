package cat.cultura.backend.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
@DiscriminatorValue("Administrator")
public class Administrator extends Organizer {
    public Administrator() {
        super();
    }

    public Administrator(String username) {
        super(username, Role.ADMIN);
    }
}

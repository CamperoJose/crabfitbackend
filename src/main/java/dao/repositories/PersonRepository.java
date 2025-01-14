package dao.repositories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "person")
public class PersonRepository extends PanacheEntity {

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String username;

    @NotNull
    @Email
    @Size(max = 255)
    private String mail;

    @NotNull
    @Size(max = 512)
    @JsonIgnore
    private String secret;

    @NotNull
    private Date created_at;

    public PersonRepository() {
    }

    public PersonRepository(String name, String username, String mail, String secret, Date created_at) {
        this.name = name;
        this.username = username;
        this.mail = mail;
        this.secret = secret;
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "PersonDao{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", mail='" + mail + '\'' +
                ", secret='" + secret + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
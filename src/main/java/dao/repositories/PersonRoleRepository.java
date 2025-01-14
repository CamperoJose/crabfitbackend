package dao.repositories;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "person_role")
public class PersonRoleRepository extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private PersonRepository person;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleRepository role;

    @NotNull
    private boolean isActive;

    public PersonRoleRepository() {
    }

    public PersonRoleRepository(PersonRepository person, RoleRepository role, boolean isActive) {
        this.person = person;
        this.role = role;
        this.isActive = isActive;
    }

    public PersonRepository getPerson() {
        return person;
    }

    public void setPerson(PersonRepository person) {
        this.person = person;
    }

    public RoleRepository getRole() {
        return role;
    }

    public void setRole(RoleRepository role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "PersonRole{" +
                "id=" + id +
                ", person=" + person +
                ", role=" + role +
                ", isActive=" + isActive +
                '}';
    }
}

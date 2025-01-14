package dao.repositories;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "role")
public class RoleRepository extends PanacheEntity {

    @Size(max = 255)
    private String roleName;

    public RoleRepository() {
    }

    public RoleRepository(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}

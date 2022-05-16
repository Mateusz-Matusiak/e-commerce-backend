package pl.fruitmachine.user.role;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.fruitmachine.user.role.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}

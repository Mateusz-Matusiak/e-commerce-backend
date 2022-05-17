package pl.fruitmachine.user;

import pl.fruitmachine.user.role.Role;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String email, String roleName);
    User getUser(String email);
    List<User> getUsers();
    Optional<User> getUserById(Long id);
}

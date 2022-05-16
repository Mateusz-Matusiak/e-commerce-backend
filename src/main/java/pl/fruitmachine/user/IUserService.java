package pl.fruitmachine.user;

import pl.fruitmachine.user.role.Role;

import java.util.List;

public interface IUserService {

    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String email, String roleName);
    User getUser(String email);
    List<User> getUsers();
}

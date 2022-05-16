package pl.fruitmachine.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fruitmachine.user.role.Role;
import pl.fruitmachine.user.role.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    public User saveUser(User user) {
        log.info("Saving new user to DB");
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role to DB");
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        log.info("Adding role {} to {}", roleName, email);
        User user = userRepository.findByEmail(email);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public User getUser(String email) {
        log.info("Getting user from DB");
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getUsers() {
        log.info("Getting all users from DB");
        return userRepository.findAll();
    }
}

package pl.fruitmachine.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.fruitmachine.user.role.Role;
import pl.fruitmachine.user.role.RoleRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user==null) {
            log.info("User not found");
            throw new UsernameNotFoundException("User not found");
        } else{
            log.info("User found {} ", email);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role-> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }


    public User saveUser(User user) {
        log.info("Saving new user to DB");
        if(user.getPassword() == null || user.getEmail()==null || user.getFirstName()==null || user.getLastName()==null){
            return null;
        }
        user.setCoins(5000);
        user.setScore(500);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        addRoleToUser(user.getEmail(), "ROLE_USER");
        return user;
    }

    public User saveManager(User user) {
        log.info("Saving new manager to DB");
        if(user.getPassword() == null || user.getEmail()==null || user.getFirstName()==null || user.getLastName()==null){
            return null;
        }
        user.setCoins(10000000);
        user.setScore(0);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        addRoleToUser(user.getEmail(), "ROLE_USER");
        addRoleToUser(user.getEmail(), "ROLE_MANAGER");
        return user;
    }

    public Role saveRole(Role role) {
        log.info("Saving new role to DB");
        return roleRepository.save(role);
    }

    public void addRoleToUser(String email, String roleName) {
        log.info("Adding role {} to {}", roleName, email);
        User user = userRepository.findByEmail(email);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    public User getUser(String email) {
        log.info("Getting user from DB");
        return userRepository.findByEmail(email);
    }

    public List<User> getUsers() {
        log.info("Getting all users from DB");
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Long id){
        return roleRepository.findById(id);
    }

    public Optional<User> updateStatisticsById(Long id, Integer coins, Integer score){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty())
            return user;

        User userUpdate = user.get();

        userUpdate.setScore(userUpdate.getScore() + score);
        userUpdate.setCoins(userUpdate.getCoins() + coins);
        return Optional.of(userUpdate);
    }
}

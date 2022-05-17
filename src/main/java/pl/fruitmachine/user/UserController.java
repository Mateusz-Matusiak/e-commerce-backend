package pl.fruitmachine.user;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody User user){
        User createdUser = userService.saveUser(user);
        if(createdUser == null){
            return ResponseEntity.badRequest().build();
        } else{
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/users/" + createdUser.getId()).toUriString());
            return ResponseEntity.created(uri).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        Optional<User> userById = userService.getUserById(id);
        if(userById.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else{
            return ResponseEntity.ok().body(userById.get());
        }
    }
}

package pl.fruitmachine.user;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/user/register")
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

    @PostMapping("/manager/register")
    public ResponseEntity<User> addManager(@RequestBody User user){
        User createdUser = userService.saveManager(user);
        if(createdUser == null){
            return ResponseEntity.badRequest().build();
        } else{
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/users/" + createdUser.getId()).toUriString());
            return ResponseEntity.created(uri).build();
        }
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        Optional<User> userById = userService.getUserById(id);
        if(userById.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else{
            return ResponseEntity.ok().body(userById.get());
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateStatistics(@RequestBody CoinsAndScore coinsAndScore, @PathVariable Long id, Principal principal){
        User currentUser = userService.getUser(principal.getName());
        if(!Objects.equals(currentUser.getId(), id)){
            return ResponseEntity.status(403).build();
        }

        Optional<User> user = userService.updateStatisticsById(id, coinsAndScore.getCoins(), coinsAndScore.getScore());
        if(user.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(user.get());
    }
}

@Data
class CoinsAndScore{
    private Integer coins;
    private Integer score;
}

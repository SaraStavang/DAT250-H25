package DAT250.Exercise.Experiment.controller;

import DAT250.Exercise.Experiment.PollManager;
import DAT250.Exercise.Experiment.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import DAT250.Exercise.Experiment.PollManager;
import DAT250.Exercise.Experiment.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = { "http://localhost:8080" })
public class UserController {

    private final PollManager pollManager;

    public UserController(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    // ===== DTOs =====
    public static class CreateUserRequest {
        public String username;
        public String email;
    }

    public static class UserDto {
        public Long id;
        public String username;
        public String email;

        public static UserDto of(User u) {
            UserDto d = new UserDto();
            d.id = u.getId();
            d.username = u.getUsername();
            d.email = u.getEmail();
            return d;
        }
    }

    // ===== Endpoints =====

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequest body) {
        User created = pollManager.createUser(body.username, body.email);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.of(created));
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        // PollManager#getUsers returns List<User>
        return pollManager.getUsers().stream().map(UserDto::of).collect(Collectors.toList());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        try {
            return ResponseEntity.ok(UserDto.of(pollManager.getUser(username)));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Nice-to-have: map duplicate-username errors to 400 with a message
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}

package com.internalworkflow.web.api;

import com.internalworkflow.domain.User;
import com.internalworkflow.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static com.internalworkflow.web.api.dto.Models.UserDto;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> list() {
        return userService.findAll().stream().map(UserDto::from).collect(Collectors.toList());
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> create(@Valid @RequestBody CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setPassword(request.getPassword());
        User saved = userService.register(user);
        return ResponseEntity.created(URI.create("/users/" + saved.getId())).body(UserDto.from(saved));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return userService.findById(id)
            .map(existing -> {
                existing.setEmail(request.getEmail());
                if (request.getRole() != null) existing.setRole(request.getRole());
                if (request.getPassword() != null && !request.getPassword().isBlank()) {
                    existing.setPassword(request.getPassword());
                    existing = userService.register(existing);
                } else {
                    existing = userService.save(existing);
                }
                return ResponseEntity.ok(UserDto.from(existing));
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		userService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

    public static class CreateUserRequest {
        @NotBlank
        @Size(min = 3, max = 50)
        private String username;
        @NotBlank @Email
        private String email;
        @NotBlank @Size(min = 6, max = 100)
        private String password;
        private com.internalworkflow.domain.UserRole role;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public com.internalworkflow.domain.UserRole getRole() { return role; }
        public void setRole(com.internalworkflow.domain.UserRole role) { this.role = role; }
    }

    public static class UpdateUserRequest {
        @NotBlank @Email
        private String email;
        @Size(min = 6, max = 100)
        private String password;
        private com.internalworkflow.domain.UserRole role;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public com.internalworkflow.domain.UserRole getRole() { return role; }
        public void setRole(com.internalworkflow.domain.UserRole role) { this.role = role; }
    }
}

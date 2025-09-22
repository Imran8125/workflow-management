package com.internalworkflow.service;

import com.internalworkflow.domain.User;
import com.internalworkflow.domain.UserRole;
import com.internalworkflow.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<User> findAll() { return userRepository.findAll(); }
	public Optional<User> findById(Long id) { return userRepository.findById(id); }
	public Optional<User> findByUsername(String username) { return userRepository.findByUsername(username); }
	public User save(User user) { return userRepository.save(user); }
	public void deleteById(Long id) { userRepository.deleteById(id); }

	public boolean usernameExists(String username) {
		return userRepository.existsByUsername(username);
	}

	public User register(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public void seedAdminIfMissing() {
		if (!userRepository.existsByUsername("admin")) {
			User admin = new User();
			admin.setUsername("admin");
			admin.setEmail("admin@example.com");
			admin.setRole(UserRole.ADMIN);
			admin.setPassword(passwordEncoder.encode("admin123"));
			userRepository.save(admin);
		}
	}
}

package edu.jhu.eventservice.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.jhu.eventservice.dto.UserPublicResponse;
import edu.jhu.eventservice.dto.UserSelfResponse;
import edu.jhu.eventservice.models.User;
import edu.jhu.eventservice.security.AuthenticatedUser;
import edu.jhu.eventservice.services.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	private final UserService us;

    // Constructor
    public UserController(UserService us) {
        this.us = us;
    }
    
    // Public lookup — returns name and phone only (req 2)
    @GetMapping("/{userId}")
    public ResponseEntity<UserPublicResponse> getUserById(@PathVariable int userId) {
        User user = us.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new UserPublicResponse(user.getFirstName(), user.getLastName(), user.getPhoneNumber()));
    }

    // Full profile for the authenticated user only (req 3)
    @GetMapping("/me")
    public ResponseEntity<UserSelfResponse> getMyProfile() {
        return AuthenticatedUser.currentUserId()
                .map(us::getUserById)
                .map(u -> ResponseEntity.ok(new UserSelfResponse(
                        u.getUserId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getPhoneNumber())))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    // Update own profile — only the authenticated user may update their own account (reqs 4)
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable int userId, @RequestParam(value="firstName") String firstName,
    		@RequestParam(value="lastName") String lastName,
    		@RequestParam(value="email") String email,
    		@RequestParam(value="phoneNumber") String phoneNumber) {

        Integer callerId = AuthenticatedUser.currentUserId().orElse(null);
        if (callerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }
        if (callerId != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You may only update your own account");
        }

    	List<String> errors = validateUser(firstName, lastName, email);
        if (!errors.isEmpty()) {
        	System.out.println(errors.toString());
        	return ResponseEntity.badRequest().build();
        }

        User existingUser = us.getUserById(userId);
        if (existingUser != null) {
            existingUser.setFirstName(firstName);
            existingUser.setLastName(lastName);
            existingUser.setEmail(email);
            existingUser.setPhoneNumber(phoneNumber);
            us.addUser(existingUser);
            return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
        } else {
        	return ResponseEntity.notFound().build();
        }
    }

    // Delete own account only (req 5)
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        Integer callerId = AuthenticatedUser.currentUserId().orElse(null);
        if (callerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }
        if (callerId != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You may only delete your own account");
        }

        User user = us.getUserById(userId);
        if (user != null) {
            us.deleteUser(user);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
        } else {
        	return ResponseEntity.notFound().build();
        }
    }
    
    private List<String> validateUser(String firstName, String lastName, String email) {
    	List<String> errors = new ArrayList<>();
    	if (firstName == null || firstName.isEmpty()) {
        	errors.add("First name cannot be empty");
        }

        if (lastName == null || lastName.isEmpty()) {
        	errors.add("Last name cannot be empty");
        }

        Pattern validEmailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        if (!validEmailRegex.matcher(email).matches()) {
        	errors.add("Email must be in valid format: " + email);
        }

        return errors;
    }
}

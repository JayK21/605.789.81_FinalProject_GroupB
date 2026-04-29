package edu.jhu.eventservice.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.jhu.eventservice.models.User;
import edu.jhu.eventservice.services.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	private final UserService us;

    // Constructor
    public UserController(UserService us) {
        this.us = us;
    }
    
    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = us.getAllUsers();
        if (users.isEmpty()) {
        	return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    // Get a user by userId
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable int userId) {
    	User user = us.getUserById(userId);
        if (user == null) {
        	return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    
    // Add a new user
    @PostMapping
    public ResponseEntity<String> addUser(@RequestParam(value="firstName") String firstName, @RequestParam(value="lastName") String lastName,
    		@RequestParam(value="email") String email, @RequestParam(value="password") String password) {
    	List<String> errors = validateUser(firstName, lastName, email, password);
        
        if (!errors.isEmpty()) {
        	System.out.println(errors.toString());
        	return ResponseEntity.badRequest().build();
        }
        
        User user = new User(null, firstName, lastName, email, password);
        us.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully");
    }
    
    // Update an existing professor
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable int userId, @RequestParam(value="firstName") String firstName, 
    		@RequestParam(value="lastName") String lastName,
    		@RequestParam(value="email") String email, @RequestParam(value="password") String password) {
    	List<String> errors = validateUser(firstName, lastName, email, password);
        
        if (!errors.isEmpty()) {
        	System.out.println(errors.toString());
        	return ResponseEntity.badRequest().build();
        }
        User existingUser = us.getUserById(userId);
        if (existingUser != null) {
            existingUser.setFirstName(firstName);
            existingUser.setLastName(lastName);
            existingUser.setEmail(email);
            existingUser.setPassword(password);
            us.addUser(existingUser); 
            return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
        } else {
        	System.out.println("User with id " + userId + " not found");
        	return ResponseEntity.notFound().build(); 
        }
    }
    
    // Delete a professor
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        User user = us.getUserById(userId);
        if (user != null) {
            us.deleteUser(user);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
        } else {
        	System.out.println("User with id " + userId + " not found");
        	return ResponseEntity.notFound().build();
        }
    }
    
    private List<String> validateUser(String firstName, String lastName, String email, String password){
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
        
        if (password == null || password.isEmpty()) {
        	errors.add("Last name cannot be empty");
        }
        
        return errors;
    }
}

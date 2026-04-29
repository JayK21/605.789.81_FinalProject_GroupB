package edu.jhu.eventservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.jhu.eventservice.models.User;
import edu.jhu.eventservice.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository ur;
	
    // add a new user
    public User addUser(User user) {
        return ur.save(user);
    }
	
    // retrieve all users
    public List<User> getAllUsers() {
        return ur.findAll(); 
    }

    // retrieve a user by its user ID
    public User getUserById(Integer userId) {
    	if (ur.findByUserId(userId).isPresent()) {
    		return ur.findByUserId(userId).get();
    	} else {
    		return null;
    	}
    }
    
    // delete a user
    public void deleteUser(User user) {
        ur.delete(user);
    }
}

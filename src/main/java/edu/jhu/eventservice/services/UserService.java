package edu.jhu.eventservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.jhu.eventservice.models.User;
import edu.jhu.eventservice.repositories.UserRepository;

public class UserService {
    @Autowired
    private UserRepository ur;
	
    // add a new course
    public User addUser(User user) {
        return ur.save(user);
    }
	
    // retrieve all courses
    public List<User> getAllUsers() {
        return ur.findAll(); 
    }

    // retrieve a course by its course number
    public User getUserById(Integer userId) {
    	if (ur.findByUserId(userId).isPresent()) {
    		return ur.findByUserId(userId).get();
    	} else {
    		return null;
    	}
    }
    
    // delete a course
    public void deleteUser(User user) {
        ur.delete(user);
    }
}

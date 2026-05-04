package edu.jhu.eventservice.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.jhu.eventservice.dto.UserPublicResponse;
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
    
    // retrieve all users
    public List<UserPublicResponse> getAllUsers(boolean publicResponse) {
    	List<User> users = ur.findAll();
    	List<UserPublicResponse> publicUsers = new ArrayList<>();
    	for (User u : users) {
    		publicUsers.add(new UserPublicResponse(u.getUserId(), u.getFirstName(), u.getLastName(), u.getPhoneNumber()));
    	}
        return publicUsers; 
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

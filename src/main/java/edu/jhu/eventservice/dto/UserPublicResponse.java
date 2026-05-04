package edu.jhu.eventservice.dto;

public class UserPublicResponse {

	private int userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public UserPublicResponse() {}

    public UserPublicResponse(int userId, String firstName, String lastName, String phoneNumber) {
    	this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}

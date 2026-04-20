package edu.jhu.eventservice.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Marks the class a rest controller
@RequestMapping("/api/v1/auth") // Requests made to /auth/anything will be handled by this class
public class AuthController {

}

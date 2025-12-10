package com.Rutuja.Resolve.IT.Controller;
import java.util.*;

import com.Rutuja.Resolve.IT.Model.User;
import com.Rutuja.Resolve.IT.Repositories.UserRepository;
import com.Rutuja.Resolve.IT.Service.UserService;

import jakarta.persistence.criteria.CriteriaBuilder.In;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody User user) {

        try {
            if (userService.getUserByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create account: " + e.getMessage());
        }

    }

    
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

  
    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }
   


    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
    User existingUser = userService.getUserByEmail(user.getEmail());
    if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", existingUser.getId());
        response.put("firstName", existingUser.getFirstName());
        response.put("email", existingUser.getEmail());
        response.put("role", "user");
        return ResponseEntity.ok(response);
    } else {
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
}


package com.cd.userservice.controller;

import com.cd.userservice.dto.UserRequestDTO;
import com.cd.userservice.dto.UserResponseDTO;
import com.cd.userservice.entity.User;
import com.cd.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;


    @GetMapping("/status/check")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("User service is working");
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid UserRequestDTO dto){
        return ResponseEntity.status(201).body(userService.createUser(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id){
        User user = userService.getUserById(id);
        UserResponseDTO responseDTO = UserResponseDTO
                .builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .id(user.getId())
                .role(user.getRole().toString())
                .build();
        return ResponseEntity.status(200).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }


}

package com.cd.userservice.service;

import com.cd.userservice.dto.UserRequestDTO;
import com.cd.userservice.entity.Role;
import com.cd.userservice.entity.User;
import com.cd.userservice.exception.UserNotFoundException;
import com.cd.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User createUser(UserRequestDTO userRequestDTO){
        User user = User.builder()
                .fullName(userRequestDTO.getFullName())
                .email(userRequestDTO.getEmail())
                .role(userRequestDTO.getRole())
                .build();

        return repository.save(user);
    }

    public User getUserById(Long id ){
       return repository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }


    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public String deleteUser(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        repository.deleteById(id);
        return "User with id : "  + user.getId()  +  "deleted successfully";
    }
}

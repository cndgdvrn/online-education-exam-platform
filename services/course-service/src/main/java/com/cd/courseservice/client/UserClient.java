package com.cd.courseservice.client;

import com.cd.courseservice.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


//service that we want to call is registered in Eureka
@FeignClient(name = "user-service", path = "/users")
public interface UserClient {

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id);

}

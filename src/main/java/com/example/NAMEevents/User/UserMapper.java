package com.example.NAMEevents.User;

import com.example.NAMEevents.Role.Role;
import com.example.NAMEevents.Role.RoleRepository;
import com.example.EventHub.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Autowired
    WebSecurityConfig webSecurityConfig;
    @Autowired
    RoleRepository roleRepository;
    public User toEntity(UserDTO userDTO){
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(webSecurityConfig.passwordEncoder().encode(userDTO.getPassword()));
        user.setRole(roleRepository.findById(3).orElse(null));
        return user;
    }
}

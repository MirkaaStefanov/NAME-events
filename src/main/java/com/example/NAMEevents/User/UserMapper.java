package com.example.NAMEevents.User;

import com.example.NAMEevents.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Autowired
    WebSecurityConfig webSecurityConfig;
    public User toEntity(UserDTO userDTO){
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(webSecurityConfig.passwordEncoder().encode(userDTO.getPassword()));
        user.setRole("User");
        user.setJob(userDTO.getJob());
        user.setGraduationPlace(userDTO.getGraduationPlace());
        user.setUserDescription(userDTO.getUserDescription());
        user.setSkillsPros(userDTO.getSkillsPros());
        user.setSkillsCons(userDTO.getSkillsCons());
        return user;
    }
}

package com.example.NAMEevents.User;

import com.example.NAMEevents.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Autowired
    WebSecurityConfig webSecurityConfig;

    public User toEntity(UserDTO userDTO) {
        User user = new User();

        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }

        if (userDTO.getFirstName() != null) {
            user.setFirstName(userDTO.getFirstName());
        }

        if (userDTO.getLastName() != null) {
            user.setLastName(userDTO.getLastName());
        }

        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }

        if (userDTO.getPassword() != null) {
            user.setPassword(webSecurityConfig.passwordEncoder().encode(userDTO.getPassword()));
        }

        user.setRole("User");

        if (userDTO.getJob() != null) {
            user.setJob(userDTO.getJob());
        }

        if (userDTO.getGraduationPlace() != null) {
            user.setGraduationPlace(userDTO.getGraduationPlace());
        }

        if (userDTO.getUserDescription() != null) {
            user.setUserDescription(userDTO.getUserDescription());
        }

        if (userDTO.getSkillsPros() != null) {
            user.setSkillsPros(userDTO.getSkillsPros());
        }

        if (userDTO.getSkillsCons() != null) {
            user.setSkillsCons(userDTO.getSkillsCons());
        }

        return user;
    }

}

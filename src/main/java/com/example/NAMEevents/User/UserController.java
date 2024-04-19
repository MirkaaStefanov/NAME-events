package com.example.NAMEevents.User;

import com.example.NAMEevents.Skill.SkillRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    private SkillRepository skillRepository;

    @GetMapping("/registration")
    public String addUser(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "user/registration";
    }
    @GetMapping("/registration/choose-pros")
    public String choosePros(@ModelAttribute UserDTO userDTO, Model model){
        model.addAttribute("userDto", userDTO);
        model.addAttribute("allSkills",skillRepository.findAll());
        return "user/registration-skills-pros";
    }
    @GetMapping("/registration/choose-cons")
    public String chooseCons(@ModelAttribute UserDTO userDTO, Model model){
        model.addAttribute("userDto", userDTO);
        model.addAttribute("allSkills",skillRepository.findAll());
        return "user/registration-skills-cons";
    }
    @PostMapping("/registration/submit")
    public String submitUser(@Valid @ModelAttribute UserDTO userDTO, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "user/registration";
        }
        User userForSeeIfUsernameExist = userRepository.getUserByUsername(userDTO.getUsername());
        if (userForSeeIfUsernameExist != null) {
            model.addAttribute("userExistMessage", "This username already exists!");
            return "user/registration";
        }

        User userForSeeIfEmailExist = userRepository.getUserByEmail(userDTO.getEmail());
        if (userForSeeIfEmailExist != null) {
            model.addAttribute("emailExistMessage", "This email already exists!");
            return "user/registration";
        }
        if (!userService.ifTwoPasswordsMatch(userDTO.getPassword(), userDTO.getConfirmPassword())) {
            model.addAttribute("passwordsDoNotMatch", "Passwords do not match!");
            return "user/registration";
        }
        User user = userMapper.toEntity(userDTO);
        userRepository.save(user);
        return "redirect:/login";
    }
}

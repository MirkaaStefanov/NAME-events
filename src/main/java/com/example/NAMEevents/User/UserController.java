package com.example.NAMEevents.User;

import com.example.NAMEevents.Event.Event;
import com.example.NAMEevents.Skill.SkillRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("userDto", new UserDTO());
        return "user/registration";
    }
    @GetMapping("/registration/choose-pros")
    public String choosePros(@ModelAttribute UserDTO userDTO,@RequestParam("id") Long id, Model model){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = userRepository.findById(id).get();
            model.addAttribute("userDto", userDTO);
            model.addAttribute("allSkills",skillRepository.findAll());
            return "user/registration-skills-pros";
        } else {
            return "id could not be find";
        }
    }
//    @PostMapping("/registration/choose-pros")
//    public String postChoosePros(BindingResult bindingResult,@RequestParam("id") Long id){
//        if (bindingResult.hasErrors()) {
//            return "user/registration-skills-pros";
//        } else {
////            User user = userRepository.findById(id).get();
////            getEvent(event, updatedEvent);
////            eventRepository.save(event);
////            model.addAttribute("event", event);
////            return "event-update-result";
//        }
//    }
    @GetMapping("/registration/choose-cons")
    public String chooseCons(@RequestParam("id") Long id, Model model){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = userRepository.findById(id).get();
            model.addAttribute("user", user);
            model.addAttribute("allSkills",skillRepository.findAll());
            return "user/registration-skills-cons";
        } else {
            return "id could not be find";
        }
    }
    @PostMapping("/registration/choose-cons")
    public String chooseCons(@ModelAttribute UserDTO userDTO, Model model){
        model.addAttribute("userDto", userDTO);
        model.addAttribute("allSkills",skillRepository.findAll());
        return "user/registration-skills-cons";
    }
    @PostMapping("/registration/submit")
    public String submitUser(@Valid @ModelAttribute UserDTO userDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "user/registration";
        }
        User userForSeeIfUsernameExist = userRepository.getUserByUsername(userDto.getUsername());
        if (userForSeeIfUsernameExist != null) {
            model.addAttribute("userExistMessage", "This username already exists!");
            return "user/registration";
        }

        User userForSeeIfEmailExist = userRepository.getUserByEmail(userDto.getEmail());
        if (userForSeeIfEmailExist != null) {
            model.addAttribute("emailExistMessage", "This email already exists!");
            return "user/registration";
        }
        if (!userService.ifTwoPasswordsMatch(userDto.getPassword(), userDto.getConfirmPassword())) {
            model.addAttribute("passwordsDoNotMatch", "Passwords do not match!");
            return "user/registration";
        }
        User user = userMapper.toEntity(userDto);
        userRepository.save(user);
        return "redirect:/login";
    }
}

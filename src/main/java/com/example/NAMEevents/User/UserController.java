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
    @PostMapping("/registration")
    public String userSubmit(@Valid @ModelAttribute UserDTO userDto, BindingResult bindingResult, Model model){
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
        return "redirect:/registration/choose-pros";
    }



    @GetMapping("/registration/choose-pros")
    public String choosePros(@ModelAttribute UserDTO userDTO, @RequestParam("id") Long id, Model model) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = userMapper.toEntity(userDTO);
            userRepository.save(user);
            model.addAttribute("user", user);
            model.addAttribute("allSkills", skillRepository.findAll());
            return "user/registration-skills-pros";
        } else {
            return "id could not be find";
        }
    }

    @PostMapping("/registration/choose-pros")
    public String postChoosePros(@ModelAttribute UserDTO userDto, BindingResult bindingResult, @RequestParam("id") Long id, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "user/registration-skills-pros";
        }
        User userForSeeIfUsernameExist = userRepository.getUserByUsername(userDto.getUsername());
        if (userForSeeIfUsernameExist != null) {
            model.addAttribute("userExistMessage", "This username already exists!");
            return "user/registration-skills-pros";
        }

        User userForSeeIfEmailExist = userRepository.getUserByEmail(userDto.getEmail());
        if (userForSeeIfEmailExist != null) {
            model.addAttribute("emailExistMessage", "This email already exists!");
            return "user/registration-skills-pros";
        }
        if (!userService.ifTwoPasswordsMatch(userDto.getPassword(), userDto.getConfirmPassword())) {
            model.addAttribute("passwordsDoNotMatch", "Passwords do not match!");
            return "user/registration-skills-pros";
        }
        User user = userMapper.toEntity(userDto);
        userRepository.save(user);
        model.addAttribute("user", user);
        return "user/registration-skills-cons";

}

    @PostMapping("/registration/choose-cons")
    public String chooseCons(@ModelAttribute UserDTO userDto, @RequestParam("id") Long id, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "user/registration-skills-cons";
        }
        User userForSeeIfUsernameExist = userRepository.getUserByUsername(userDto.getUsername());
        if (userForSeeIfUsernameExist != null) {
            model.addAttribute("userExistMessage", "This username already exists!");
            return "user/registration-skills-cons";
        }

        User userForSeeIfEmailExist = userRepository.getUserByEmail(userDto.getEmail());
        if (userForSeeIfEmailExist != null) {
            model.addAttribute("emailExistMessage", "This email already exists!");
            return "user/registration-skills-cons";
        }
        if (!userService.ifTwoPasswordsMatch(userDto.getPassword(), userDto.getConfirmPassword())) {
            model.addAttribute("passwordsDoNotMatch", "Passwords do not match!");
            return "user/registration-skills-cons";
        }
        if (bindingResult.hasErrors()) {
            return "user/registration-skills-cons";
        }
        User user = userMapper.toEntity(userDto);
        userRepository.save(user);
        model.addAttribute("user", user);
        return "redirect:/login";

    }

    @GetMapping("/registration/choose-cons")
    public String chooseCons(@ModelAttribute UserDTO userDTO, @RequestParam("id") Long id,Model model) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = userMapper.toEntity(userDTO);
            userRepository.save(user);
            model.addAttribute("user", user);
            model.addAttribute("allSkills", userService.doNotChosenSkill(userDTO));
            return "user/registration-skills-cons";
        } else {
            return "id could not be find";
        }
    }

    @PostMapping("/registration/submit")
    public String submitUser(@Valid @ModelAttribute UserDTO userDto, BindingResult bindingResult, Model model) {
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
    @PostMapping("/send-friend-request")
    public String sendFriendRequest(@RequestParam(name = "eventId") Integer eventId, @RequestParam(name = "userId") Long userID, Model model){
        return userService.sendFriendRequest(eventId, userID, model);
    }
    @GetMapping("/all-friend-requests")
    public String answerFriendRequest(Model model){
        return userService.showAllRequests(model);
    }
    @PostMapping("/accept-request")
    public String acceptFriendRequest(@RequestParam(name = "friend_id") Long friendId){
        return userService.acceptRequest(friendId);
    }
    @PostMapping("/delete-request")
    public String deleteFriendRequest(@RequestParam(name = "friend_id") Long friendId){
        return userService.deleteRequest(friendId);
    }
    @GetMapping("/all-friends")
    public String showAllFriends(Model model){
        return userService.showAllFriends(model);
    }
}

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String userSubmit(@Valid @ModelAttribute UserDTO userDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
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
        redirectAttributes.addFlashAttribute("user_id", user.getId());
        return "redirect:/registration/choose-pros";
    }


    @GetMapping("/registration/choose-pros")
    public String choosePros(@ModelAttribute("user_id") Long id, Model model) {
        model.addAttribute("userDto", new UserDTO());
        model.addAttribute("userId", id);
        model.addAttribute("allSkills", skillRepository.findAll());
        return "user/registration-skills-pros";
    }

    @PostMapping("/registration/choose-pros")
    public String postChoosePros(@ModelAttribute UserDTO userDto, BindingResult bindingResult, @RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(id).get();
        user.setSkillsPros(userDto.getSkillsPros());
        user.setSkillsCons(userService.makingNewListWithoutChosenOnes(user));
        userRepository.save(user);
        return "redirect:/login";

    }
    @PostMapping("/send-friend-request")
    public String sendFriendRequest(@RequestParam(name = "eventId") Integer eventId, @RequestParam(name = "userId") Long userID, Model model) {
        return userService.sendFriendRequest(eventId, userID, model);
    }

    @GetMapping("/all-friend-requests")
    public String answerFriendRequest(Model model) {
        return userService.showAllRequests(model);
    }

    @PostMapping("/accept-request")
    public String acceptFriendRequest(@RequestParam(name = "friend_id") Long friendId) {
        return userService.acceptRequest(friendId);
    }

    @PostMapping("/delete-request")
    public String deleteFriendRequest(@RequestParam(name = "friend_id") Long friendId) {
        return userService.deleteRequest(friendId);
    }

    @GetMapping("/all-friends")
    public String showAllFriends(Model model) {
        return userService.showAllFriends(model);
    }
}

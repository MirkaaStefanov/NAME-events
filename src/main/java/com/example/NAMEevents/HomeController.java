package com.example.NAMEevents;

import com.example.NAMEevents.User.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @GetMapping("/")
    public String home() {
        return "index";
    }
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }
    @GetMapping("/profile")
    public String profile(){return "user/profile";}

}

package com.example.NAMEevents;

import com.example.NAMEevents.Event.Event;
import com.example.NAMEevents.Event.EventRepository;
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
    EventRepository eventRepository;
    @GetMapping("/")
    public String home(Model model) {
        Iterable<Event> allEvents = eventRepository.findAll();
        model.addAttribute("allEvents", allEvents);
        return "event/all-events";
    }
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }
    @GetMapping("/profile")
    public String profile(){return "user/profile";}

}

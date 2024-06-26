package com.example.NAMEevents.Event;


import com.example.NAMEevents.User.User;
import com.example.NAMEevents.User.UserRepository;
import com.example.NAMEevents.User.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.text.ParseException;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/event")
public class EventController {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventService eventService;
    @Autowired
    EventMapper eventMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @GetMapping("/add")
    public String addEvent(Model model) {
        model.addAttribute("eventDTO", new EventDTO());
        return "event/event-form";
    }

    @PostMapping("/submit")
    public String postEvent(@Valid @ModelAttribute EventDTO eventDTO, BindingResult bindingResult, Model model) throws ParseException {
        if (bindingResult.hasErrors()) {
            return "event/event-form";
        }
        if (eventService.errorEventStatus(eventDTO)) {
            model.addAttribute("notValidDate", "Please enter a valid date!");
            return "event/event-form";
        } else {
            Event event = eventMapper.toEntity(eventDTO);
            eventRepository.save(event);
            model.addAttribute("event", event);
            return "redirect:/";
        }
    }


    @GetMapping("/show-event")
    public String getEventDetails(@RequestParam(name = "eventId") Integer eventId, Model model, @ModelAttribute("message")String message) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            List<User> usersWithPros = eventService.findSuggestedUsers(eventId);
            model.addAttribute("usersWithPros", usersWithPros);
            model.addAttribute("event", event);
            model.addAttribute("message", message);
            return "event/event-details";
        } else {
            return "id could not be find";
        }
    }

    @GetMapping("/search")
    public String searchEvents(@RequestParam(name = "name", required = false) String name,
                               @RequestParam(name = "place", required = false) String place,
                               @RequestParam(name = "date", required = false) String date,
                               Model model) {
        return eventService.searchEvents(name, place, date,model);
    }

    @GetMapping("/update")
    public String updateProductForm(@RequestParam(name = "id") Integer id, Model model) {
        return eventService.updateForm(id, model);
    }

    @PostMapping("/update")
    public String postUpdatedProduct(@RequestParam(name = "eventId") Integer id, @Valid @ModelAttribute EventDTO eventDTO, BindingResult bindingResult) {
        return eventService.postUpdate(id, eventDTO, bindingResult);
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Integer id, Model model) {
        return eventService.delete(id, model);
    }


    @PostMapping("/apply")
    public String apply(@RequestParam(name = "eventId") Integer id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.getUserByUsername(username);
        Event event = eventRepository.findById(id).get();
        List<Event> events = user.getEvents();
        for (Event listEvent : events) {
            if (listEvent.equals(event)) {
                model.addAttribute("message", "You have already applied for this event!");
                model.addAttribute("event", event);
                return "event/event-details";
            }
        }
        event.getUsers().add(user);
        user.getEvents().add(event);
        userRepository.save(user);
        eventRepository.save(event);

        model.addAttribute("event", event);
        model.addAttribute("message", "You have successfully applied for the event!");
        return "event/event-details";
    }
    @PostMapping("/mark-presence")
    public String markPresence(@RequestParam(name = "eventId") Integer event_id, Model model){
        return eventService.markPresent(event_id, model);
    }
}


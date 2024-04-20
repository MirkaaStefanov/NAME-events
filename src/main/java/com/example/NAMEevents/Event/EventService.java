package com.example.NAMEevents.Event;

import com.example.NAMEevents.Skill.Skill;
import com.example.NAMEevents.User.User;
import com.example.NAMEevents.User.UserRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    UserRepository userRepository;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate localDate = LocalDate.now();

    public String updateForm(Integer id, Model model) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            Event event = eventRepository.findById(id).get();
            model.addAttribute("updateEvent", event);
            return "event/event-update-form";
        } else {
            return "id could not be find";
        }
    }

    public String postUpdate(Integer id, Event updatedEvent, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "event-update-form";
        } else {
            Event event = eventRepository.findById(id).get();
            getEvent(event, updatedEvent);
            eventRepository.save(event);
            model.addAttribute("event", event);
            return "event-update-result";
        }
    }

    private Event getEvent(Event event, Event updatedEvent) {
        event.setName(updatedEvent.getName());
        event.setDate(updatedEvent.getDate());
        event.setDuration(updatedEvent.getDuration());
        event.setDescription(updatedEvent.getDescription());
        event.setPlace(updatedEvent.getPlace());
        event.setCapacity(updatedEvent.getCapacity());
        event.setEventStatus(updatedEvent.getEventStatus());
        return event;
    }

    public String delete(Integer id, Model model) {
        Event event = eventRepository.findById(id).get();
        eventRepository.delete(event);
        model.addAttribute("event", event);
        return "event-delete";
    }

    public String searchEvents(String name,
                               String place,
                               String date,
                               Double minPrice,
                               Double maxPrice,
                               Model model) {

        if (place == null) {
            place = "";
        }
        if (date == null) {
            date = "";
        }

        if (minPrice == null) {
            minPrice = (double) 0;
        }
        if (maxPrice == null) {
            maxPrice = (double) Integer.MAX_VALUE;
        }
        if (minPrice > maxPrice) {
            double maxPrice1 = maxPrice;
            maxPrice = minPrice;
            minPrice = maxPrice1;
        }

        model.addAttribute("allEvents", eventRepository.findByPlaceTypeDateAndPrice(name, place, date, minPrice, maxPrice));
        return "event/all-events";
    }


    public boolean errorEventStatus(EventDTO eventDTO) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(eventDTO.getDate());
            Date local = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (date.before(local)) {
                return true;
            }
        } catch (ParseException ex) {
            System.out.println("Parsing error!" + ex);
        }
        return false;
    }

    public List<User> findSuggestedUsers(Event event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.getUserByUsername(username);
        List<Skill> skillConsList = user.getSkillsCons();

        List<User> usersInEvent = event.getUsers();

        List<User> usersWithPros = new ArrayList<>();

        for (User oneUser : userRepository.findAll()) {
            if (usersInEvent.contains(oneUser)) {
                List<Skill> oneUserSkills = oneUser.getSkillsPros();
                for (Skill authenticatedUserCon:skillConsList) {
                    if(oneUserSkills.contains(authenticatedUserCon)){
                        usersWithPros.add(oneUser);
                    }
                }
            }
        }
        return usersWithPros;
    }

    public String sendFriendRequest(@RequestParam(name = "eventId") Integer eventId,
                                    @RequestParam(name = "userId") Long userId,
                                    Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.getUserByUsername(username);
        Event event = eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            return "event/event-details";
        }

        User friend = userRepository.findById(userId).orElse(null);

        if (friend == null) {
            return "event/event-details";
        }

        List<User> userFriendRequests = user.getFriendRequests();
        for (User friendRequest : userFriendRequests) {
            if (friendRequest.getId().equals(userId)) {
                model.addAttribute("alreadySent", "You have already sent a friend request to this user!");
                model.addAttribute("event", event);
                return "event/event-details";
            }
        }
        user.getFriendRequests().add(friend);
        userRepository.save(user);

        model.addAttribute("suggestedUser", friend);
        model.addAttribute("event", event);
        model.addAttribute("successfullySent", "You have successfully sent a friend request to this user!");
        return "event/event-details";
    }

}
package com.example.NAMEevents.User;

import com.example.NAMEevents.Event.Event;
import com.example.NAMEevents.Event.EventRepository;
import com.example.NAMEevents.Message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EventRepository eventRepository;
    public boolean ifTwoPasswordsMatch(String pass1, String pass2){
        return pass1.equals(pass2);
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

        model.addAttribute("event", event);
        model.addAttribute("successfullySent", "You have successfully sent a friend request to this user!");
        return "event/event-details";
    }
    public String answerRequest(@RequestParam(name = "receiverId") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User sender = userRepository.getUserByUsername(username);
        User receiver = userRepository.findById(id).get();

        message.setSender(sender);
        message.setReceiver(receiver);

        return "redirect:/message/show?receiverId=" + id;
    }


}

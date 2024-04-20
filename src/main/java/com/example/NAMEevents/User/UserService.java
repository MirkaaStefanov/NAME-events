package com.example.NAMEevents.User;

import com.example.NAMEevents.Event.Event;
import com.example.NAMEevents.Event.EventRepository;
import com.example.NAMEevents.Message.Message;
import com.example.NAMEevents.Skill.Skill;
import com.example.NAMEevents.Skill.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    SkillRepository skillRepository;

    public boolean ifTwoPasswordsMatch(String pass1, String pass2) {
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
        friend.getFriendRequests().add(user);
        userRepository.save(user);

        model.addAttribute("event", event);
        model.addAttribute("successfullySent", "You have successfully sent a friend request to this user!");
        return "event/event-details";
    }

    public String showAllRequests(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.getUserByUsername(username);
        model.addAttribute("requests", user.getFriendRequests());
        return "/user/show-requests";
    }

    public String acceptRequest(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.getUserByUsername(username);
        User friend = userRepository.findById(id).get();
        user.getFriends().add(friend);
        friend.getFriends().add(user);
        user.getFriendRequests().remove(friend);
        userRepository.save(user);
        userRepository.save(friend);

        return "redirect:/all-friend-requests";
    }

    public String deleteRequest(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.getUserByUsername(username);
        User requestBy = userRepository.findById(id).get();
        user.getFriendRequests().remove(requestBy);
        userRepository.save(user);
        return "redirect:/all-friend-requests";
    }

    public String showAllFriends(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.getUserByUsername(username);
        model.addAttribute("allFriends", user.getFriends());
        return "user/all-friends";
    }

    public List<Skill> makingNewListWithoutChosenOnes(UserDTO userDTO) {
        List<Skill> allSkills = (List<Skill>) skillRepository.findAll();
        ArrayList<Skill> chosenPros = (ArrayList<Skill>) userDTO.getSkillsPros();
        List<Skill> newList=(List<Skill>) skillRepository.findAll();
        for (Skill skill : allSkills)
            for (int i = 0; i < allSkills.size(); i++) {
                if (skill.getSkillName().equals(chosenPros.get(i))) {
                    newList.remove(i);
                }
            }
        return  newList;
    }
}

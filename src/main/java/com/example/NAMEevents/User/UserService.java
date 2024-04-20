package com.example.NAMEevents.User;

import com.example.NAMEevents.Event.Event;
import com.example.NAMEevents.Event.EventRepository;
import com.example.NAMEevents.Skill.Skill;
import com.example.NAMEevents.Skill.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    SkillRepository skillRepository;
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


    public List<Skill> doNotChosenSkill(UserDTO userDTO){
        Iterable<Skill> allSkills=skillRepository.findAll();
        List<Skill> userPros=userDTO.getSkillsPros();
        ArrayList<Skill> skillListWithoutChosenOnes= (ArrayList<Skill>) skillRepository.findAll();
        for (Skill skill : allSkills){
            for (int i = 0; i< userPros.size(); i++) {
                if(skill.getSkillName().equals(userPros.get(i))){
                    skillListWithoutChosenOnes.remove(i);
                }
            }
        }
        return skillListWithoutChosenOnes;
    }
}

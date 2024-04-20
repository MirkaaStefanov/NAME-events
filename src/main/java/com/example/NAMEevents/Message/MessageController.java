package com.example.NAMEevents.Message;

import com.example.NAMEevents.User.User;
import com.example.NAMEevents.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/send")
    public String sendMessage(@RequestParam(name = "receiverId") Long id, @RequestParam("text") String text) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User sender = userRepository.getUserByUsername(username);

        User receiver = userRepository.findById(id).get();

        Message message = new Message();

        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessage(text);

        messageRepository.save(message);

        return "redirect:/message/show?receiverId=" + id;

    }

    @GetMapping("/show")
    public String showMessage(@RequestParam(name = "receiverId") Long userId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User sender = userRepository.getUserByUsername(username);

        User receiver = userRepository.findById(userId).orElse(null);

        if (receiver == null) {
            // Handle the case when the receiver is not found
            return "redirect:/error"; // Redirect to an error page or handle it accordingly
        }

        List<Message> messagesWithUser = messageRepository.findBySenderAndReceiver(sender, receiver);

        model.addAttribute("messages", messagesWithUser);
        model.addAttribute("receiver", receiver);

        return "message/message";
    }

    @GetMapping("/showUsersForChat")
    public String showUsersForChat(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.getUserByUsername(username);


        List<User> usersForChat = userRepository.findUsersForChat(user.getId());

        model.addAttribute("users", usersForChat);

        return "message/users";
    }

}

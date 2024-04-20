package com.example.NAMEevents.Message;

import com.example.NAMEevents.User.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {

    List<Message> findBySenderAndReceiver(User sender, User receiver);
}

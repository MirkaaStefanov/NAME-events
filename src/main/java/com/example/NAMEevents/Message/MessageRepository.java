package com.example.NAMEevents.Message;

import com.example.NAMEevents.User.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {

    @Query("SELECT m FROM Message m WHERE m.sender = :sender AND m.receiver = :receiver OR m.sender = :receiver AND m.receiver = :sender")
    List<Message> findBySenderAndReceiver(@Param("sender") User sender, @Param("receiver") User receiver);

}

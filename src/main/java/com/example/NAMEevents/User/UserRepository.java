package com.example.NAMEevents.User;

import com.example.NAMEevents.Message.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    public User getUserByUsername(@Param("username") String username);
    public User getUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.id != :userId ")
    List<User> findUsersForChat(@Param("userId") Long userId);

}


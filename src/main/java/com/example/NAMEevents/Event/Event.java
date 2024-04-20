package com.example.NAMEevents.Event;

import com.example.NAMEevents.EventStatus.EventStatus;
import com.example.NAMEevents.User.User;;
import jakarta.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String date;
    private int duration;
    private String description;
    private String place;
    private String time;
    private int capacity;
    private double ticketPrice;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;
    @ManyToMany
    @JoinTable(
    name = "events_users",
    joinColumns = @JoinColumn(name = "event_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User>users;
    @ManyToMany
    @JoinTable(
            name = "events_users_present",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User>usersPresent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public EventStatus getEventStatus() {
        LocalDate localDate=LocalDate.now();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date eventDate = dateFormat.parse(date);
            Date local = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (eventDate.before(local)) {
                eventStatus=EventStatus.FINISHED;
            }
            else {
                eventStatus=EventStatus.AVAILABLE;
            }
        }catch (ParseException ex){
            System.out.println("Parsing error!" + ex);
        }
        if (users.size()>=capacity){
            eventStatus=EventStatus.FULL;
        }
        return this.eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getUsersPresent() {
        return usersPresent;
    }

    public void setUsersPresent(List<User> usersPresent) {
        this.usersPresent = usersPresent;
    }
}

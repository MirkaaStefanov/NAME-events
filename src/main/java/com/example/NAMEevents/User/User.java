package com.example.NAMEevents.User;

import com.example.NAMEevents.Event.Event;
import com.example.NAMEevents.Skill.Skill;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String password;
    private String role;
    @ManyToMany(mappedBy = "users")
    private List<Event> events;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "skill_user_pros",
            joinColumns = @JoinColumn(name = "skill_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<Skill> skillsPros;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "skill_user_cons",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skillsCons;
    private String graduationPlace;
    private String job;
    private String userDescription;
    private List<User>friendRequests;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    private boolean enabled=true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Skill> getSkillsPros() {
        return skillsPros;
    }

    public void setSkillsPros(List<Skill> skillsPros) {
        this.skillsPros = skillsPros;
    }

    public List<Skill> getSkillsCons() {
        return skillsCons;
    }

    public void setSkillsCons(List<Skill> skillsCons) {
        this.skillsCons = skillsCons;
    }

    public String getGraduationPlace() {
        return graduationPlace;
    }

    public void setGraduationPlace(String graduationPlace) {
        this.graduationPlace = graduationPlace;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public List<User> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<User> friendRequests) {
        this.friendRequests = friendRequests;
    }
}

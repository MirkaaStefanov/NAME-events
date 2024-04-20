package com.example.NAMEevents.Event;

import com.example.NAMEevents.EventStatus.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class EventMapper {
    public Event toEntity(EventDTO eventDTO) {
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDate(eventDTO.getDate());
        event.setDuration(eventDTO.getDuration());
        event.setDescription(eventDTO.getDescription());
        event.setPlace(eventDTO.getPlace());
        event.setTime(eventDTO.getTime());
        event.setTicketPrice(eventDTO.getTicketPrice());
        event.setCapacity(eventDTO.getCapacity());
        try {
            event.setImage(Base64.getEncoder().encodeToString(eventDTO.getFile().getBytes()));
        } catch (Exception e) {
            System.out.println(e);
        }

        event.setEventStatus(EventStatus.AVAILABLE);
        return event;
    }

    public EventDTO toDto(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setName(event.getName());
        eventDTO.setDate(event.getDate());
        eventDTO.setDuration(event.getDuration());
        eventDTO.setDescription(eventDTO.getDescription());
        eventDTO.setPlace(event.getPlace());
        eventDTO.setTime(event.getTime());
        eventDTO.setTicketPrice(event.getTicketPrice());
        eventDTO.setCapacity(event.getCapacity());
        return eventDTO;
    }
}

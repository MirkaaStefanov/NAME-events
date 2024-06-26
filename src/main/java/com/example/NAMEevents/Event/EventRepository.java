package com.example.NAMEevents.Event;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface EventRepository extends CrudRepository<Event, Integer> {
    @Query("SELECT e FROM Event e " +
            "WHERE (:name IS NULL OR e.name LIKE %:name%) " +
            "AND (:place IS NULL OR e.place LIKE %:place%) " +
            "AND (:date IS NULL OR CAST(e.date AS string) LIKE %:date%) ")
    List<Event> findByPlaceTypeDateAndPrice(@Param("name") String name,
                                            @Param("place") String place,
                                            @Param("date") String date
    );
}

package com.hackathon.netplatform.repository;

import com.hackathon.netplatform.model.Event;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.hackathon.netplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
  @Query("SELECT e FROM Event e JOIN e.interests i WHERE i.id IN :interests")
  List<Event> findByInterestIds(@Param("interests") List<UUID> interestsIDs);

  @Query("SELECT e.visitors FROM Event e WHERE e.id = :eventId")
  Set<User> findVisitorsByEventId(@Param("eventId") UUID eventId);
}

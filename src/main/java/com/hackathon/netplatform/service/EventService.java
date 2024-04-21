package com.hackathon.netplatform.service;

import com.hackathon.netplatform.dto.request.EventRequestDto;
import com.hackathon.netplatform.dto.request.InterestsIdsRequest;
import com.hackathon.netplatform.dto.response.EventInterestsResponse;
import com.hackathon.netplatform.dto.response.EventResponseDto;
import com.hackathon.netplatform.dto.response.EventVisitorsResponse;
import com.hackathon.netplatform.dto.response.UserResponseDto;
import com.hackathon.netplatform.model.Event;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface EventService {
  EventResponseDto createEvent(EventRequestDto eventRequestDto);

  EventResponseDto getEvent(UUID id);

  List<UserResponseDto> getUsersByEvent(UUID id);
  List<Event> getAllEventsEntity();
  List<EventResponseDto> getAllEvents();

  void deleteEvent(UUID id);

  Page<EventResponseDto> getAllEventsByPagination(int offset,int pageSize);

  List<EventInterestsResponse> getEventsByInterests(InterestsIdsRequest interests);

  Event getEventEntity(UUID eventId);

  EventVisitorsResponse addUserToEvent(UUID eventId, UUID userId);
  void deleteInterestById(UUID id);

  EventVisitorsResponse removeUserFromEvent(UUID eventId, UUID userId);
}

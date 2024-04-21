package com.hackathon.netplatform.service.impl;

import com.hackathon.netplatform.dto.request.EventRequestDto;
import com.hackathon.netplatform.dto.request.InterestsIdsRequest;
import com.hackathon.netplatform.dto.response.EventInterestsResponse;
import com.hackathon.netplatform.dto.response.EventResponseDto;
import com.hackathon.netplatform.dto.response.EventVisitorsResponse;
import com.hackathon.netplatform.dto.response.UserResponseDto;
import com.hackathon.netplatform.exception.EventNotFoundException;
import com.hackathon.netplatform.exception.UserAlreadyParticipatingException;
import com.hackathon.netplatform.exception.UserNotParticipatingException;
import com.hackathon.netplatform.model.Event;
import com.hackathon.netplatform.model.Interest;
import com.hackathon.netplatform.model.User;
import com.hackathon.netplatform.repository.EventRepository;
import com.hackathon.netplatform.service.EventService;
import com.hackathon.netplatform.service.InterestService;
import com.hackathon.netplatform.service.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

  private final EventRepository eventRepository;
  private final UserService userService;
  private final InterestService interestService;
  private final ModelMapper modelMapper;

  @Override
  public EventResponseDto createEvent(EventRequestDto eventRequestDto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserEmail = authentication.getName();

    User creator = userService.getUserByEmail(currentUserEmail);
    Event event = setEventFields(eventRequestDto, creator);

    eventRepository.save(event);
    return modelMapper.map(event, EventResponseDto.class);
  }

  @Override
  public EventResponseDto getEvent(UUID id) {
    return modelMapper.map(getEvent(id), EventResponseDto.class);
  }

  @Override
  public List<UserResponseDto> getUsersByEvent(UUID eventId) {
    return eventRepository.findVisitorsByEventId(eventId).stream()
        .map(user -> modelMapper.map(user, UserResponseDto.class))
        .toList();
  }

  @Override
  public List<EventResponseDto> getAllEvents() {
    return eventRepository.findAll().stream()
        .map(event -> modelMapper.map(event, EventResponseDto.class))
        .toList();
  }

  @Override
  public void deleteEvent(UUID id) {
    Event event = getEventEntity(id);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserEmail = authentication.getName();

    if (currentUserEmail.equals(event.getCreator().getEmail())) {
      eventRepository.delete(event);
    }
  }

  @Override
  public Page<EventResponseDto> getAllEventsByPagination(int offset, int pageSize) {
    Page<Event> eventPage = eventRepository.findAll(PageRequest.of(offset, pageSize));

    List<EventResponseDto> eventResponseList =
        eventPage.getContent().stream()
            .map(event -> modelMapper.map(event, EventResponseDto.class))
            .toList();

    return new PageImpl<>(eventResponseList, eventPage.getPageable(), eventPage.getTotalElements());
  }

  @Override
  public List<EventInterestsResponse> getEventsByInterests(
      InterestsIdsRequest interestsIdsRequest) {
    List<UUID> interestsIds = interestsIdsRequest.getInterestsIds();
    return eventRepository.findByInterestIds(interestsIds).stream()
        .map(event -> modelMapper.map(event, EventInterestsResponse.class))
        .toList();
  }

  @Override
  public Event getEventEntity(UUID eventId) {
    return eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
  }

  @Override
  public EventVisitorsResponse addUserToEvent(UUID eventId, UUID userId) {
    Event event = getEventEntity(eventId);
    User user = userService.getUserById(userId);
    if (event.getVisitors().contains(user)) {
      throw new UserAlreadyParticipatingException(eventId, userId);
    }
    event.getVisitors().add(user);

    return modelMapper.map(eventRepository.save(event), EventVisitorsResponse.class);
  }

  @Override
  public EventVisitorsResponse removeUserFromEvent(UUID eventId, UUID userId) {
    Event event = getEventEntity(eventId);
    User user = userService.getUserById(userId);

    if (!event.getVisitors().contains(user)) {
      throw new UserNotParticipatingException(eventId, userId);
    }
    event.getVisitors().remove(user);

    return modelMapper.map(eventRepository.save(event), EventVisitorsResponse.class);
  }

  private Event setEventFields(EventRequestDto eventRequestDto, User creator) {
    Event event = new Event();
    event.setCreator(creator);
    event.setTitle(eventRequestDto.getTitle());
    event.setDescription(eventRequestDto.getDescription());
    event.setDate(eventRequestDto.getDate());
    event.setLocation(eventRequestDto.getLocation());

    Set<Interest> interests = getInterests(eventRequestDto);
    event.setInterests(interests);
    return event;
  }

  private Set<Interest> getInterests(EventRequestDto eventRequestDto) {
    Set<Interest> interests = new HashSet<>();
    for (String interest : eventRequestDto.getInterests()) {
      Interest current = interestService.getInterest(interest);
      interests.add(current);
    }
    return interests;
  }
}

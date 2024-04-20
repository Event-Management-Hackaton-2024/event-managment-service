package com.hackathon.netplatform.service.impl;

import com.hackathon.netplatform.dto.request.EventRequestDto;
import com.hackathon.netplatform.dto.response.EventResponseDto;
import com.hackathon.netplatform.exception.EventNotFoundException;
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
  public List<EventResponseDto> getAllEvents() {
    return eventRepository.findAll().stream()
        .map(event -> modelMapper.map(event, EventResponseDto.class))
        .toList();
  }

  private Event getEventBuId(UUID id) {
    return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
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

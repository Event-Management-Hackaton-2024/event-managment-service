package com.hackathon.netplatform.service;

import com.hackathon.netplatform.dto.request.EventRequestDto;
import com.hackathon.netplatform.dto.response.EventResponseDto;
import com.hackathon.netplatform.model.Event;

import java.util.List;
import java.util.UUID;

public interface EventService {
  EventResponseDto createEvent(EventRequestDto eventRequestDto);

  EventResponseDto getEvent(UUID id);

  List<EventResponseDto> getAllEvents();
  Event getEventEntity(UUID eventId);
}

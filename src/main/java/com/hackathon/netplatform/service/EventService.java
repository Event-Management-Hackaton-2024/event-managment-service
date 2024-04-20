package com.hackathon.netplatform.service;

import com.hackathon.netplatform.dto.request.EventRequestDto;
import com.hackathon.netplatform.dto.response.EventResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventService {
  EventResponseDto createEvent(EventRequestDto eventRequestDto);

  EventResponseDto getEvent(UUID id);

  List<EventResponseDto> getAllEvents();
}

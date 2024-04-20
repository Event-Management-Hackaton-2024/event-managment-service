package com.hackathon.netplatform.service;

import com.hackathon.netplatform.dto.request.EventRequestDto;
import com.hackathon.netplatform.dto.response.EventResponseDto;
import com.hackathon.netplatform.model.Event;

import java.util.UUID;

public interface EventService {
  EventResponseDto createEvent(EventRequestDto eventRequestDto);
  Event getEvent (UUID eventId);
}

package com.hackathon.netplatform.service;

import com.hackathon.netplatform.dto.request.EventRequestDto;
import com.hackathon.netplatform.dto.response.EventResponseDto;

public interface EventService {
  EventResponseDto createEvent(EventRequestDto eventRequestDto);
}

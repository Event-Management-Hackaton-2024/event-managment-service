package com.hackathon.netplatform.controller;

import com.hackathon.netplatform.dto.request.EventRequestDto;
import com.hackathon.netplatform.dto.response.EventResponseDto;
import com.hackathon.netplatform.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

  private final EventService eventService;

  @Operation(summary = "Create event")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public EventResponseDto createEvent(@Valid @RequestBody EventRequestDto eventRequestDto) {
    return eventService.createEvent(eventRequestDto);
  }
}

package com.hackathon.netplatform.controller;

import com.hackathon.netplatform.dto.request.EventRequestDto;
import com.hackathon.netplatform.dto.response.EventResponseDto;
import com.hackathon.netplatform.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
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

  @Operation(summary = "Get event")
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public EventResponseDto getEvent(@PathVariable("id") UUID id) {
    return eventService.getEvent(id);
  }

  @Operation(summary = "Get all events")
  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public List<EventResponseDto> getAllEvents() {
    return eventService.getAllEvents();
  }
}

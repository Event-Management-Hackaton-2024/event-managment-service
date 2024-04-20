package com.hackathon.netplatform.controller;

import com.hackathon.netplatform.dto.request.EventRequestDto;
import com.hackathon.netplatform.dto.request.InterestsIdsRequest;
import com.hackathon.netplatform.dto.response.*;
import com.hackathon.netplatform.service.EventService;
import com.hackathon.netplatform.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

  private final EventService eventService;
  private final ImageService imageService;

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

  @Operation(summary = "Get event users")
  @GetMapping("/{id}/users")
  @ResponseStatus(HttpStatus.OK)
  public List<UserResponseDto> getEventUsers(@PathVariable("id") UUID id) {
    return eventService.getUsersByEvent(id);
  }

  @Operation(summary = "Get all events")
  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public List<EventResponseDto> getAllEvents() {
    return eventService.getAllEvents();
  }

  @Operation(summary = "Get all events by Interests")
  @GetMapping("/interests")
  @ResponseStatus(HttpStatus.OK)
  public List<EventInterestsResponse> getAllEventsByInterest(@RequestBody InterestsIdsRequest interestsIds) {
    return eventService.getEventsByInterests(interestsIds);
  }

  @Operation(summary = "Add a user to event")
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("{eventId}/add/{userId}")
  public EventVisitorsResponse addUserToEvent(@PathVariable UUID eventId, @PathVariable UUID userId) {
    return eventService.addUserToEvent(eventId, userId);
  }
  @Operation(summary = "Remove a user from event")
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("{eventId}/remove/{userId}")
  public EventVisitorsResponse removeUserFromEvent(@PathVariable UUID eventId, @PathVariable UUID userId) {
    return eventService.removeUserFromEvent(eventId, userId);
  }

  @Operation(summary = "Upload new image in file system and attach to event")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = "/{eventId}/picture")
  public ImageResponseDto uploadImage(
      @PathVariable("eventId") @Valid UUID eventId,
      @RequestParam("image") @Valid MultipartFile image)
      throws IOException {
    return imageService.uploadImage(image, null, eventId);
  }

  @Operation(summary = "Get image of event")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{eventId}/picture", produces = "image/png")
  public byte[] getImage(@PathVariable("eventId") @Valid UUID eventId) throws IOException {
    return imageService.downloadImageForEvent(eventId);
  }

  @Operation(summary = "Delete image from file system and detach from user")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{eventId}/picture")
  public void deleteImage(@PathVariable("eventId") @Valid UUID eventId) throws IOException {
    imageService.deleteImageForEvent(eventId);
  }
}

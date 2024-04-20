package com.hackathon.netplatform.controller;

import com.hackathon.netplatform.dto.request.EventRequestDto;
import com.hackathon.netplatform.dto.response.EventResponseDto;
import com.hackathon.netplatform.dto.response.ImageResponseDto;
import com.hackathon.netplatform.service.EventService;
import com.hackathon.netplatform.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

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

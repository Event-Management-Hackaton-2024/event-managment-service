package com.hackathon.netplatform.controller;

import com.hackathon.netplatform.dto.request.InterestRequestDto;
import com.hackathon.netplatform.dto.response.InterestResponseDto;
import com.hackathon.netplatform.service.InterestService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interests")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    @Operation(summary = "Create interest")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createInterest(@Valid @RequestBody InterestRequestDto interestRequestDto) {
        return interestService.createInterest(interestRequestDto);
    }

    @Operation(summary = "Get all interests")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InterestResponseDto> getAllInterests() {
        return interestService.findAllInterests();
    }
}

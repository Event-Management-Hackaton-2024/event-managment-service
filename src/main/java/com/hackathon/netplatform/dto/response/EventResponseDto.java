package com.hackathon.netplatform.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;

@Data
public class EventResponseDto {

    private UUID id;
    private String title;
    private String description;
    private String location;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate date;
}

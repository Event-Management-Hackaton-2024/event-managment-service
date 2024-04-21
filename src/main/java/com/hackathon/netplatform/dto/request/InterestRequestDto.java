package com.hackathon.netplatform.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InterestRequestDto {

    @NotBlank
    private String name;
}

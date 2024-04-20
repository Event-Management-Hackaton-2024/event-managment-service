package com.hackathon.netplatform.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageResponseDto {
  private String type;
  private String filePath;
  private UUID userId;
  private UUID eventId;
}

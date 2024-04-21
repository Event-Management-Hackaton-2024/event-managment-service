package com.hackathon.netplatform.dto.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
public class InterestsIdsRequest {
    private List<UUID> interestsIds;
}

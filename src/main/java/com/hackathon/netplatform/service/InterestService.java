package com.hackathon.netplatform.service;

import com.hackathon.netplatform.dto.request.InterestRequestDto;
import com.hackathon.netplatform.dto.response.InterestResponseDto;
import com.hackathon.netplatform.model.Interest;
import java.util.List;
import java.util.UUID;

public interface InterestService {

  String createInterest(InterestRequestDto interestRequestDto);

  List<InterestResponseDto> findAllInterests();

  Interest getInterestByID(UUID id);

  Interest getInterest(String name);

  void deleteByID(UUID id);
}

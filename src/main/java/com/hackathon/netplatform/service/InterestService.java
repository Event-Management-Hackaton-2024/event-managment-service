package com.hackathon.netplatform.service;

import com.hackathon.netplatform.dto.request.InterestRequestDto;
import com.hackathon.netplatform.dto.response.InterestResponseDto;
import com.hackathon.netplatform.model.Interest;
import java.util.List;

public interface InterestService {

  String createInterest(InterestRequestDto interestRequestDto);

  List<InterestResponseDto> findAllInterests();

  Interest getInterest(String name);
}

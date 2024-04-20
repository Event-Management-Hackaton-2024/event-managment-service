package com.hackathon.netplatform.service.impl;

import com.hackathon.netplatform.dto.request.InterestRequestDto;
import com.hackathon.netplatform.dto.response.InterestResponseDto;
import com.hackathon.netplatform.exception.InterestExistsException;
import com.hackathon.netplatform.exception.InterestNotFoundException;
import com.hackathon.netplatform.model.Interest;
import com.hackathon.netplatform.repository.InterestRepository;
import com.hackathon.netplatform.service.InterestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

  private final InterestRepository interestRepository;
  private final ModelMapper modelMapper;

  @Override
  public String createInterest(InterestRequestDto interestRequestDto) {
    if (interestRepository.existsByName(interestRequestDto.getName())) {
      throw new InterestExistsException(interestRequestDto.getName());
    }

    Interest interest = new Interest();
    interest.setName(interestRequestDto.getName());
    interestRepository.save(interest);
    return interest.getName();
  }

  @Override
  public List<InterestResponseDto> findAllInterests() {
    return interestRepository.findAll().stream()
            .map(interest -> modelMapper.map(interest, InterestResponseDto.class))
            .toList();
  }

  @Override
  public Interest getInterest(String name) {
    return interestRepository.findByName(name).orElseThrow(() ->
            new InterestNotFoundException(name));
  }

}

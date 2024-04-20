package com.hackathon.netplatform.service;

import com.hackathon.netplatform.dto.request.AuthRequestDto;
import com.hackathon.netplatform.dto.request.UserRequestDto;
import com.hackathon.netplatform.dto.response.AuthResponseDto;
import com.hackathon.netplatform.dto.response.UserResponseDto;

public interface AuthService {
    AuthResponseDto authenticate(AuthRequestDto authRequestDto);

    UserResponseDto registerUser(UserRequestDto userRequestDto);


}

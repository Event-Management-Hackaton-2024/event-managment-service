package com.hackathon.netplatform.dto.request;

import com.hackathon.netplatform.model.Interest;
import com.hackathon.netplatform.model.User;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class EditUserRequestDto {

  private UUID id;
  private String username;
  private String firstName;
  private String lastName;
  private String password;
  private String phoneNumber;
  private List<UUID> followers;
  private List<String> skills;
  private List<String> interests;
}

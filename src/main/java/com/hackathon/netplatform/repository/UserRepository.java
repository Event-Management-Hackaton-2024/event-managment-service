package com.hackathon.netplatform.repository;

import com.hackathon.netplatform.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, UUID> {

  @Query("SELECT u FROM User u JOIN u.interests i WHERE i.id IN :interestIds")
  List<User> findByInterestIds(@Param("interestIds") List<UUID> interestIds);
  @Query("SELECT u FROM User u JOIN u.skills i WHERE i.id IN :interestIds")
  List<User> findByInterestIdsSkills(@Param("interestIds") List<UUID> interestIds);

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  void deleteByEmail(String email);
}

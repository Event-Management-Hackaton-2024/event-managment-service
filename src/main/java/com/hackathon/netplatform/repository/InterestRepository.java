package com.hackathon.netplatform.repository;

import com.hackathon.netplatform.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InterestRepository extends JpaRepository<Interest, UUID> {

    Optional<Interest> findByName(String name);

    boolean existsByName(String name);
}

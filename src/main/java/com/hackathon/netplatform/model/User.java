package com.hackathon.netplatform.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private String username;
  private String firstName;
  private String lastName;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  private String phoneNumber;

  @ManyToMany
  @JoinTable(
      name = "users_followers",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "follower_id"))
  private Set<User> followers;

  @ManyToMany
  @JoinTable(
      name = "users_skills",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "interest_id", referencedColumnName = "id"))
  private List<Interest> skills;

  @ManyToMany
  @JoinTable(
      name = "users_interests_searched",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "interest_id", referencedColumnName = "id"))
  private List<Interest> interests;

  @OneToOne
  @JoinColumn(name = "image_id")
  private Image image;

  private boolean isEventCreator;

  @ManyToMany
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Set<Role> roles;
}

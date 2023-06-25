package com.mukund.mvcjwt.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mukund.mvcjwt.dto.AuthUserDTO;
import com.mukund.mvcjwt.entity.AuthUser;

public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {

    public Optional<AuthUser> findByUsername(String username);

    public Optional<AuthUser> findById(UUID id);

    @Query("SELECT NEW com.mukund.mvcjwt.dto.AuthUserDTO(u.id,u.name) FROM AuthUser u")
    public List<AuthUserDTO> findAllUsers();

}
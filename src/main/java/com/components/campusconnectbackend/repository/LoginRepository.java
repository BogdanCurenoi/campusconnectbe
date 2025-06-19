package com.components.campusconnectbackend.repository;

import com.components.campusconnectbackend.domain.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, String> {
    Optional<Login> findByUid(String uid);
    boolean existsByUid(String uid);
    boolean existsByUsername(String username);
}
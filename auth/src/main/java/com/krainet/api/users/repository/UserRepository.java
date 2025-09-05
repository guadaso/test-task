package com.krainet.api.users.repository;

import com.krainet.api.users.entity.Users;
import com.krainet.common.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUserName(String username);

    Optional<Users> findByEmail(String email);

    List<Users> findByRole(UserRole role);
}

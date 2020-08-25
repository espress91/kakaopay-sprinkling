package com.github.espress91.kakaopaysprinkling.repository;

import com.github.espress91.kakaopaysprinkling.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

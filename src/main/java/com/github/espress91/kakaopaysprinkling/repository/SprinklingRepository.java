package com.github.espress91.kakaopaysprinkling.repository;

import com.github.espress91.kakaopaysprinkling.data.model.Sprinkling;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SprinklingRepository extends JpaRepository<Sprinkling, Long> {
    Optional<Sprinkling> findById(Long sprinklingId);
    Optional<Sprinkling> findByToken(String token);
}

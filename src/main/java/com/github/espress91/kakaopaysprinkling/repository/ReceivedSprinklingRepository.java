package com.github.espress91.kakaopaysprinkling.repository;

import com.github.espress91.kakaopaysprinkling.data.model.ReceivedSprinkling;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceivedSprinklingRepository extends JpaRepository<ReceivedSprinkling, Long> {
}

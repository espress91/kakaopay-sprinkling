package com.github.espress91.kakaopaysprinkling.repository;

import com.github.espress91.kakaopaysprinkling.data.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

}

package com.github.espress91.kakaopaysprinkling.service;

import com.github.espress91.kakaopaysprinkling.data.model.Room;
import com.github.espress91.kakaopaysprinkling.data.model.User;
import com.github.espress91.kakaopaysprinkling.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Optional<Room> getRoom(Long roomId) {
        return roomRepository.findById(roomId);
    }

    @Transactional
    public Room createRoom(User user) {
        Room room = new Room();
        room.setParticipants(1);
        return roomRepository.save(room);
    }

    @Transactional
    public Room enterRoom(User user, Room room) {
        room.setParticipants(room.getParticipants() + 1);
        return roomRepository.save(room);
    }

}

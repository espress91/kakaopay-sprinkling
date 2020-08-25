package com.github.espress91.kakaopaysprinkling.data.dto;

import com.github.espress91.kakaopaysprinkling.data.model.Room;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class RoomDto {
    private Long id;

    private int participants; //참여자 수

    public Room toEntity() {
        Room room = new Room();

        room.setParticipants(participants);

        return room;
    }

    public static RoomDto convertToDto(Room room) {
        RoomDto roomDto = new RoomDto();

        roomDto.setId(room.getId());
        roomDto.setParticipants(room.getParticipants());

        return roomDto;
    }
}

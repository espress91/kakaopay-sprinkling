package com.github.espress91.kakaopaysprinkling.controller;

import com.github.espress91.kakaopaysprinkling.data.dto.RoomDto;
import com.github.espress91.kakaopaysprinkling.data.dto.response.ApiResult;
import com.github.espress91.kakaopaysprinkling.data.model.Room;
import com.github.espress91.kakaopaysprinkling.data.model.User;
import com.github.espress91.kakaopaysprinkling.error.NotFoundException;
import com.github.espress91.kakaopaysprinkling.service.RoomService;
import com.github.espress91.kakaopaysprinkling.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.github.espress91.kakaopaysprinkling.data.dto.response.ApiResult.OK;

@Slf4j
@RestController
@RequestMapping("room")
public class RoomRestController {
    private final UserService userService;
    private final RoomService roomService;

    public RoomRestController(UserService userService, RoomService roomService) {
        this.userService = userService;
        this.roomService = roomService;
    }

    @PostMapping
    public ApiResult<RoomDto> createRoom(@RequestHeader("X-USER-ID") Long userId) {
        return OK(RoomDto.convertToDto(roomService.createRoom(
                userService.getUser(userId).orElseThrow(() -> new NotFoundException(User.class, userId))
        )));
    }

    @PutMapping
    public ApiResult<RoomDto> enterRoom(@RequestHeader("X-USER-ID") Long userId,
                                        @RequestHeader("X-ROOM-ID") Long roomId) {
        return OK(RoomDto.convertToDto(roomService.enterRoom(
                userService.getUser(userId).orElseThrow(() -> new NotFoundException(User.class, userId)),
                roomService.getRoom(roomId).orElseThrow(() -> new NotFoundException(Room.class, roomId))
        )));
    }
}

package com.github.espress91.kakaopaysprinkling.controller;

import com.github.espress91.kakaopaysprinkling.data.dto.ReceivedSprinklingDto;
import com.github.espress91.kakaopaysprinkling.data.dto.SprinklingDto;
import com.github.espress91.kakaopaysprinkling.data.dto.response.ApiResult;
import com.github.espress91.kakaopaysprinkling.data.model.ReceivedSprinkling;
import com.github.espress91.kakaopaysprinkling.data.model.Room;
import com.github.espress91.kakaopaysprinkling.data.model.Sprinkling;
import com.github.espress91.kakaopaysprinkling.data.model.User;
import com.github.espress91.kakaopaysprinkling.error.NotFoundException;
import com.github.espress91.kakaopaysprinkling.service.ReceivedSprinklingService;
import com.github.espress91.kakaopaysprinkling.service.RoomService;
import com.github.espress91.kakaopaysprinkling.service.SprinklingService;
import com.github.espress91.kakaopaysprinkling.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.github.espress91.kakaopaysprinkling.data.dto.response.ApiResult.ERROR;
import static com.github.espress91.kakaopaysprinkling.data.dto.response.ApiResult.OK;

@Slf4j
@RestController
@RequestMapping("sprinkling")
public class SprinklingRestController {
    private final SprinklingService sprinklingService;
    private final ReceivedSprinklingService receivedSprinklingService;
    private final UserService userService;
    private final RoomService roomService;

    public SprinklingRestController(SprinklingService sprinklingService, ReceivedSprinklingService receivedSprinklingService, UserService userService, RoomService roomService) {
        this.sprinklingService = sprinklingService;
        this.receivedSprinklingService = receivedSprinklingService;
        this.userService = userService;
        this.roomService = roomService;
    }

    @PostMapping("/create")
    public ApiResult<SprinklingDto> createSprinkling(@RequestHeader("X-USER-ID") Long userId,
                                                     @RequestHeader("X-ROOM-ID") Long roomId,
                                                     @RequestBody SprinklingDto sprinklingDto) {
        if(sprinklingDto.getDivideNum() > sprinklingDto.getBudget()) {
            return ERROR("뿌리기 할 돈이 나눠줄 사람에 비해 매우 적습니다.", HttpStatus.BAD_REQUEST);
        }

        return OK(SprinklingDto.convertToDto(sprinklingService.createSprinkling(sprinklingDto.toEntity(),
                userService.getUser(userId).orElseThrow(() -> new NotFoundException(User.class, userId)),
                roomService.getRoom(roomId).orElseThrow(() -> new NotFoundException(Room.class, roomId))
                )));
    }

    @PostMapping("/receive")
    public ApiResult<ReceivedSprinklingDto> receiveSprinkling(@RequestHeader("X-USER-ID") Long userId,
                                                              @RequestHeader("X-ROOM-ID") Long roomId,
                                                              @RequestBody SprinklingDto sprinklingDto) {
        Sprinkling sprinkling = sprinklingService.getSprinkling(sprinklingDto.getToken()).orElseThrow(() -> new NotFoundException(Sprinkling.class, sprinklingDto.getToken()));

        if(sprinkling.getOwner().getId() == userId) {
            return ERROR("자신이 뿌리기 한 건은 받을 수 없습니다.", HttpStatus.BAD_REQUEST);
        } else if(sprinkling.getRoom().getId() != roomId) {
            return ERROR("뿌리기가 호출된 대화방에 속해 있지 않습니다.", HttpStatus.BAD_REQUEST);
        } else if(receivedSprinklingService.checkReceivedUser(sprinkling.getReceivedSprinklings(), userId)) {
            return ERROR("이미 받은 뿌리기입니다.", HttpStatus.BAD_REQUEST);
        } else if(ChronoUnit.MINUTES.between(sprinkling.getSprinklingDate(), LocalDateTime.now()) > 10) {
            sprinklingService.shutDownSprinkling(sprinkling);
            return ERROR("해당 뿌리기는 유효시간이 지났습니다.", HttpStatus.BAD_REQUEST);
        }

        ReceivedSprinkling receivedSprinkling = receivedSprinklingService.receiveSprinkling(sprinkling,
                userService.getUser(userId).orElseThrow(() -> new NotFoundException(User.class, userId)));

        if(receivedSprinkling == null) {
            return ERROR("뿌리기를 다른 사용자들이 모두 받았습니다.", HttpStatus.BAD_REQUEST);
        }

        return OK(ReceivedSprinklingDto.convertToDto(receivedSprinkling));
    }

    @GetMapping("/info")
    public ApiResult<SprinklingDto> getInfo(@RequestHeader("X-USER-ID") Long userId,
                                            @RequestBody SprinklingDto sprinklingDto) {
        Sprinkling sprinkling = sprinklingService.getSprinkling(sprinklingDto.getToken()).orElseThrow(() -> new NotFoundException(Sprinkling.class, sprinklingDto.getToken()));

        if(sprinkling.getOwner().getId() != userId) {
            return ERROR("해당 건에 대해 뿌린 사람이 아닙니다.", HttpStatus.BAD_REQUEST);
        } else if(ChronoUnit.DAYS.between(sprinkling.getSprinklingDate(), LocalDateTime.now()) > 7) {
            return ERROR("조회 기간이 만료되었습니다.", HttpStatus.BAD_REQUEST);
        }

        return OK(SprinklingDto.convertToDto(sprinkling));
    }
}

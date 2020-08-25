package com.github.espress91.kakaopaysprinkling.service;

import com.github.espress91.kakaopaysprinkling.data.model.Room;
import com.github.espress91.kakaopaysprinkling.data.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoomServiceTest {
    @Autowired
    RoomService roomService;

    @Autowired
    UserService userService;

    private User roomMaker;

    private User enterUser;

    private Room room;

    @BeforeAll
    void 사용자_등록() {
        roomMaker = new User();
        roomMaker.setBudget(100000);
        userService.enrollUser(roomMaker);

        enterUser = new User();
        enterUser.setBudget(100000);
        userService.enrollUser(enterUser);
    }

    @Test
    @Order(1)
    void 방_만들기() {
        room = roomService.createRoom(roomMaker);
        assertThat(room.getParticipants()).isEqualTo(roomService.getRoom(room.getId()).get().getParticipants());
    }

    @Test
    @Order(2)
    void 방에_들어가기() {
        assertThat(roomService.enterRoom(enterUser, room).getParticipants()).isEqualTo(roomService.getRoom(room.getId()).get().getParticipants());
    }
}
package com.github.espress91.kakaopaysprinkling.service;

import com.github.espress91.kakaopaysprinkling.data.model.Room;
import com.github.espress91.kakaopaysprinkling.data.model.Sprinkling;
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
class ReceivedSprinklingServiceTest {
    @Autowired
    RoomService roomService;

    @Autowired
    UserService userService;

    @Autowired
    SprinklingService sprinklingService;

    @Autowired
    ReceivedSprinklingService receivedSprinklingService;

    private Sprinkling sprinkling;

    private User user;

    private User receiver;

    private Room room;

    @BeforeAll
    void 테스트_준비() {
        user = new User();
        user.setBudget(30000);
        userService.enrollUser(user);

        room = roomService.createRoom(user);

        sprinkling = new Sprinkling();
        sprinkling.setBudget(20000);
        sprinkling.setDivideNum(7);

        sprinkling = sprinklingService.setSprinkling(sprinkling, user, room);
        sprinklingService.divideBudget(sprinkling);

        receiver = new User();
        receiver.setBudget(0);
        userService.enrollUser(receiver);

        roomService.enterRoom(receiver, room);
    }

    @Test
    @Order(1)
    void 뿌리기를_받았는지_확인하기() {
        assertThat(receivedSprinklingService.checkReceivedUser(sprinkling.getReceivedSprinklings(), receiver.getId()))
                .isFalse();
    }

    @Test
    @Order(2)
    void 뿌리기_받기() {
        assertThat(receivedSprinklingService.receiveSprinkling(sprinkling, receiver)).isNotNull();
    }

    @Test
    @Order(3)
    void 뿌리기를_받았는지_확인하기2() {
        assertThat(receivedSprinklingService.checkReceivedUser(sprinkling.getReceivedSprinklings(), receiver.getId()))
                .isTrue();
    }
}
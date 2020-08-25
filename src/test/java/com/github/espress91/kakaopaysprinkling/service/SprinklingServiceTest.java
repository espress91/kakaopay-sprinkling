package com.github.espress91.kakaopaysprinkling.service;

import com.github.espress91.kakaopaysprinkling.data.model.ReceivedSprinkling;
import com.github.espress91.kakaopaysprinkling.data.model.Room;
import com.github.espress91.kakaopaysprinkling.data.model.Sprinkling;
import com.github.espress91.kakaopaysprinkling.data.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SprinklingServiceTest {
    @Autowired
    RoomService roomService;

    @Autowired
    UserService userService;

    @Autowired
    SprinklingService sprinklingService;

    private Sprinkling sprinkling;

    private User user;

    private Room room;

    @BeforeAll
    void 테스트_준비() {
        user = new User();
        user.setBudget(20000);
        userService.enrollUser(user);

        room = roomService.createRoom(user);

        sprinkling = new Sprinkling();
        sprinkling.setBudget(10000);
        sprinkling.setDivideNum(5);
    }

    @Test
    @Order(1)
    void 돈_뿌리는_사람의_돈을_뿌린만큼_줄이기() {
        sprinklingService.setOwnerBudget(sprinkling, user);
        assertThat(user.getBudget()).isEqualTo(10000);
    }

    @Test
    @Order(2)
    void 뿌리기_등록하기() {
        sprinkling = sprinklingService.setSprinkling(sprinkling, user, room);
        assertThat(sprinklingService.getSprinkling(sprinkling.getId()).get().getBudget()).isEqualTo(10000);
    }

    @Test
    @Order(3)
    void 돈_분배하기() {
        List<ReceivedSprinkling> receivedSprinklings = sprinklingService.divideBudget(sprinkling);

        int sum = 0;
        for(ReceivedSprinkling receivedSprinkling : receivedSprinklings) {
            sum = sum + receivedSprinkling.getBudget();
        }
        assertThat(sum).isEqualTo(10000);
        assertThat(receivedSprinklings.size()).isEqualTo(5);
    }

    @Test
    @Order(4)
    void 뿌리기_마감시_뿌린돈_회수하기() {
        sprinklingService.shutDownSprinkling(sprinkling);
        assertThat(user.getBudget()).isEqualTo(20000);
    }

}
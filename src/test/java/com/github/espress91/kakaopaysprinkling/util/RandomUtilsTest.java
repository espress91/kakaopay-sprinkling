package com.github.espress91.kakaopaysprinkling.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RandomUtilsTest {
    @Test
    void 램덤값_생성한다() {
        assertThat(RandomUtils.getInstance().getRandom(10))
                .isBetween(0, 9);
    }

    @Test
    void 랜덤토큰_생성한다() {
        assertThat(RandomUtils.getInstance().generateToken())
                .hasSize(3);
    }

}
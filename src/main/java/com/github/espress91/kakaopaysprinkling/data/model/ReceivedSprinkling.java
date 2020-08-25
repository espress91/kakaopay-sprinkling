package com.github.espress91.kakaopaysprinkling.data.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class ReceivedSprinkling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne //양방향
    @JoinColumn(name = "SPRINKLING_ID")
    private Sprinkling sprinkling;  //돈 받은 뿌리기

    @ManyToOne  //단방향
    @JoinColumn(name = "USER_ID") // 돈 받은 사용자
    private User user;

    private int budget; //받은 금액

    public void addSprinkling(Sprinkling sprinkling) {
        this.sprinkling = sprinkling;
        sprinkling.getReceivedSprinklings().add(this);
    }
}

package com.github.espress91.kakaopaysprinkling.data.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Sprinkling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne  //단방향
    @JoinColumn(name = "ROOM_ID")
    private Room room;  //단톡방

    @ManyToOne  //단방향
    @JoinColumn(name = "USER_ID")
    private User owner; //돈 뿌린 사람

    private int budget; //뿌린 금액

    @JsonProperty("divide_num")
    private int divideNum;

    @OneToMany(mappedBy = "sprinkling") // 양방향
    private List<ReceivedSprinkling> receivedSprinklings = new ArrayList<>();    //돈 받는 사람

    @JsonProperty("sprinkling_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private LocalDateTime sprinklingDate;

    private int balance;    //잔액

}

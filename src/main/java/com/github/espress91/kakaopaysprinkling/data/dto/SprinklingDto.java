package com.github.espress91.kakaopaysprinkling.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.espress91.kakaopaysprinkling.data.model.Sprinkling;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class SprinklingDto {
    private Long id;

    private String token;

    private RoomDto room;  //단톡방

    private UserDto owner; //돈 뿌린 사람

    private int budget; //뿌린 금액

    @JsonProperty("divide_num")
    private int divideNum;

    private List<ReceivedSprinklingDto> receivedSprinklings = new ArrayList<>();    //돈 받는 사람

    @JsonProperty("sprinkling_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private LocalDateTime sprinklingDate;

    private int balance;    //받기 완료된 금

    public Sprinkling toEntity() {
        Sprinkling sprinkling = new Sprinkling();

        sprinkling.setToken(token);

        if(room != null) {
            sprinkling.setRoom(room.toEntity());
        }
        if(owner != null) {
            sprinkling.setOwner(owner.toEntity());
        }

        sprinkling.setBudget(budget);
        sprinkling.setDivideNum(divideNum);

        if(sprinklingDate == null) {
            sprinkling.setSprinklingDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        } else {
            sprinkling.setSprinklingDate(sprinklingDate);
        }
        sprinkling.setBalance(budget - balance);

        return sprinkling;
    }

    public static SprinklingDto convertToDto(Sprinkling sprinkling) {
        SprinklingDto sprinklingDto = new SprinklingDto();

        sprinklingDto.setId(sprinkling.getId());
        sprinklingDto.setToken((sprinkling.getToken()));
        sprinklingDto.setRoom(RoomDto.convertToDto(sprinkling.getRoom()));
        sprinklingDto.setOwner(UserDto.convertToDto(sprinkling.getOwner()));
        sprinklingDto.setBudget(sprinkling.getBudget());
        sprinklingDto.setDivideNum(sprinkling.getDivideNum());
        sprinkling.getReceivedSprinklings().forEach(receivedSprinkling -> sprinklingDto.getReceivedSprinklings().add(ReceivedSprinklingDto.convertToDto(receivedSprinkling)));
        sprinklingDto.setSprinklingDate(sprinkling.getSprinklingDate());
        sprinklingDto.setBalance(sprinkling.getBudget() - sprinkling.getBalance());

        return sprinklingDto;
    }
}

package com.github.espress91.kakaopaysprinkling.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.espress91.kakaopaysprinkling.data.model.ReceivedSprinkling;
import com.github.espress91.kakaopaysprinkling.repository.SprinklingRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class ReceivedSprinklingDto {
    @JsonIgnore
    @Autowired
    SprinklingRepository sprinklingRepository;

    private Long id;

    private Long sprinklingId;

    private UserDto user;

    private int budget; //받은 금액

    public ReceivedSprinkling toEntity() {
        ReceivedSprinkling receivedSprinkling = new ReceivedSprinkling();

        receivedSprinkling.addSprinkling(sprinklingRepository.findById(sprinklingId).get());
        receivedSprinkling.setUser(user.toEntity());
        receivedSprinkling.setBudget(budget);

        return receivedSprinkling;
    }

    public static ReceivedSprinklingDto convertToDto(ReceivedSprinkling receivedSprinkling) {
        ReceivedSprinklingDto receivedSprinklingDto = new ReceivedSprinklingDto();

        receivedSprinklingDto.setId(receivedSprinkling.getId());
        receivedSprinklingDto.setSprinklingId(receivedSprinkling.getSprinkling().getId());
        if(receivedSprinkling.getUser() != null) {
            receivedSprinklingDto.setUser(UserDto.convertToDto(receivedSprinkling.getUser()));
        }
        receivedSprinklingDto.setBudget(receivedSprinkling.getBudget());

        return receivedSprinklingDto;
    }
}

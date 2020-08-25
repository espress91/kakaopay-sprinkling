package com.github.espress91.kakaopaysprinkling.data.dto;

import com.github.espress91.kakaopaysprinkling.data.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDto {
    private Long id;

    private int budget;

    public User toEntity() {
        User user = new User();

        user.setBudget(budget);

        return user;
    }

    public static UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setBudget(user.getBudget());

        return userDto;
    }

}

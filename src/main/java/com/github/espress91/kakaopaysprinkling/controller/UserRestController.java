package com.github.espress91.kakaopaysprinkling.controller;

import com.github.espress91.kakaopaysprinkling.data.dto.UserDto;
import com.github.espress91.kakaopaysprinkling.data.dto.response.ApiResult;
import com.github.espress91.kakaopaysprinkling.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.espress91.kakaopaysprinkling.data.dto.response.ApiResult.OK;

@Slf4j
@RestController
@RequestMapping("user")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ApiResult<UserDto> enrollUser(@RequestBody UserDto userDto) {
        return OK(UserDto.convertToDto(userService.enrollUser(userDto.toEntity())));
    }
}

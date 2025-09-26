package com.jaydeep.tech_view.dto.auth_dto;

import com.jaydeep.tech_view.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {
    private String accessToken;
    private UserDto userDto;

    public LoginResponseDto(String accessToken) {
        this.accessToken=accessToken;
    }
}

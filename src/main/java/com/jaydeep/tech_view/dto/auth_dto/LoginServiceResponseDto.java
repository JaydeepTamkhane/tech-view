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
public class LoginServiceResponseDto {
    private String accessToken;
    private String refreshToken;
    private UserDto userDto;

}

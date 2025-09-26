package com.jaydeep.tech_view.controller;


import com.jaydeep.tech_view.dto.UserDto;
import com.jaydeep.tech_view.dto.auth_dto.LoginDto;
import com.jaydeep.tech_view.dto.auth_dto.LoginResponseDto;
import com.jaydeep.tech_view.dto.auth_dto.LoginServiceResponseDto;
import com.jaydeep.tech_view.dto.auth_dto.SignUpRequestDto;
import com.jaydeep.tech_view.security.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        return new ResponseEntity<>(authService.signUp(signUpRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        LoginServiceResponseDto loginServiceResponseDto = authService.login(loginDto);
        Cookie cookie = new Cookie("refreshToken", loginServiceResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);

//        need to make the below false as the localhost is http not https
//        cookie.setSecure(true);
        cookie.setSecure(false);
        response.addCookie(cookie);
        return new ResponseEntity<>(LoginResponseDto.builder().accessToken(loginServiceResponseDto.getAccessToken()).userDto(loginServiceResponseDto.getUserDto()).build(), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request) {
        String refreshToken = Arrays.stream(request.getCookies()).filter(cookie -> "refreshToken".equals(cookie.getName())).findFirst().map(Cookie::getValue).orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));

        String accessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new LoginResponseDto(accessToken));
    }
}

package com.jaydeep.tech_view.controller;



import com.jaydeep.tech_view.advice.ApiResponse;
import com.jaydeep.tech_view.dto.UserDto;
import com.jaydeep.tech_view.security.CurrentUserUtil;
import com.jaydeep.tech_view.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        UserDto user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }
}

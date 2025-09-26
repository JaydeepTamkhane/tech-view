package com.jaydeep.tech_view.controller.admin_controller;


import com.jaydeep.tech_view.advice.ApiResponse;
import com.jaydeep.tech_view.security.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class AdminController {
    private final CurrentUserUtil currentUserUtil;

    @GetMapping
    public ApiResponse<String> getAdmin() {
        return new ApiResponse<>("welcome admin " + currentUserUtil.getCurrentUser());
    }
}

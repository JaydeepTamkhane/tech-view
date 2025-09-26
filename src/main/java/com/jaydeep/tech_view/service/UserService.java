package com.jaydeep.tech_view.service;


import com.jaydeep.tech_view.dto.UserDto;
import com.jaydeep.tech_view.entity.User;

public interface UserService {
    User getUserById(Long userId);
    UserDto getCurrentUser();
}

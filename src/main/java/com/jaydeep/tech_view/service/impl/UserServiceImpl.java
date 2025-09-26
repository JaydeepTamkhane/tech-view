package com.jaydeep.tech_view.service.impl;


import com.jaydeep.tech_view.dto.UserDto;
import com.jaydeep.tech_view.entity.User;
import com.jaydeep.tech_view.exception.ResourceNotFoundException;
import com.jaydeep.tech_view.repository.UserRepository;
import com.jaydeep.tech_view.security.CurrentUserUtil;
import com.jaydeep.tech_view.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final CurrentUserUtil currentUserUtil;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public User getUserById(Long userId) {
        return userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("use not found with id " + userId));
    }

    @Override
    @Transactional
    public UserDto getCurrentUser() {
        User user= currentUserUtil.getCurrentUser();
        UserDto userDto= modelMapper.map(user, UserDto.class);
        return userDto;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }


}

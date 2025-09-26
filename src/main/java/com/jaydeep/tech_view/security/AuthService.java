package com.jaydeep.tech_view.security;




import com.jaydeep.tech_view.dto.auth_dto.LoginDto;
import com.jaydeep.tech_view.dto.auth_dto.LoginServiceResponseDto;
import com.jaydeep.tech_view.dto.auth_dto.SignUpRequestDto;
import com.jaydeep.tech_view.dto.UserDto;
import com.jaydeep.tech_view.entity.User;
import com.jaydeep.tech_view.entity.enums.Role;
import com.jaydeep.tech_view.exception.ResourceNotFoundException;
import com.jaydeep.tech_view.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserDto signUp(SignUpRequestDto signUpRequestDto) {
        User user = userRepository.findByEmail(signUpRequestDto.getEmail()).orElse(null);
        if (user != null) {
            throw new RuntimeException("user already exits with email " + signUpRequestDto.getEmail());
        }

        User newUser = modelMapper.map(signUpRequestDto, User.class);
        newUser.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        newUser.setRoles(Set.of(Role.USER));
        newUser = userRepository.save(newUser);
        return modelMapper.map(newUser, UserDto.class);
    }

    public LoginServiceResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword()
        ));

        User user = (User) authentication.getPrincipal();


        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        UserDto userDto= modelMapper.map(user, UserDto.class);
        return LoginServiceResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userDto(userDto)
                .build();
    }

    public String refreshToken(String refreshToken) {
        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new RuntimeException("Provided token is not a refresh token");
        }
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return jwtService.generateAccessToken(user);
    }
}

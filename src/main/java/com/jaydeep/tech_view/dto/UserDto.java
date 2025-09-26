package com.jaydeep.tech_view.dto;


import com.jaydeep.tech_view.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private  String name;
//    will see in the future whether to disclose the use email or not
//    private String email;
    private Set<Role> roles;
}

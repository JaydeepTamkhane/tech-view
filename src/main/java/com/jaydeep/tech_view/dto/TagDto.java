package com.jaydeep.tech_view.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TagDto {
    private Long id;

    @NotBlank(message = "Tag name must not be blank")
    private String name;
}

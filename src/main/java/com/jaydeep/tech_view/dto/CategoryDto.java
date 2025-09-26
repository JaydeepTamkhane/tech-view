package com.jaydeep.tech_view.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Category name must not be blank")
    private String name;
}

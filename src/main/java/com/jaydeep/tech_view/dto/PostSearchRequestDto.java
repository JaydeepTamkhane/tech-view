package com.jaydeep.tech_view.dto;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostSearchRequestDto {
    private String searchInput;
    private List<String> categories;
    private List<String> tags;
    private Integer year;
}

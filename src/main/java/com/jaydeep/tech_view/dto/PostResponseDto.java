package com.jaydeep.tech_view.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String coverImageUrl;
    private String coverImagePublicId;
    private Long authorId;
    private Set<String> tags;
    private Set<String> categories;
    private Integer totalLikes;
    private Integer totalComments;
    private Boolean likedByCurrentUser;
}

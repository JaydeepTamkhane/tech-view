package com.jaydeep.tech_view.dto;

import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private Long postId;
    private AuthorDto authorDto;
    private Long parentId;
    private String content;
}

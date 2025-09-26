package com.jaydeep.tech_view.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CommentRequestDto {
    @NotBlank(message = "Comment content can't be blank")
    private String content;
    private Long parentId;
}

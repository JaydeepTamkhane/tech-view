package com.jaydeep.tech_view.service;

import com.jaydeep.tech_view.dto.CommentRequestDto;
import com.jaydeep.tech_view.dto.CommentResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    CommentResponseDto createComment(Long postId, @Valid CommentRequestDto createCommentRequestDto);

    Page<CommentResponseDto>getTopLevelComments(Long postId, Pageable pageable);

    List<CommentResponseDto>getChildComments(Long parentCommentId);

    void deleteComment(Long postId, Long commentId);
}

package com.jaydeep.tech_view.service.impl;

import com.jaydeep.tech_view.dto.AuthorDto;
import com.jaydeep.tech_view.dto.CommentRequestDto;
import com.jaydeep.tech_view.dto.CommentResponseDto;
import com.jaydeep.tech_view.entity.Comment;
import com.jaydeep.tech_view.entity.Post;
import com.jaydeep.tech_view.entity.User;
import com.jaydeep.tech_view.exception.BadRequestException;
import com.jaydeep.tech_view.exception.ResourceNotFoundException;
import com.jaydeep.tech_view.repository.CommentRepository;
import com.jaydeep.tech_view.repository.PostRepository;
import com.jaydeep.tech_view.repository.UserRepository;
import com.jaydeep.tech_view.security.CurrentUserUtil;
import com.jaydeep.tech_view.security.OwnershipUtil;
import com.jaydeep.tech_view.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CurrentUserUtil currentUserUtil;
    private final UserRepository userRepository;
    private final OwnershipUtil ownershipUtil;

    @Override
    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto createCommentRequestDto) {
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post not found with id" + postId));
        User user = currentUserUtil.getCurrentUser();
        Comment parent = null;
        if (createCommentRequestDto.getParentId() != null) {
            parent = commentRepository
                    .findById(createCommentRequestDto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("user not found with id" +
                            createCommentRequestDto.getParentId()));
            if (!parent.getPost().getId().equals(postId)) {
                throw new ResourceNotFoundException("Parent comment does not belong to this post");
            }
        }
        Comment comment = Comment.builder()
                .post(post)
                .parent(parent)
                .author(user)
                .content(createCommentRequestDto.getContent())
                .build();
        Comment savedComment = commentRepository.save(comment);
        post.setTotalComments(post.getTotalComments() + 1);
        postRepository.save(post);

        return CommentResponseDto
                .builder()
                .id(savedComment.getId())
                .parentId(savedComment.getParent() != null ? savedComment.getParent().getId() : null)
                .authorDto(AuthorDto
                        .builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build())
                .content(savedComment.getContent())
                .build();
    }

    @Override
    @Transactional
    public Page<CommentResponseDto> getTopLevelComments(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id" + postId));

        return commentRepository.findByPostAndParentIsNullOrderByCreatedAtAsc(post, pageable)
                .map((comment ->
                        CommentResponseDto
                                .builder()
                                .id(comment.getId())
                                .postId(comment.getPost().getId())
                                .parentId(null)
                                .authorDto(
                                        AuthorDto.builder()
                                                .id(comment.getAuthor().getId())
                                                .name(comment.getAuthor().getName())
                                                .build()
                                )
                                .content(comment.getContent())
                                .build()));
    }

    @Override
    @Transactional
    public List<CommentResponseDto> getChildComments(Long parentCommentId) {
        Comment parent = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + parentCommentId));


        return commentRepository.findByParentOrderByCreatedAtAsc(parent)
                .stream()
                .map((comment ->
                        CommentResponseDto
                                .builder()
                                .id(comment.getId())
                                .postId(comment.getPost().getId())
                                .parentId(comment.getParent().getId())
                                .authorDto(
                                        AuthorDto.builder()
                                                .id(comment.getAuthor().getId())
                                                .name(comment.getAuthor().getName())
                                                .build()
                                )
                                .content(comment.getContent())
                                .build()))
                .collect(Collectors.toList()

                );
    }

    @Override
    @Transactional
    public void deleteComment(Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));


        ownershipUtil.ensureCurrentUserOwnership(comment.getAuthor());


        if (!comment.getPost().getId().equals(postId)) {
            throw new BadRequestException("Comment does not belong to the post with id :" + postId);
        }
        commentRepository.delete(comment);
    }


}

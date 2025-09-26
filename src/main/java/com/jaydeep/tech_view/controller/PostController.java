package com.jaydeep.tech_view.controller;

import com.jaydeep.tech_view.dto.*;
import com.jaydeep.tech_view.repository.PostLikeRepository;
import com.jaydeep.tech_view.service.CommentService;
import com.jaydeep.tech_view.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final PostLikeRepository postLikeRepository;


    //    Search
    @GetMapping("/search")
    public Page<PostResponseDto> searchPosts(
            @RequestBody PostSearchRequestDto postSearchRequestDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.searchPosts(postSearchRequestDto, PageRequest.of(page, size));
    }


    //    core post controller below
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody PostRequestDto dto) throws Exception {
        PostResponseDto response = postService.createPost(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{postId}/cover-image", consumes = {"multipart/form-data"})
    public ResponseEntity<PostResponseDto> uploadCoverImage(
            @PathVariable Long postId,
            @RequestParam("file") MultipartFile file) throws Exception {

        PostResponseDto response = postService.uploadCoverImage(postId, file);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}/cover-image")
    public ResponseEntity<PostResponseDto> deleteCoverImage(@PathVariable Long postId) throws Exception {
        PostResponseDto response = postService.deleteCoverImage(postId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PostResponseDto> posts = postService.getAllPosts(PageRequest.of(page, size));
        return ResponseEntity.ok(posts);
    }


    // Get single post by id
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long postId) {
        PostResponseDto post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }


    //    comment controllers from below

    @PostMapping("/{postId}/comment")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long postId, @Valid @RequestBody CommentRequestDto createCommentRequestDto) {
        CommentResponseDto createCommentResponseDto = commentService.createComment(postId, createCommentRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createCommentResponseDto);
    }


    @GetMapping("/{postId}/comment")
    public ResponseEntity<Page<CommentResponseDto>> getTopLevelComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CommentResponseDto> comments = commentService.getTopLevelComments(postId, PageRequest.of(page, size));
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {

        commentService.deleteComment(postId, commentId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{postId}/comment/{parentId}/replies")
    public ResponseEntity<List<CommentResponseDto>> getTopLevelComments(
            @PathVariable Long postId, @PathVariable Long parentId) {

        List<CommentResponseDto> comments = commentService.getChildComments(parentId);
        return ResponseEntity.ok(comments);
    }


    //like controllers
//    todo return the boolean likedByUser using the postlike repo in the postresponseDto so we can show upvote or not
    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {
        postService.likePost(postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId) {
        postService.unlikePost(postId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{postId}/like")
    public ResponseEntity<Page<AuthorDto>> getAllLikes(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AuthorDto> likes = postService.getPostLikes(postId, PageRequest.of(page, size));

        return ResponseEntity.ok(likes);
    }

}

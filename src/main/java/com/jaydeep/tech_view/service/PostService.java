package com.jaydeep.tech_view.service;


import com.jaydeep.tech_view.dto.AuthorDto;
import com.jaydeep.tech_view.dto.PostRequestDto;
import com.jaydeep.tech_view.dto.PostResponseDto;
import com.jaydeep.tech_view.dto.PostSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PostService {

    Page<PostResponseDto> searchPosts(PostSearchRequestDto postSearchRequestDto,Pageable pageable);

    PostResponseDto createPost(PostRequestDto dto);

    PostResponseDto uploadCoverImage(Long postId, MultipartFile file) throws IOException;

    PostResponseDto deleteCoverImage(Long postId) throws IOException;


    Page<PostResponseDto> getAllPosts(Pageable pageable);  // must return Page<PostResponseDto>

    PostResponseDto getPostById(Long postId);

    void likePost(Long postId);

    void unlikePost(Long postId);

    Page<AuthorDto> getPostLikes(Long postId, Pageable pageable);
}
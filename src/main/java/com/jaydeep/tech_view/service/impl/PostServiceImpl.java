package com.jaydeep.tech_view.service.impl;

import com.jaydeep.tech_view.dto.AuthorDto;
import com.jaydeep.tech_view.dto.PostRequestDto;
import com.jaydeep.tech_view.dto.PostResponseDto;
import com.jaydeep.tech_view.dto.PostSearchRequestDto;
import com.jaydeep.tech_view.entity.*;
import com.jaydeep.tech_view.exception.ResourceNotFoundException;
import com.jaydeep.tech_view.repository.*;
import com.jaydeep.tech_view.security.CurrentUserUtil;
import com.jaydeep.tech_view.service.CloudinaryService;
import com.jaydeep.tech_view.service.PostService;
import com.jaydeep.tech_view.specification.PostSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;
    private final CurrentUserUtil currentUserUtil;
    private final PostLikeRepository postLikeRepository;

    @Override
    public Page<PostResponseDto> searchPosts(PostSearchRequestDto postSearchRequestDto, Pageable pageable) {
        Specification<Post> spec = PostSpecifications.search(
                postSearchRequestDto.getSearchInput(),
                postSearchRequestDto.getCategories(),
                postSearchRequestDto.getTags(),
                postSearchRequestDto.getYear()
        );

        Page<Post> posts = postRepository.findAll(spec, pageable);

        return posts.map(post -> PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .coverImageUrl(post.getCoverImageUrl())
                .coverImagePublicId(post.getCoverImagePublicId())
                .authorId(post.getAuthor().getId())
                .tags(post.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                .categories(post.getCategories().stream().map(Category::getName).collect(Collectors.toSet()))
                .totalLikes(post.getTotalLikes())
                .totalComments(post.getTotalComments())
                .build());
    }

    @Override
    @Transactional
    public PostResponseDto createPost(PostRequestDto dto) {
        User author = userRepository.findById(dto.getAuthorId()).orElseThrow(() -> new RuntimeException("Author not found"));

        Set<Tag> tags = dto.getTagIds() != null ? new HashSet<>(tagRepository.findAllById(dto.getTagIds())) : new HashSet<>();

        Set<Category> categories = dto.getCategoryIds() != null ? new HashSet<>(categoryRepository.findAllById(dto.getCategoryIds())) : new HashSet<>();

        Post post = Post.builder().title(dto.getTitle()).content(dto.getContent()).author(author).coverImageUrl(null).coverImagePublicId(null).tags(tags).categories(categories).totalLikes(0).totalComments(0).build();


        Post savedPost = postRepository.save(post);

        return PostResponseDto.builder().id(savedPost.getId()).title(savedPost.getTitle()).content(savedPost.getContent()).coverImageUrl(savedPost.getCoverImageUrl()).coverImagePublicId(savedPost.getCoverImagePublicId()).authorId(savedPost.getAuthor().getId()).tags(savedPost.getTags().stream().map(Tag::getName).collect(Collectors.toSet())).categories(savedPost.getCategories().stream().map(Category::getName).collect(Collectors.toSet())).totalLikes(savedPost.getTotalLikes()).totalComments(savedPost.getTotalComments()).build();
    }

    @Override
    @Transactional
    public PostResponseDto uploadCoverImage(Long postId, MultipartFile file) throws IOException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        Map uploadResult = cloudinaryService.uploadPostCoverImage(file, post.getAuthor().getId(), post.getId());

        post.setCoverImageUrl((String) uploadResult.get("secure_url"));
        post.setCoverImagePublicId((String) uploadResult.get("public_id"));

        Post updated = postRepository.save(post);

        return PostResponseDto.builder().id(updated.getId()).title(updated.getTitle()).content(updated.getContent()).coverImageUrl(updated.getCoverImageUrl()).coverImagePublicId(updated.getCoverImagePublicId()).authorId(updated.getAuthor().getId()).tags(updated.getTags().stream().map(Tag::getName).collect(Collectors.toSet())).categories(updated.getCategories().stream().map(Category::getName).collect(Collectors.toSet())).totalLikes(updated.getTotalLikes()).totalComments(updated.getTotalComments()).build();
    }

    @Override
    @Transactional
    public PostResponseDto deleteCoverImage(Long postId) throws IOException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        if (post.getCoverImagePublicId() == null) {
            throw new IllegalStateException("No cover image to delete");
        }

        // Delete from Cloudinary
        cloudinaryService.deleteFile(post.getCoverImagePublicId());

        // Reset fields
        post.setCoverImageUrl(null);
        post.setCoverImagePublicId(null);

        Post updated = postRepository.save(post);

        return PostResponseDto.builder().id(updated.getId()).title(updated.getTitle()).content(updated.getContent()).coverImageUrl(updated.getCoverImageUrl()).coverImagePublicId(updated.getCoverImagePublicId()).authorId(updated.getAuthor().getId()).tags(updated.getTags().stream().map(Tag::getName).collect(Collectors.toSet())).categories(updated.getCategories().stream().map(Category::getName).collect(Collectors.toSet())).totalLikes(updated.getTotalLikes()).totalComments(updated.getTotalComments()).build();
    }


    @Override
    @Transactional(readOnly = true)
    public Page<PostResponseDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(post ->
                        PostResponseDto
                                .builder()
                                .id(post.getId())
                                .title(post.getTitle())
                                .content(post.getContent())
                                .coverImageUrl(post.getCoverImageUrl())
                                .coverImagePublicId(post.getCoverImagePublicId())
                                .authorId(post.getAuthor().getId())
                                .tags(post.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                                .categories(post.getCategories().stream().map(Category::getName).collect(Collectors.toSet()))
                                .totalLikes(post.getTotalLikes())
                                .totalComments(post.getTotalComments()).build());
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponseDto getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
        User user = currentUserUtil.getCurrentUser();
        Boolean likedByCurrentUser = postLikeRepository.existsByUserAndPost(user, post);
        return PostResponseDto
                .builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .coverImageUrl(post.getCoverImageUrl())
                .coverImagePublicId(post.getCoverImagePublicId())
                .authorId(post.getAuthor().getId())
                .tags(post.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                .categories(post.getCategories().stream().map(Category::getName).collect(Collectors.toSet()))
                .totalLikes(post.getTotalLikes())
                .totalComments(post.getTotalComments())
                .likedByCurrentUser(likedByCurrentUser)
                .build();
    }

    @Override
    @Transactional
    public void likePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        User user = currentUserUtil.getCurrentUser();
        if (postLikeRepository.existsByUserAndPost(user, post)) {
            throw new IllegalStateException("Already Liked These post");
        }
        PostLike postLike = PostLike.builder().user(user).post(post).build();
        PostLike savedLike = postLikeRepository.save(postLike);
        post.setTotalLikes(post.getTotalLikes() + 1);

        postRepository.save(post);
    }


    @Override
    @Transactional
    public void unlikePost(Long postId) {
        User user = currentUserUtil.getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post not found with id: " + postId));
        PostLike like = postLikeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new ResourceNotFoundException("Like not found "));

        postLikeRepository.delete(like);

        post.setTotalLikes(post.getTotalLikes() - 1);
        postRepository.save(post);
    }

    @Override
    public Page<AuthorDto> getPostLikes(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post not found with id: " + postId));

        Page<PostLike> likes = postLikeRepository.findByPostOrderByCreatedAtAsc(post, pageable);


        return likes.map(like ->
                AuthorDto
                        .builder()
                        .id(like.getUser().getId())
                        .name(like.getUser().getName())
                        .build());
    }


}

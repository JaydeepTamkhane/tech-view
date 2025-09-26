package com.jaydeep.tech_view.repository;

import com.jaydeep.tech_view.entity.Post;
import com.jaydeep.tech_view.entity.PostLike;
import com.jaydeep.tech_view.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUserAndPost(User user, Post post);

    Optional<PostLike> findByUserAndPost(User user, Post post);

    Page<PostLike> findByPostOrderByCreatedAtAsc(Post post, Pageable pageable);

}

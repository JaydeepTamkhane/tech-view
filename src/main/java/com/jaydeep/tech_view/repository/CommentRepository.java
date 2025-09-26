package com.jaydeep.tech_view.repository;

import com.jaydeep.tech_view.entity.Comment;
import com.jaydeep.tech_view.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostAndParentIsNullOrderByCreatedAtAsc(Post post, Pageable pageable);

    List<Comment> findByParentOrderByCreatedAtAsc(Comment parent);
}

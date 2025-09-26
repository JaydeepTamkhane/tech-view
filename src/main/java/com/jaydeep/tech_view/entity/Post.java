package com.jaydeep.tech_view.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "post")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "author_id")
    private User author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String coverImageUrl;

    @Column(nullable = true)
    private String coverImagePublicId;

    @Column(nullable = false)
    private Integer totalLikes = 0;

    @Column(nullable = false)
    private Integer totalComments = 0;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_category",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<PostLike> likes;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<Comment> comments;
}


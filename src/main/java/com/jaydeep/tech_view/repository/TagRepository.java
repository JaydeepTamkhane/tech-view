package com.jaydeep.tech_view.repository;

import com.jaydeep.tech_view.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}

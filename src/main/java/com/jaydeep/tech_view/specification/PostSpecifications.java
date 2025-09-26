package com.jaydeep.tech_view.specification;


import com.jaydeep.tech_view.entity.Post;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PostSpecifications {

    public static Specification<Post> search(String searchInput, List<String> categories, List<String> tags, Integer year) {
        return (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            if (searchInput != null && !searchInput.isEmpty()) {
                String[] keywords = searchInput.trim().split("\\s+");
                for (String kw : keywords) {
                    String pattern = "%" + kw.toLowerCase() + "%";
                    Predicate keywordPredicate = cb.or(cb.like(cb.lower(root.get("title")), pattern), cb.like(cb.lower(root.get("author").get("name")), pattern));
                    predicates.add(keywordPredicate);
                }
            }

            if (categories != null && !categories.isEmpty()) {
                predicates.add(root.join("categories").get("name").in(categories));
            }

            if (tags != null && !tags.isEmpty()) {
                predicates.add(root.join("tags").get("name").in(tags));
            }

            if (year != null) {
                predicates.add(cb.equal(cb.function("date_part", Integer.class, cb.literal("year"), root.get("createdAt")), year));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}



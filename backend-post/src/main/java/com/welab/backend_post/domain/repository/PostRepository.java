package com.welab.backend_post.domain.repository;

import com.welab.backend_post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByUserId(String userId);
}

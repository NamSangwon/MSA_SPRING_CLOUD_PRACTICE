package com.welab.backend_post.domain.repository;

import com.welab.backend_post.domain.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    PostComment findByUserId(String userId);
}

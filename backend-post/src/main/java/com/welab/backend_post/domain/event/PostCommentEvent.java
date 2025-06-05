package com.welab.backend_post.domain.event;

import com.welab.backend_post.domain.Post;
import com.welab.backend_post.domain.PostComment;
import com.welab.backend_post.remote.user.dto.SiteUserInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostCommentEvent {
    public static final String Topic = "post-comment";

    private String action;
    private String userId; // 댓글 작성자
    private String comment; // 댓글 작성 내용

    @Getter
    @Setter
    public static class TargetPost {
        private String userId; // 포스트 작성자
        private String title; // 포스트 타이틀
        private String content; // 포스트 내용
    }

    private TargetPost targetPost;

    public static PostCommentEvent fromEntity(String action, PostComment postComment) {
        PostCommentEvent event = new PostCommentEvent();

        event.action = action;
        event.userId = postComment.getUserId();
        event.comment = postComment.getComment();

        Post post = postComment.getPost();
        if (post != null) {
            event.targetPost = new TargetPost();

            event.targetPost.setUserId(post.getUserId());
            event.targetPost.setTitle(post.getTitle());
            event.targetPost.setContent(post.getContent());
        }

        return event;
    }
}

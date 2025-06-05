package com.welab.backend_alim.event.consumer.message.post;

import lombok.Getter;
import lombok.Setter;

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
}
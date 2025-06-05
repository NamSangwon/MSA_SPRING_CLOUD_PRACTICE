package com.welab.backend_post.service.Aop;

import com.welab.backend_post.common.type.ActionAndId;
import com.welab.backend_post.domain.PostComment;
import com.welab.backend_post.domain.event.PostCommentEvent;
import com.welab.backend_post.domain.repository.PostCommentRepository;
import com.welab.backend_post.event.producer.KafkaMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PostCommentCreateEventAop {
    private final KafkaMessageProducer kafkaMessageProducer;
    private final PostCommentRepository postCommentRepository;

    @AfterReturning(
            value = "execution(* com.welab.backend_post.service.PostService.*AndNotify(..))",
            returning = "actionAndId"
    )
    public void publishPostCommentCreateEvent(JoinPoint joinPoint, ActionAndId actionAndId) {
        publishPostCommentCreateEvent(actionAndId);
    }

    private void publishPostCommentCreateEvent(ActionAndId actionAndId) {
        try {
            PostComment postComment = postCommentRepository.findById(actionAndId.getId()).orElse(null);

            if (postComment == null) {
                log.warn("postComment is null");
                return;
            }

            kafkaMessageProducer.send(PostCommentEvent.Topic, PostCommentEvent.fromEntity("Create", postComment));
        } catch (Exception e) {
            log.warn("새로운 댓글 추가 이벤트를 전송하지 못하였습니다. id={}", actionAndId.getId());
        }
    }

}

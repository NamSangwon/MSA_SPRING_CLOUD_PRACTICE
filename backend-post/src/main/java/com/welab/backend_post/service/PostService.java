package com.welab.backend_post.service;

import com.welab.backend_post.common.exception.NotFound;
import com.welab.backend_post.common.type.ActionAndId;
import com.welab.backend_post.domain.Post;
import com.welab.backend_post.domain.PostComment;
import com.welab.backend_post.domain.dto.PostCommentCreateDto;
import com.welab.backend_post.domain.dto.PostCreateDto;
import com.welab.backend_post.domain.event.PostCommentEvent;
import com.welab.backend_post.domain.repository.PostCommentRepository;
import com.welab.backend_post.domain.repository.PostRepository;
import com.welab.backend_post.event.producer.KafkaMessageProducer;
import com.welab.backend_post.remote.alim.RemoteAlimService;
import com.welab.backend_post.remote.alim.dto.SendSmsDto;
import com.welab.backend_post.remote.user.RemoteUserService;
import com.welab.backend_post.remote.user.dto.SiteUserInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final RemoteUserService remoteUserService;
    private final RemoteAlimService remoteAlimService;

    @Transactional
    public void createPost(PostCreateDto createDto) {
        Post post = createDto.toEntity();

        postRepository.save(post);
    }

    @Transactional
    public void addPostComment(PostCommentCreateDto createDto) {
        Post post = postRepository.findById(createDto.getPostId())
                    .orElseThrow(() -> new NotFound("포스팅 글을 찾을 수 없습니다."));

        PostComment postComment = createDto.toEntity();
        postCommentRepository.save(postComment);

        post.addComment(postComment);

        // 알림톡을 보내기 위한 사용자 정보 조회'
        SiteUserInfoDto userInfoDto = remoteUserService.userInfo(post.getUserId()).getData();

        // 알맅톡 전송 요청
        SendSmsDto.Request requestDto = new SendSmsDto.Request();
        requestDto.setUserId(userInfoDto.getUserId());
        requestDto.setPhoneNumber(userInfoDto.getPhoneNumber());
        requestDto.setTitle("댓글 달림");
        requestDto.setMessage("새로운 댓글이 달렸습니다.");

        remoteAlimService.sendSms(requestDto);
    }

    @Transactional
    public ActionAndId addPostCommentAndNotify(PostCommentCreateDto createDto) {
        Post post = postRepository.findById(createDto.getPostId())
                .orElseThrow(() -> new NotFound("포스팅 글을 찾을 수 없습니다."));

        PostComment postComment = createDto.toEntity();
        postCommentRepository.save(postComment);

        post.addComment(postComment);

        return ActionAndId.of("Create", postComment.getId());
    }
}

package com.classmate.post_service.service.impl;

import com.classmate.post_service.client.ICommentClient;
import com.classmate.post_service.dto.APIResponseDTO;
import com.classmate.post_service.dto.CommentDTO;
import com.classmate.post_service.dto.PostDTO;
import com.classmate.post_service.entity.Post;
import com.classmate.post_service.mapper.IPostMapper;
import com.classmate.post_service.repository.IPostRepository;
import com.classmate.post_service.service.IPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements IPostService {

    private final IPostRepository postRepository;
    private final IPostMapper postMapper;
    private final ICommentClient ICommentClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);

    public PostServiceImpl(IPostRepository postRepository, IPostMapper postMapper, ICommentClient ICommentClient) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.ICommentClient = ICommentClient;
    }

    @Override
    public APIResponseDTO getPostById(Long id) {
        LOGGER.info("Getting post by id...");
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        APIResponseDTO apiResponseDTO = postMapper.convertToAPIResponseDTO(post);
        List<CommentDTO> commentDTOS = ICommentClient.getCommentsByPostId(id, 0, 10);
        apiResponseDTO.setCommentDTOS(commentDTOS);
        return apiResponseDTO;
    }

    @Override
    public List<PostDTO> getPostsByName(String name, int page, int size) {
        Page<Post> postsPage = postRepository.findByTitleContainingIgnoreCase(name, PageRequest.of(page, size));
        return postsPage.getContent().stream().map(postMapper::convertToPostDTO).collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> getPostsByForumId(Long forumId, int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return postRepository.findByForumId(forumId, pageRequest)
                .map(postMapper::convertToPostDTO)
                .getContent();
    }

    @Override
    public PostDTO savePost(PostDTO postDTO) {
        Post post = postMapper.convertToPost(postDTO);
        Post savedPost = postRepository.save(post);
        return postMapper.convertToPostDTO(savedPost);
    }

    @Override
    public void updatePost(Long id, PostDTO postDTO) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        post.setTitle(postDTO.getTitle());
        post.setBody(post.getBody());
        post.setAuthorId(post.getAuthorId());
        postRepository.save(post);
    }

    @Override
    public void deletePost(Long id, Long userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        postRepository.delete(post);
        if(!post.getAuthorId().equals(userId)) {
            throw new RuntimeException("User not authorized to delete this comment");
        }
        postRepository.delete(post);
    }
}

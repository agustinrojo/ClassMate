package com.classmate.post_service;

import com.classmate.post_service.client.ICommentClient;
import com.classmate.post_service.dto.*;
import com.classmate.post_service.entity.Post;
import com.classmate.post_service.exception.InvalidPostException;
import com.classmate.post_service.mapper.IPostMapper;
import com.classmate.post_service.publisher.PostPublisher;
import com.classmate.post_service.repository.IPostRepository;
import com.classmate.post_service.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostServiceImplTest {

    @Mock
    private IPostRepository postRepository;

    @Mock
    private IPostMapper postMapper;

    @Mock
    private ICommentClient commentClient;

    @Mock
    private PostPublisher postPublisher;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * User Story CM-9: Crear post dentro de un foro
     * Probar crear un post, ingresando título y cuerpo, sin pasarse del máximo de caracteres definido (PASA)
     */
    @Test
    public void testSavePostWithValidTitleAndBody() {
        PostDTO postDTO = new PostDTO(null, 1L, 1L, "Valid title", "Valid body", null);
        Post post = new Post(null, 1L, 1L, "Valid title", "Valid body", null);

        when(postMapper.convertToPost(postDTO)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.convertToPostDTO(post)).thenReturn(postDTO);

        PostDTO savedPost = postService.savePost(postDTO);

        assertNotNull(savedPost);
        assertEquals("Valid title", savedPost.getTitle());
        assertEquals("Valid body", savedPost.getBody());
    }

    /**
     * User Story CM-9: Crear post dentro de un foro
     * Probar crear un post sin ingresar título, pero si cuerpo (FALLA)
     */
    @Test
    public void testSavePostWithoutTitle() {
        PostDTO postDTO = new PostDTO(null, 1L, 1L, "", "Valid body", null);

        InvalidPostException exception = assertThrows(InvalidPostException.class, () -> postService.savePost(postDTO));
        assertEquals("Post title cannot be empty", exception.getMessage());
    }

    /**
     * User Story CM-9: Crear post dentro de un foro
     * Probar crear un post sin ingresar cuerpo, pero si título (FALLA)
     */
    @Test
    public void testSavePostWithoutBody() {
        PostDTO postDTO = new PostDTO(null, 1L, 1L, "Valid title", "", null);

        InvalidPostException exception = assertThrows(InvalidPostException.class, () -> postService.savePost(postDTO));
        assertEquals("Post body cannot be empty", exception.getMessage());
    }

    /**
     * User Story CM-9: Crear post dentro de un foro
     * Probar crear un post, ingresando título y cuerpo, pasándose del máximo de caracteres definido para el título (FALLA)
     */
    @Test
    public void testSavePostWithTooLongTitle() {
        String longTitle = "a".repeat(301);
        PostDTO postDTO = new PostDTO(null, 1L, 1L, longTitle, "Valid body", null);

        InvalidPostException exception = assertThrows(InvalidPostException.class, () -> postService.savePost(postDTO));
        assertEquals("Post title cannot exceed 300 characters", exception.getMessage());
    }

    /**
     * User Story CM-9: Crear post dentro de un foro
     * Probar crear un post, ingresando título y cuerpo, pasándose del máximo de caracteres definido para el cuerpo (FALLA)
     */
    @Test
    public void testSavePostWithTooLongBody() {
        String longBody = "a".repeat(5001);
        PostDTO postDTO = new PostDTO(null, 1L, 1L, "Valid title", longBody, null);

        InvalidPostException exception = assertThrows(InvalidPostException.class, () -> postService.savePost(postDTO));
        assertEquals("Post body cannot exceed 5000 characters", exception.getMessage());
    }

    /**
     * User Story CM-19: Eliminar post de un foro
     * Probar clickear eliminar post y luego la confirmación (PASA)
     */
    @Test
    public void testDeletePost() {
        Long postId = 1L;
        Long userId = 1L; // The user attempting to delete the post
        Post post = new Post(postId, 1L, userId, "Valid title", "Valid body", null);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        doNothing().when(postRepository).delete(post);
        doNothing().when(postPublisher).publishPostDeletion(any(PostDeletionDTO.class));

        postService.deletePost(postId, userId);

        verify(postRepository, times(1)).delete(post);
        verify(postPublisher, times(1)).publishPostDeletion(any(PostDeletionDTO.class));
    }
    /**
     * User Story CM-20: Modificar post
     * Probar modificar un post, modificando el título, sin pasarse del máximo de caracteres definido (PASA)
     */
    @Test
    public void testUpdatePostWithValidTitle() {
        Long postId = 1L;
        PostUpdateDTO postUpdateDTO = new PostUpdateDTO("Updated title", "Valid body");
        Post post = new Post(postId, 1L, 1L, "Valid title", "Valid body", null);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.convertToPostDTO(post)).thenReturn(new PostDTO(postId, 1L, 1L, "Updated title", "Valid body", null));

        postService.updatePost(postId, postUpdateDTO);

        verify(postRepository, times(1)).save(post);
        assertEquals("Updated title", post.getTitle());
    }

    /**
     * User Story CM-20: Modificar post
     * Probar modificar un post, modificando el cuerpo, sin pasarse del máximo de caracteres definido (PASA)
     */
    @Test
    public void testUpdatePostWithValidBody() {
        Long postId = 1L;
        PostUpdateDTO postUpdateDTO = new PostUpdateDTO("Valid title", "Updated body");
        Post post = new Post(postId, 1L, 1L, "Valid title", "Valid body", null);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.convertToPostDTO(post)).thenReturn(new PostDTO(postId, 1L, 1L, "Valid title", "Updated body", null));

        postService.updatePost(postId, postUpdateDTO);

        verify(postRepository, times(1)).save(post);
        assertEquals("Updated body", post.getBody());
    }

    /**
     * User Story CM-20: Modificar post
     * Probar modificar un post, modificando el título y cuerpo, sin pasarse del máximo de caracteres definido (PASA)
     */
    @Test
    public void testUpdatePostWithValidTitleAndBody() {
        Long postId = 1L;
        PostUpdateDTO postUpdateDTO = new PostUpdateDTO("Updated title", "Updated body");
        Post post = new Post(postId, 1L, 1L, "Valid title", "Valid body", null);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.convertToPostDTO(post)).thenReturn(new PostDTO(postId, 1L, 1L, "Updated title", "Updated body", null));

        postService.updatePost(postId, postUpdateDTO);

        verify(postRepository, times(1)).save(post);
        assertEquals("Updated title", post.getTitle());
        assertEquals("Updated body", post.getBody());
    }

    /**
     * User Story CM-20: Modificar post
     * Probar modificar un post y no cambiar nada (PASA)
     */
    @Test
    public void testUpdatePostWithoutChangingAnything() {
        Long postId = 1L;
        PostUpdateDTO postUpdateDTO = new PostUpdateDTO("Valid title", "Valid body");
        Post post = new Post(postId, 1L, 1L, "Valid title", "Valid body", null);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postMapper.convertToPostDTO(post)).thenReturn(new PostDTO(postId, 1L, 1L, "Valid title", "Valid body", null));

        postService.updatePost(postId, postUpdateDTO);

        // Assert that nothing was changed
        verify(postRepository, times(1)).save(post);
        assertEquals("Valid title", post.getTitle());
        assertEquals("Valid body", post.getBody());
    }

    /**
     * User Story CM-20: Modificar post
     * Probar modificar un post sin ingresar título, pero si cuerpo (FALLA)
     */
    @Test
    public void testUpdatePostWithoutTitle() {
        Long postId = 1L;
        PostUpdateDTO postUpdateDTO = new PostUpdateDTO("", "Updated body");

        InvalidPostException exception = assertThrows(InvalidPostException.class, () -> postService.updatePost(postId, postUpdateDTO));
        assertEquals("Post title cannot be empty", exception.getMessage());
    }

    /**
     * User Story CM-20: Modificar post
     * Probar modificar un post sin ingresar cuerpo, pero si título (FALLA)
     */
    @Test
    public void testUpdatePostWithoutBody() {
        Long postId = 1L;
        PostUpdateDTO postUpdateDTO = new PostUpdateDTO("Updated title", "");

        InvalidPostException exception = assertThrows(InvalidPostException.class, () -> postService.updatePost(postId, postUpdateDTO));
        assertEquals("Post body cannot be empty", exception.getMessage());
    }

    /**
     * User Story CM-20: Modificar post
     * Probar modificar un post, ingresando título y cuerpo, pasándose del máximo de caracteres definido para el título (FALLA)
     */
    @Test
    public void testUpdatePostWithTooLongTitle() {
        Long postId = 1L;
        String longTitle = "a".repeat(301);
        PostUpdateDTO postUpdateDTO = new PostUpdateDTO(longTitle, "Updated body");

        InvalidPostException exception = assertThrows(InvalidPostException.class, () -> postService.updatePost(postId, postUpdateDTO));
        assertEquals("Post title cannot exceed 300 characters", exception.getMessage());
    }

    /**
     * User Story CM-20: Modificar post
     * Probar modificar un post, ingresando título y cuerpo, pasándose del máximo de caracteres definido para el cuerpo (FALLA)
     */
    @Test
    public void testUpdatePostWithTooLongBody() {
        Long postId = 1L;
        String longBody = "a".repeat(5001);
        PostUpdateDTO postUpdateDTO = new PostUpdateDTO("Updated title", longBody);

        InvalidPostException exception = assertThrows(InvalidPostException.class, () -> postService.updatePost(postId, postUpdateDTO));
        assertEquals("Post body cannot exceed 5000 characters", exception.getMessage());
    }

    /**
     * User Story CM-22: Ver post de un foro
     * Probar clickear post dentro de un foro (PASA)
     */
    @Test
    public void testGetPostById() {
        Long postId = 1L;
        Post post = new Post(postId, 1L, 1L, "Valid title", "Valid body", null);
        APIResponseDTO apiResponseDTO = new APIResponseDTO(postId, 1L, 1L, "Valid title", "Valid body", null, null);
        List<CommentDTOResponse> commentDTOS = Arrays.asList(
                new CommentDTOResponse(1L, postId, 1L, "Comment 1", null),
                new CommentDTOResponse(2L, postId, 1L, "Comment 2", null)
        );

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postMapper.convertToAPIResponseDTO(post)).thenReturn(apiResponseDTO);
        when(commentClient.getCommentsByPostId(postId, 0, 10)).thenReturn(commentDTOS);

        APIResponseDTO retrievedResponse = postService.getPostById(postId);

        assertNotNull(retrievedResponse);
        assertEquals("Valid title", retrievedResponse.getTitle());
        assertEquals("Valid body", retrievedResponse.getBody());
        assertNotNull(retrievedResponse.getCommentDTOS());
        assertEquals(2, retrievedResponse.getCommentDTOS().size());
        assertEquals("Comment 1", retrievedResponse.getCommentDTOS().get(0).getBody());
        assertEquals("Comment 2", retrievedResponse.getCommentDTOS().get(1).getBody());
    }
}

package com.classmate.comment_service;

import com.classmate.comment_service.dto.CommentDTO;
import com.classmate.comment_service.entity.Comment;
import com.classmate.comment_service.exception.InvalidCommentException;
import com.classmate.comment_service.exception.UnauthorizedActionException;
import com.classmate.comment_service.mapper.CommentMapper;
import com.classmate.comment_service.repository.CommentRepository;
import com.classmate.comment_service.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CommentServiceImpl.
 */
public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    /**
     * Set up the test environment.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    /**
     * Test adding a comment with a body less than 2000 characters.
     * User Story CM-10: Agregar comentario a Post
     * Probar agregar comentario con una cantidad de caracteres menor a 2000 (PASA).
     */
    @Test
    public void testSaveCommentWithValidBody() {
        CommentDTO commentDTO = new CommentDTO(null, 1L, 1L, "Valid comment body", null);
        Comment comment = new Comment(null, 1L, 1L, "Valid comment body", null);

        when(commentMapper.mapToComment(commentDTO)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.mapToCommentDTO(comment)).thenReturn(commentDTO);

        CommentDTO savedComment = commentService.saveComment(commentDTO);

        assertNotNull(savedComment);
        assertEquals("Valid comment body", savedComment.getBody());
    }

    /**
     * Test adding a comment with a body more than 2000 characters.
     * User Story CM-10: Agregar comentario a Post
     * Probar agregar comentario con una cantidad de caracteres mayor a 2000 (FALLA).
     */
    @Test
    public void testSaveCommentWithTooLongBody() {
        String longBody = "a".repeat(2001);
        CommentDTO commentDTO = new CommentDTO(null, 1L, 1L, longBody, null);

        InvalidCommentException exception = assertThrows(InvalidCommentException.class, () -> commentService.saveComment(commentDTO));
        assertEquals("Comment body cannot exceed 2000 characters", exception.getMessage());
    }

    /**
     * Test adding an empty comment.
     * User Story CM-10: Agregar comentario a Post
     * Probar agregar un comentario vacío (FALLA).
     */
    @Test
    public void testSaveCommentWithEmptyBody() {
        CommentDTO commentDTO = new CommentDTO(null, 1L, 1L, "", null);

        InvalidCommentException exception = assertThrows(InvalidCommentException.class, () -> commentService.saveComment(commentDTO));
        assertEquals("Comment body cannot be empty", exception.getMessage());
    }


    /**
     * Test selecting trash icon and confirming deletion of a comment.
     * User Story CM-11: Eliminar Comentario a Post
     * Probar seleccionar icono “basura” y confirmar el borrado del comentario (PASA).
     */
    @Test
    public void testDeleteComment() {
        Long commentId = 1L;
        Long userId = 1L; // The user attempting to delete the comment
        Comment comment = new Comment(commentId, 1L, userId, "Valid comment body", null);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        doNothing().when(commentRepository).delete(comment);

        commentService.deleteComment(commentId, userId);

        verify(commentRepository, times(1)).delete(comment);
    }

    /**
     * Test deleting a comment made by another user.
     * User Story CM-11: Eliminar Comentario a Post
     * Probar eliminar comentario realizado por otro usuario (FALLA).
     */
    @Test
    public void testDeleteCommentByAnotherUser() {
        Long commentId = 1L;
        Long anotherUserId = 2L; // ID of the user who did not create the comment
        Long authorId = 1L; // Actual author ID of the comment
        Comment comment = new Comment(commentId, 1L, authorId, "This is a comment by another user", null);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        UnauthorizedActionException exception = assertThrows(UnauthorizedActionException.class, () -> commentService.deleteComment(commentId, anotherUserId));
        assertEquals("User not authorized to delete this comment", exception.getMessage());

        verify(commentRepository, times(0)).delete(comment);
    }


    /**
     * Test selecting "edit" to edit a comment with a body less than 2000 characters.
     * User Story CM-12: Modificar Comentario a Post
     * Probar seleccionar “editar” para editar un comentario y este no posee más de 2000 caracteres (PASA).
     */
    @Test
    public void testUpdateCommentWithValidBody() {
        Long commentId = 1L;
        CommentDTO commentDTO = new CommentDTO(commentId, 1L, 1L, "Updated comment body", null);
        Comment comment = new Comment(commentId, 1L, 1L, "Valid comment body", null);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.mapToCommentDTO(comment)).thenReturn(commentDTO);

        commentService.updateComment(commentId, commentDTO);

        verify(commentRepository, times(1)).save(comment);
        assertEquals("Updated comment body", comment.getBody());
    }

    /**
     * Test selecting "edit" to edit a comment with a body more than 2000 characters.
     * User Story CM-12: Modificar Comentario a Post
     * Probar seleccionar “editar” para editar un comentario y este posee más de 2000 caracteres (FALLA).
     */
    @Test
    public void testUpdateCommentWithTooLongBody() {
        Long commentId = 1L;
        String longBody = "a".repeat(2001);
        CommentDTO commentDTO = new CommentDTO(commentId, 1L, 1L, longBody, null);

        InvalidCommentException exception = assertThrows(InvalidCommentException.class, () -> commentService.updateComment(commentId, commentDTO));
        assertEquals("Comment body cannot exceed 2000 characters", exception.getMessage());
    }

    /**
     * Test selecting "edit" to edit a comment with an empty body.
     * User Story CM-12: Modificar Comentario a Post
     * Probar seleccionar “editar” para editar un comentario y este no posee cuerpo (FALLA).
     */
    @Test
    public void testUpdateCommentWithEmptyBody() {
        Long commentId = 1L;
        CommentDTO commentDTO = new CommentDTO(commentId, 1L, 1L, "", null);

        InvalidCommentException exception = assertThrows(InvalidCommentException.class, () -> commentService.updateComment(commentId, commentDTO));
        assertEquals("Comment body cannot be empty", exception.getMessage());
    }


    /**
     * Test clicking "view comments" with less than or equal to 10 comments on the post and displaying that number of comments.
     * User Story CM-13: Ver comentario a Post
     * Probar hacer click en “ver comentarios”, habiendo menos de 10 comentarios en el post y se despliega esa cantidad de comentarios (PASA).
     */
    @Test
    public void testGetCommentsByPostIdWithLessOrEqualTo10Comments() {
        Long postId = 1L;
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            comments.add(new Comment((long) i, postId, 1L, "Comment " + i, null));
        }

        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> commentsPage = new PageImpl<>(comments, pageable, comments.size());

        when(commentRepository.findByPostId(postId, pageable)).thenReturn(commentsPage);
        when(commentMapper.mapToCommentDTO(any(Comment.class))).thenAnswer(invocation -> {
            Comment comment = invocation.getArgument(0);
            return new CommentDTO(comment.getId(), comment.getPostId(), comment.getAuthorId(), comment.getBody(), comment.getCreationDate());
        });

        List<CommentDTO> returnedComments = commentService.getCommentsByPostId(postId, 0, 10);

        assertNotNull(returnedComments);
        assertEquals(5, returnedComments.size()); // Should return 5 comments
    }

    /**
     * Test clicking "view comments" with more than 10 comments on the post and only 10 comments are displayed.
     * User Story CM-13: Ver comentario a Post
     * Probar hacer click en “ver comentarios”, habiendo más de 10 comentarios en el post y solo se despliegan 10 comentarios (Pasa).
     */
    @Test
    public void testGetCommentsByPostIdWithMoreThan10Comments() {
        Long postId = 1L;
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            comments.add(new Comment((long) i, postId, 1L, "Comment " + i, null));
        }

        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> commentsPage = new PageImpl<>(comments.subList(0, 10), pageable, comments.size());

        when(commentRepository.findByPostId(postId, pageable)).thenReturn(commentsPage);
        when(commentMapper.mapToCommentDTO(any(Comment.class))).thenAnswer(invocation -> {
            Comment comment = invocation.getArgument(0);
            return new CommentDTO(comment.getId(), comment.getPostId(), comment.getAuthorId(), comment.getBody(), comment.getCreationDate());
        });

        List<CommentDTO> returnedComments = commentService.getCommentsByPostId(postId, 0, 10);

        assertNotNull(returnedComments);
        assertEquals(10, returnedComments.size()); // Should return only 10 comments
    }

    /**
     * Test clicking "view comments" and nothing is displayed.
     * User Story CM-13: Ver comentario a Post
     * Probar hacer click en “ver comentarios” y no se despliega nada (PASA).
     */
    @Test
    public void testGetCommentsByPostIdWithNoComments() {
        Long postId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(commentRepository.findByPostId(postId, pageable)).thenReturn(emptyPage);

        List<CommentDTO> returnedComments = commentService.getCommentsByPostId(postId, 0, 10);

        assertNotNull(returnedComments);
        assertTrue(returnedComments.isEmpty());
    }
}

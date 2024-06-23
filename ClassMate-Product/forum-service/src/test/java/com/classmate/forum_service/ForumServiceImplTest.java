package com.classmate.forum_service;

import com.classmate.forum_service.client.IPostClient;
import com.classmate.forum_service.dto.APIResponseDTO;
import com.classmate.forum_service.dto.ForumResponseDTO;
import com.classmate.forum_service.dto.PostResponseDTO;
import com.classmate.forum_service.dto.create.ForumRequestDTO;
import com.classmate.forum_service.entity.Forum;
import com.classmate.forum_service.exception.InvalidForumException;
import com.classmate.forum_service.exception.UnauthorizedActionException;
import com.classmate.forum_service.mapper.IForumMapper;
import com.classmate.forum_service.repository.IForumRepository;
import com.classmate.forum_service.service.impl.ForumServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ForumServiceImplTest {

    @Mock
    private IForumRepository forumRepository;

    @Mock
    private IForumMapper forumMapper;

    @Mock
    private IPostClient postClient;

    @InjectMocks
    private ForumServiceImpl forumService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * User Story CM-3: Crear foro
     * Probar crear foro con todos los campos ingresados en condiciones (PASA)
     */
    @Test
    public void testSaveForumWithValidFields() {
        ForumRequestDTO forumRequestDTO = new ForumRequestDTO("Valid title", "Valid description");
        Forum forum = new Forum();
        forum.setTitle("Valid title");
        forum.setDescription("Valid description");
        forum.setCreatorId(1L);
        ForumResponseDTO forumResponseDTO = new ForumResponseDTO(1L, "Valid title", "Valid description", 1L, Collections.singletonList(1L), Collections.singletonList(1L), null);

        when(forumMapper.convertToForum(forumRequestDTO)).thenReturn(forum);
        when(forumRepository.save(forum)).thenReturn(forum);
        when(forumMapper.convertToForumResponseDTO(forum)).thenReturn(forumResponseDTO);

        ForumResponseDTO savedForum = forumService.saveForum(forumRequestDTO, 1L);

        assertNotNull(savedForum);
        assertEquals("Valid title", savedForum.getTitle());
        assertEquals("Valid description", savedForum.getDescription());
        assertEquals(1L, savedForum.getCreatorId());
    }

    /**
     * User Story CM-3: Crear foro
     * Probar crear foro sin haber ingresado titulo (FALLA)
     */
    @Test
    public void testSaveForumWithoutTitle() {
        ForumRequestDTO forumRequestDTO = new ForumRequestDTO("", "Valid description");

        InvalidForumException exception = assertThrows(InvalidForumException.class, () -> forumService.saveForum(forumRequestDTO, 1L));
        assertEquals("Forum title cannot be empty", exception.getMessage());
    }

    /**
     * User Story CM-3: Crear foro
     * Probar crear foro sin ingresar descripción (FALLA)
     */
    @Test
    public void testSaveForumWithoutDescription() {
        ForumRequestDTO forumRequestDTO = new ForumRequestDTO("Valid title", "");

        InvalidForumException exception = assertThrows(InvalidForumException.class, () -> forumService.saveForum(forumRequestDTO, 1L));
        assertEquals("Forum description cannot be empty", exception.getMessage());
    }

    /**
     * User Story CM-3: Crear foro
     * Probar crear foro sin ingresar imagen (PASA)
     */
    @Test
    public void testSaveForumWithoutImage() {
        // Image handling is not implemented yet, hence the regular test case without image field.
        ForumRequestDTO forumRequestDTO = new ForumRequestDTO("Valid title", "Valid description");
        Forum forum = new Forum();
        forum.setTitle("Valid title");
        forum.setDescription("Valid description");
        forum.setCreatorId(1L);
        ForumResponseDTO forumResponseDTO = new ForumResponseDTO(1L, "Valid title", "Valid description", 1L, Collections.singletonList(1L), Collections.singletonList(1L), null);

        when(forumMapper.convertToForum(forumRequestDTO)).thenReturn(forum);
        when(forumRepository.save(forum)).thenReturn(forum);
        when(forumMapper.convertToForumResponseDTO(forum)).thenReturn(forumResponseDTO);

        ForumResponseDTO savedForum = forumService.saveForum(forumRequestDTO, 1L);

        assertNotNull(savedForum);
        assertEquals("Valid title", savedForum.getTitle());
        assertEquals("Valid description", savedForum.getDescription());
        assertEquals(1L, savedForum.getCreatorId());
    }

    /**
     * User Story CM-3: Crear foro
     * Probar crear foro ingresando titulo con un numero de caracteres superior a 100 (FALLA)
     */
    @Test
    public void testSaveForumWithLongTitle() {
        String longTitle = "A".repeat(101);
        ForumRequestDTO forumRequestDTO = new ForumRequestDTO(longTitle, "Valid description");

        InvalidForumException exception = assertThrows(InvalidForumException.class, () -> forumService.saveForum(forumRequestDTO, 1L));
        assertEquals("Forum title cannot exceed 100 characters", exception.getMessage());
    }

    /**
     * User Story CM-3: Crear foro
     * Probar crear foro ingresando descripción con un numero de caracteres superior a 300 (FALLA)
     */
    @Test
    public void testSaveForumWithLongDescription() {
        String longDescription = "A".repeat(301);
        ForumRequestDTO forumRequestDTO = new ForumRequestDTO("Valid title", longDescription);

        InvalidForumException exception = assertThrows(InvalidForumException.class, () -> forumService.saveForum(forumRequestDTO, 1L));
        assertEquals("Forum description cannot exceed 300 characters", exception.getMessage());
    }

    /**
     * User Story CM-6: Eliminar foro
     * Probar eliminar foro siendo creador (PASA)
     */
    @Test
    public void testDeleteForumAsCreator() {
        Long forumId = 1L;
        Long creatorId = 1L;
        Forum forum = new Forum();
        forum.setId(forumId);
        forum.setCreatorId(creatorId);

        when(forumRepository.findById(forumId)).thenReturn(Optional.of(forum));

        assertDoesNotThrow(() -> forumService.deleteForum(forumId, creatorId));

        verify(forumRepository, times(1)).delete(forum);
    }

    /**
     * User Story CM-6: Eliminar foro
     * Probar eliminar foro sin ser creador (FALLA)
     */
    @Test
    public void testDeleteForumNotAsCreator() {
        Long forumId = 1L;
        Long creatorId = 1L;
        Long nonCreatorId = 2L;
        Forum forum = new Forum();
        forum.setId(forumId);
        forum.setCreatorId(creatorId);

        when(forumRepository.findById(forumId)).thenReturn(Optional.of(forum));

        UnauthorizedActionException exception = assertThrows(UnauthorizedActionException.class, () -> forumService.deleteForum(forumId, nonCreatorId));
        assertEquals("User not authorized to delete this forum", exception.getMessage());

        verify(forumRepository, never()).delete(forum);
    }

    /**
     * User Story CM-7: Modificar foro
     * Probar modificar un foro siguiendo un formato correcto (PASA)
     */
    @Test
    public void testUpdateForumWithValidData() {
        Long forumId = 1L;
        ForumRequestDTO forumRequestDTO = new ForumRequestDTO("Updated title", "Updated description");
        Forum forum = new Forum();
        forum.setId(forumId);
        forum.setTitle("Original title");
        forum.setDescription("Original description");

        when(forumRepository.findById(forumId)).thenReturn(Optional.of(forum));

        assertDoesNotThrow(() -> forumService.updateForum(forumId, forumRequestDTO));

        verify(forumRepository, times(1)).save(forum);
        assertEquals("Updated title", forum.getTitle());
        assertEquals("Updated description", forum.getDescription());
    }

    /**
     * User Story CM-7: Modificar foro
     * Probar modificar un foro sin ingresar un título (FALLA)
     */
    @Test
    public void testUpdateForumWithoutTitle() {
        Long forumId = 1L;
        ForumRequestDTO forumRequestDTO = new ForumRequestDTO("", "Updated description");

        InvalidForumException exception = assertThrows(InvalidForumException.class, () -> forumService.updateForum(forumId, forumRequestDTO));
        assertEquals("Forum title cannot be empty", exception.getMessage());
    }

    /**
     * User Story CM-7: Modificar foro
     * Probar modificar un foro ingresando un titulo con un numero de caracteres superior a 100 (FALLA)
     */
    @Test
    public void testUpdateForumWithLongTitle() {
        Long forumId = 1L;
        String longTitle = "A".repeat(101);
        ForumRequestDTO forumRequestDTO = new ForumRequestDTO(longTitle, "Updated description");

        InvalidForumException exception = assertThrows(InvalidForumException.class, () -> forumService.updateForum(forumId, forumRequestDTO));
        assertEquals("Forum title cannot exceed 100 characters", exception.getMessage());
    }

    /**
     * User Story CM-7: Modificar foro
     * Probar modificar un foro sin ingresar una descripción (FALLA)
     */
    @Test
    public void testUpdateForumWithoutDescription() {
        Long forumId = 1L;
        ForumRequestDTO forumRequestDTO = new ForumRequestDTO("Updated title", "");

        InvalidForumException exception = assertThrows(InvalidForumException.class, () -> forumService.updateForum(forumId, forumRequestDTO));
        assertEquals("Forum description cannot be empty", exception.getMessage());
    }

    /**
     * User Story CM-7: Modificar foro
     * Probar modificar un foro ingresando una descripción con un numero de caracteres superior a 300 (FALLA)
     */
    @Test
    public void testUpdateForumWithLongDescription() {
        Long forumId = 1L;
        String longDescription = "A".repeat(301);
        ForumRequestDTO forumRequestDTO = new ForumRequestDTO("Updated title", longDescription);

        InvalidForumException exception = assertThrows(InvalidForumException.class, () -> forumService.updateForum(forumId, forumRequestDTO));
        assertEquals("Forum description cannot exceed 300 characters", exception.getMessage());
    }

    /**
     * User Story CM-7: Modificar foro
     * Probar modificar un foro más de una vez (PASA)
     */
    @Test
    public void testUpdateForumMultipleTimes() {
        Long forumId = 1L;
        ForumRequestDTO firstUpdate = new ForumRequestDTO("First title", "First description");
        ForumRequestDTO secondUpdate = new ForumRequestDTO("Second title", "Second description");
        Forum forum = new Forum();
        forum.setId(forumId);
        forum.setTitle("Original title");
        forum.setDescription("Original description");

        when(forumRepository.findById(forumId)).thenReturn(Optional.of(forum));

        forumService.updateForum(forumId, firstUpdate);
        forumService.updateForum(forumId, secondUpdate);

        verify(forumRepository, times(2)).save(forum);
        assertEquals("Second title", forum.getTitle());
        assertEquals("Second description", forum.getDescription());
    }

    /**
     * User Story CM-6: Ver foro
     * Probar clickear nombre del foro (PASA)
     */
    @Test
    public void testGetForumById() {
        Long forumId = 1L;
        Forum forum = new Forum();
        forum.setId(forumId);
        forum.setTitle("Forum title");
        forum.setDescription("Forum description");
        List<PostResponseDTO> postDTOs = Arrays.asList(new PostResponseDTO(), new PostResponseDTO());
        APIResponseDTO apiResponseDTO = new APIResponseDTO();
        apiResponseDTO.setForum(new ForumResponseDTO());
        apiResponseDTO.setPosts(postDTOs);

        when(forumRepository.findById(forumId)).thenReturn(Optional.of(forum));
        when(forumMapper.convertToAPIResponseDTO(forum)).thenReturn(apiResponseDTO);
        when(postClient.getPostsByForumId(forumId, 0, 10)).thenReturn(postDTOs);

        APIResponseDTO response = forumService.getForumById(forumId);

        assertNotNull(response);
        assertNotNull(response.getForum());
        assertEquals(2, response.getPosts().size());
    }

    /**
     * User Story CM-53: Ver foros
     * Probar ver foros (PASA)
     */
    @Test
    public void testGetAllForums() {
        Forum forum1 = new Forum(1L, "Forum 1", 1L, "Description 1", new ArrayList<>(), new ArrayList<>(), null);
        Forum forum2 = new Forum(2L, "Forum 2", 2L, "Description 2", new ArrayList<>(), new ArrayList<>(), null);

        when(forumRepository.findAll()).thenReturn(Arrays.asList(forum1, forum2));
        when(forumMapper.convertToForumResponseDTO(forum1)).thenReturn(new ForumResponseDTO(1L, "Forum 1", "Description 1", 1L, new ArrayList<>(), new ArrayList<>(), null));
        when(forumMapper.convertToForumResponseDTO(forum2)).thenReturn(new ForumResponseDTO(2L, "Forum 2", "Description 2", 2L, new ArrayList<>(), new ArrayList<>(), null));

        List<ForumResponseDTO> forums = forumService.getAllForums();

        assertNotNull(forums);
        assertEquals(2, forums.size());
        assertEquals("Forum 1", forums.get(0).getTitle());
        assertEquals("Forum 2", forums.get(1).getTitle());
    }
}

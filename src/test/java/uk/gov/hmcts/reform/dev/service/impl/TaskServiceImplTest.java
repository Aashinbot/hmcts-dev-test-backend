package uk.gov.hmcts.reform.dev.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uk.gov.hmcts.reform.dev.exception.InvalidInputException;
import uk.gov.hmcts.reform.dev.exception.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.models.ExampleCase;
import uk.gov.hmcts.reform.dev.models.Status;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private ExampleCase sampleCase;

    @BeforeEach
    void setup() {
        sampleCase = new ExampleCase("ABC123", "TitleTest", "Desc", Status.NotStarted, LocalDateTime.now());
    }

    @Test
    void testCreateTask_validInput() {
        when(taskRepository.save(any(ExampleCase.class))).thenReturn(sampleCase);

        assertDoesNotThrow(() -> taskService.createTask("Valid Title", Status.NotStarted, "desc", "17/06/2025"));
        verify(taskRepository, times(1)).save(any(ExampleCase.class));
    }

    @Test
    void testCreateTask_invalidTitle() {
        Exception e = assertThrows(InvalidInputException.class, () ->
            taskService.createTask("No", Status.NotStarted, "desc", "17/06/2025")
        );
        assertTrue(e.getMessage().contains("title needs to be"));
    }

    @Test
    void testCreateTask_invalidDateFormat() {
        Exception e = assertThrows(InvalidInputException.class, () ->
            taskService.createTask("Valid Title", Status.NotStarted, "desc", "2025-06-17")
        );
        assertTrue(e.getMessage().contains("format dd/MM/yyyy"));
    }

    @Test
    void testFindTaskById_found() {
        when(taskRepository.findById("1")).thenReturn(Optional.of(sampleCase));

        ExampleCase result = taskService.findTaskById(1);
        assertEquals("TitleTest", result.getTitle());
    }

    @Test
    void testFindTaskById_notFound() {
        when(taskRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.findTaskById(99));
    }

    @Test
    void testFindAllTasks_paginated() {
        Page<ExampleCase> mockPage = new PageImpl<>(List.of(sampleCase));
        Pageable pageable = PageRequest.of(0, 10);

        when(taskRepository.findAll(pageable)).thenReturn(mockPage);

        Page<ExampleCase> result = taskService.findAllTasks(pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testUpdateTask_success() {
        ExampleCase updated = new ExampleCase("ABC999", "NewTitle", "NewDesc", Status.Completed, LocalDateTime.now());

        when(taskRepository.findById("1")).thenReturn(Optional.of(sampleCase));
        when(taskRepository.save(any())).thenReturn(updated);

        ExampleCase result = taskService.updateTask(1, updated);

        assertEquals("NewTitle", result.getTitle());
        assertEquals(Status.Completed, result.getStatus());
    }

    @Test
    void testUpdateTask_notFound() {
        when(taskRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () ->
            taskService.updateTask(999, sampleCase)
        );
    }

    @Test
    void testDeleteTask_exists() {
        when(taskRepository.existsById("1")).thenReturn(true);
        doNothing().when(taskRepository).deleteById("1");

        assertDoesNotThrow(() -> taskService.deleteTask(1));
    }

    @Test
    void testDeleteTask_notFound() {
        when(taskRepository.existsById("999")).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () ->
            taskService.deleteTask(999)
        );
    }
}

package uk.gov.hmcts.reform.dev.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.dev.exception.InvalidInputException;
import uk.gov.hmcts.reform.dev.models.ExampleCase;
import uk.gov.hmcts.reform.dev.models.Status;
import uk.gov.hmcts.reform.dev.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;


@ExtendWith(MockitoExtension.class)
class CaseControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private CaseController caseController;

    @Test
    void testCreateTask_success() {
        doNothing().when(taskService).createTask(anyString(), any(), anyString(), anyString());

        ResponseEntity<String> response = caseController.createTask("Test", Status.NotStarted, "desc", "2025-06-30");

        assertEquals(CREATED, response.getStatusCode());
        assertEquals("Task Created", response.getBody());
    }

    @Test
    void testCreateTask_invalidInput() {
        doThrow(new InvalidInputException("Invalid date")).when(taskService)
            .createTask(anyString(), any(), anyString(), anyString());

        ResponseEntity<String> response = caseController.createTask("Test", Status.NotStarted, "desc", "bad-date");

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid date", response.getBody());
    }

    @Test
    void testGetExampleCase() {
        ResponseEntity<ExampleCase> response = caseController.getExampleCase();

        assertEquals(OK, response.getStatusCode());
        assertEquals("ABC12345", Objects.requireNonNull(response.getBody()).getCaseNumber());
    }

    @Test
    void testGetRequestById_success() {
        ExampleCase example = new ExampleCase(1, "X", "title", "desc", Status.InProgress, LocalDateTime.now());
        when(taskService.findTaskById(1)).thenReturn(example);

        ResponseEntity<ExampleCase> response = caseController.getRequestById(1);

        assertEquals(ACCEPTED, response.getStatusCode());
        assertEquals("X", Objects.requireNonNull(response.getBody()).getCaseNumber());
    }

    @Test
    void testGetRequestById_invalid() {
        when(taskService.findTaskById(99)).thenThrow(new InvalidInputException("Not found"));

        ResponseEntity<ExampleCase> response = caseController.getRequestById(99);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetAllTasks_paginated() {
        ExampleCase ec = new ExampleCase(1, "X", "title", "desc", Status.InProgress, LocalDateTime.now());
        Page<ExampleCase> page = new PageImpl<>(List.of(ec));

        Pageable pageable = PageRequest.of(0, 10);
        when(taskService.findAllTasks(pageable)).thenReturn(page);

        Page<ExampleCase> result = caseController.getAllTasks(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("X", result.getContent().get(0).getCaseNumber());
    }

    @Test
    void testUpdateTask_success() {
        ExampleCase input = new ExampleCase(1, "CASE-1", "Updated", "desc", Status.Completed, LocalDateTime.now());
        when(taskService.updateTask(eq(1), any())).thenReturn(input);

        ResponseEntity<ExampleCase> response = caseController.updateTask(1, input);

        assertEquals(OK, response.getStatusCode());
        assertEquals("Updated", Objects.requireNonNull(response.getBody()).getTitle());
    }

    @Test
    void testDeleteTask_success() {
        doNothing().when(taskService).deleteTask(1);

        ResponseEntity<String> response = caseController.deleteTask(1);

        assertEquals(OK, response.getStatusCode());
        assertEquals("Task deleted successfully", response.getBody());
    }

    @Test
    void testDeleteTask_notFound() {
        doThrow(new RuntimeException("Not found")).when(taskService).deleteTask(999);

        ResponseEntity<String> response = caseController.deleteTask(999);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Task with ID 999 not found"));
    }
}


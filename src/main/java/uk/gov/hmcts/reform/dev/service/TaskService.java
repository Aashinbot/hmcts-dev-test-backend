package uk.gov.hmcts.reform.dev.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.gov.hmcts.reform.dev.models.ExampleCase;
import uk.gov.hmcts.reform.dev.models.Status;

public interface TaskService {
    void createTask(String title, Status status, String description, String dueDate);

    ExampleCase findTaskById(int id);

    Page<ExampleCase> findAllTasks(Pageable pageable);

    ExampleCase updateTask(int id, ExampleCase updatedTask);

    void deleteTask(int id);
}

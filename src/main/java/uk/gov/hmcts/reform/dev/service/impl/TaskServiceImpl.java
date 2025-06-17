package uk.gov.hmcts.reform.dev.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.exception.InvalidInputException;
import uk.gov.hmcts.reform.dev.exception.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.models.ExampleCase;
import uk.gov.hmcts.reform.dev.models.Status;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;
import uk.gov.hmcts.reform.dev.service.TaskService;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class TaskServiceImpl implements TaskService {


    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void createTask(String title, Status status, String description, String dueDate) {
        boolean validated = validateInput(title, status, dueDate);

        if (validated) {
            String caseN = createCaseNumber(title);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(dueDate, formatter);

            taskRepository.save(new ExampleCase(caseN, title, description, status, date.atStartOfDay()));

        }

    }

    @Override
    public ExampleCase findTaskById(int id) {
        return taskRepository.findById(String.valueOf(id))
            .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    @Override
    public Page<ExampleCase> findAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public ExampleCase updateTask(int id, ExampleCase updatedTask) {
        ExampleCase existing = taskRepository.findById(String.valueOf(id))
            .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        existing.setCaseNumber(updatedTask.getCaseNumber());
        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setStatus(updatedTask.getStatus());

        return taskRepository.save(existing);
    }

    @Override
    public void deleteTask(int id) {

        if (!taskRepository.existsById(String.valueOf(id))) {
           throw new TaskNotFoundException("Not found");
        }
        else{
            taskRepository.deleteById(String.valueOf(id));
        }
    }


    private String createCaseNumber(String title) {
        SecureRandom secureRandom = new SecureRandom();
        if (title.length() >= 3) {
            String firstThree = title.substring(0, 3);
            int threeNumbs = secureRandom.nextInt(999) +100;
            return firstThree + threeNumbs;
        } else {
            return "RAND"+(secureRandom.nextInt(100) + 100 );
        }
    }

    private boolean validateInput(String title,Status status, String dueDate) {
        if(title != null && status != null && dueDate != null) {
            if ( title.length() >= 100  || title.length() <= 5) {
                throw new InvalidInputException("title needs to be between 5 and 100 chars");
            }
            else if (!dueDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
                throw new InvalidInputException("Need to be in the format dd/MM/yyyy");
            } else {
                return true;
            }
        }
        return false;
    }

}

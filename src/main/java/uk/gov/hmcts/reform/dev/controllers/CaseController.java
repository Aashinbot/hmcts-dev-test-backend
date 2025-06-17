package uk.gov.hmcts.reform.dev.controllers;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dev.exception.InvalidInputException;
import uk.gov.hmcts.reform.dev.models.ExampleCase;
import uk.gov.hmcts.reform.dev.models.Status;
import uk.gov.hmcts.reform.dev.service.TaskService;

import java.time.LocalDateTime;

import static org.springframework.http.ResponseEntity.*;

@RestController
public class CaseController {

    private final TaskService taskService;

    @Autowired
    public CaseController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(value = "/create-task")
    public ResponseEntity<String> createTask(@RequestParam String title, @RequestParam Status status,
                                             @RequestParam String description, @RequestParam String dueDate) {

        try{
            taskService.createTask(title,status,description,dueDate);

            return ResponseEntity.status(HttpStatus.CREATED).body("Task Created");
        }
        catch (InvalidInputException e){

           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
        }
    }
    @GetMapping(value = "/get-example-case", produces = "application/json")
    public ResponseEntity<ExampleCase> getExampleCase() {
        return ok(new ExampleCase(1, "ABC12345", "Case Title",
                                  "Case Description", Status.NotStarted, LocalDateTime.now()
        ));
    }

    @GetMapping(value = "/get-task-by-id")
    public ResponseEntity<ExampleCase> getRequestById(@RequestParam int id){
        try{
            ExampleCase exampleCase = taskService.findTaskById(id);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(exampleCase);
        }
        catch (InvalidInputException e){

            return badRequest().body(null);
        }
    }

    @GetMapping(value = "/get-all-tasks")
    public Page<ExampleCase> getAllTasks(@PageableDefault (size = 5,sort = "id") Pageable pageable){

        return taskService.findAllTasks(pageable);
    }

    @PutMapping("/update-task-by-id")
    public ResponseEntity<ExampleCase> updateTask(
        @RequestParam int id,
        @Valid @RequestBody ExampleCase updatedTask
    ) {

        ExampleCase exampleCaseSaved = taskService.updateTask(id, updatedTask);
        return ResponseEntity.ok(exampleCaseSaved);
    }

    @DeleteMapping("/delete-task-by-id")
    public ResponseEntity<String> deleteTask(@RequestParam int id) {

        try {
            taskService.deleteTask(id);
            return ok("Task deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Task with ID " + id + " not found");
        }

    }

}

package uk.gov.hmcts.reform.dev.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document("Tasks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExampleCase {

    @Id
    private int id;
    private String caseNumber;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime createdDate;

    public ExampleCase(String caseNumber, String title, String description, Status status, LocalDateTime createdDate) {
        this.caseNumber = caseNumber;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
    }
}

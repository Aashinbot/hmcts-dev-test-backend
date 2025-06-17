package uk.gov.hmcts.reform.dev.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.gov.hmcts.reform.dev.models.ExampleCase;

public interface TaskRepository extends MongoRepository<ExampleCase,String> {

}

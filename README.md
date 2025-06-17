# hmcts-dev-test-backend

This is the backend system test that has been assigned, in it, I have Fufilled the requirements that where set up in the task, There exists new APIs that accomplish the task that was set forward, 
I will explain in detail how they where made and their APIs.

To preface, I have used a MongoDB as my database, and due to the username and password being sensative as well as private information, I regret to inform you that I cannot share those details. However, in the code,
you can see how I chose to implement the MongoDB in my code. 

1.  Create a task with the following properties:
        Title
        Description (optional field)
        Status
        Due date/time
This has been achieved through the API /create-tasks, where it requries these fields. The input has been validated and once correct, will safely store it inside the DB.
4 Parameters:- Title, Description, Status, due Date

3.    Retrieve a task by ID
This has been achived by the API /get-task-by-id, where it will try to find the task, throwing a custom exception if not found. 1 input, ID

4.    Retrieve all tasks
This has been achived through the API /get-all-tasks, it will get a paged version of the tasks to handle large data, this is incase there are millions of tasks that might exist.

5.    Update the status of a task
This has been achived through the API /update-task-by-id, it will update the task of the ID that has been provided. Parameters:= Id, and Task
6.    Delete a task
This has been achieved through the API delete-tasks-by-id, it will delete the task of the ID that you have provided. Parameters: Id

All units created has been UNIT tested, other tests can only be completed with the entire system is available to fully ensure it is working.

Hopefully this is suffiecent,

# Workshop-cicd-lab2

## Phase 2

Based on Phase 1 code, which implements a simple Monolithic ToDoList REST API with 3 services, ToDo List service, User service and Notification service, it is necessary to change the structure of the solution towards a Microservice architecture:
- Use a message broker to communicate between services (a loosely coupled communication strategy);
- Use a separate database for each service;
- Expose only the necessary endpoints outside the Microservice APP.

The base code for this lab already has a separation of each service, and the ToDo List and User services are implemented. It is ** only necessary ** to make small changes to the Notification service. Just find the ** TODO **.

Please follow the guide to follow the necessary steps to:
- Make message communication between services;
- Implement a resilient solution for communication;
- Implement a one-command deployment solution as part of a full automation process.
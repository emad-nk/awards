## Employee directory organization

This is an application for managing employees of a company. Employees belong to organizations within the company.

As recognition, employees can receive Awards.

---------

## Endpoints

Caching is used for the endpoint that gets all the employees. If save/delete happens on the employee table the cache gets evicted.

Swagger endpoint: http://localhost:3000/swagger-ui/index.html

## Tests

Integration tests have the naming convention to end with `*IT.java` and unit tests have the naming convention to end with `*Test.java`.

## Starting the application

First call `./start-deps.sh` to start the dependencies, then `StartApplication` in the **test** package can be called to start the application which starts the application with `local` profile.

For running tests, the docker dependencies should be running `./start-deps.sh`.

To stop the dependencies call `./stop-deps.sh`

### Running the app via gradle
First call `./start-deps.sh` to start the dependencies then use the following gradle commands:
```
./gradlew build
./gradlew run
```
**NOTE:** Running application via gradle won't run the `DataLoader` to add data to the DB, since DataLoader has been moved to the test package. If you need data to be loaded in the DB please use `StartApplication` in the test package.


## Tech Stack

- Spring boot 3.x
- Java 17
- Postgres
- Redis

## Future Development

- A controller for the `organization`, so organizations can be created/updated/deleted via endpoints.
- Refactor `IndexController` similar to the `EmployeeController` to not access repositories directly but access via different services.
- Separate services for Activity and Organization
- Sending metrics of failures/successes
- More test coverage
- Workflow diagram

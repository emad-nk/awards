# Implementation details

A service layer has been created so the controller can access it instead of accessing repositories directly and having the whole business logic in the controller.

The purpose of message broker was unclear within the application, so improvised its behaviour to show it is running on a separate non-blocking thread for long-running processes. Therefore, after each CRUD operation, an asynchronous event is sent via `EventPublisher` to create activities or update the awards cache when applicable.

Got rid of in memory cache in `AwardsCache` and replaced it with Redis. That brings it closer to the production code, and in case an instance goes down the cache does not get removed.

Got rid of H2 database and replaced it with Postgres to use proper database migration and bring the application closer to the production code.

Used Lombok to get rid of a lot of boilerplate code.

So many things have been refactored, which can be discussed later during the meeting.

Due to time constraints some parts/refactorings have been skipped, please read the [Future Development](#future-development) section for more details.

## Endpoints

Caching is used for the endpoint that gets all the employees. If save/delete happens on the employee table the cache gets evicted.

Swagger endpoint: http://localhost:3000/swagger-ui/index.html

## Tests

I've tried to put as many tests as possible, however tests are covering most important aspects of the code, and the coverage is not 100% due to time constraint.

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


---------
## Employee directory organization

This is an application for managing employees of a company. Employees belong to organizations within the company.

As recognition, employees can receive Awards.

## Instructions

In preparation for the upcoming call with NinjaOne, `clone` this repo and run it locally. If everything runs successfully, you will see the following page in your browser.

![success](success.png)

Become familiar with the application and it's characteristics. Use your favorite HTTP Client (like [Postman](https://www.postman.com/)) to exercise the endpoints and step through the code to help you get to know the application.

In the call, we will introduce new code to the application, and you will comment on issues with the endpoint. Please be ready to share your screen in the call with us with the application ready to run.

**Bonus:** Spot any issues or potential improvements you notice in the application while you're familiarizing yourself and make note of them for our call. We would love to see your input in how to make this application better.

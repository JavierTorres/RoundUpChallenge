# Round-Up transactions to Savings Goals

## Table of Contents

- [Features](#features)
- [Description](#description)
- [Notes](#notes)
- [CMD](#cmd)

---

## Features

- Java 17
- Maven
- Non-blocking reactive streams
- Hexagonal Architecture
- SprintBoot 3
- Unit Tests
- Integration Test
- Mockito
- Wiremock
- Lombok

---

## Description

This is a MVP microservice to connect to the Starling public api to take all the OUT not internal transactions for a primary account and round them up to the nearest
pound and then be transferred into a savings goal.

### Assumptions
The following are just assumptions, however they could change if discussing.

1. Only the transactions with `OUT` for the `direction` are been taken for the round-up
2. The transactions `INTERNAL_TRANSFER` for the `source` are not been taken for the round-up to avoid the transfers to the savings goal.
3. The business logic to refresh or get the authorization token to connect to the Starling API is not part of this exercise. It will be pass as an environment variable when running the application.

## Technical details

### Hexagonal Architecture

Hexagonal architecture approach has been taken in order to divide the code base into several loosely-coupled interchangeable components, such as the application core, domain and the infrastructure.

- `application`: It contains just a single use case to run the roundUp.
- `domain`: It contains all the business logic without having any dependency with the application or the infrastructure.
- `infrastructure`: It contains adapters to specific Starling API and the controller to trigger this application.

### Non-blocking reactive streams

Blocking threads consume resources. For latency-sensitive workloads which need to handle a large number of concurrent requests, the non-blocking async programming model is more efficient, that's why `Spring WebFlux` is being used. Also, this is particularly relevant for APIs, and generally for scenarios dealing with multiple requests.

## Testing

It contains Unit tests `UTest` file to test individually each component

There are also `ITest` to run an integration test fully integrated with Wiremock


## CMD

### Clean, Build and run tests

```
./mvnw clean install
```

### Run

Export a valid Authorization token to the environment variable called `AUTHORIZATION_BEARER`, please only the token without `Bearer` prefix
```
export AUTHORIZATION_BEARER=REPLACE
```

Then, run the application
```
java -Denv_var=$AUTHORIZATION_BEARER -jar target/StarlingAPIChallenge-0.0.1-SNAPSHOT.jar
```

Finally, run a POST to trigger the round-up, for example

```
curl --location 'http://localhost:8080/round-up' \
--header 'Content-Type: application/json' \
--data '{
    "minTransactionTimestamp": "2023-02-01T23:42:40.592Z",
    "maxTransactionTimestamp": "2023-02-19T23:42:40.592Z"
}'
```

## Future improvements

- Replace Authorization Bearer pass as a enviorment variable with the appropiate logic to get it automatically.
- Dockerizer in order to run as a container
- Add observability.

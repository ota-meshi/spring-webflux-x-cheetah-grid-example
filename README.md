# Cheetah Grid × Spring WebFlux

Sample implementation using [Cheetah Grid](https://github.com/future-architect/cheetah-grid) and Spring WebFlux.

## Setup

```bash
npm ci
```

## Start Server

```bash
npm run build
mvn clean compile spring-boot:run
```

App runs at `http://localhost:8080/`.

## Build `jar`

```bash
npm run build
mvn clean package
```

A jar file is created in `target/spring-webflux-x-cheetah-grid-example-1.0.0-SNAPSHOT.jar`.

## Launch App with `jar`

```bsah
java -jar target/spring-webflux-x-cheetah-grid-example-1.0.0-SNAPSHOT.jar
```

## Infomations

### DB data location

H2 database files are in `target/db/app.mv.db`.

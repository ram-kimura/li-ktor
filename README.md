# li-ktor

This project was created using the [Ktor Project Generator](https://start.ktor.io).

Here are some useful links to get you started:

- [Ktor Documentation](https://ktor.io/docs/home.html)
- [Ktor GitHub page](https://github.com/ktorio/ktor)
- The [Ktor Slack chat](https://app.slack.com/client/T09229ZC6/C0A974TJ9). You'll need to [request an invite](https://surveys.jetbrains.com/s3/kotlin-slack-sign-up) to join.

## Features

Here's a list of features included in this project:

| Name                                               | Description                                                 |
| ----------------------------------------------------|------------------------------------------------------------- |
| [Routing](https://start.ktor.io/p/routing-default) | Allows to define structured routes and associated handlers. |

## Building & Running

To build or run the project, use one of the following tasks:

| Task                          | Description                                                          |
| -------------------------------|---------------------------------------------------------------------- |
| `./gradlew test`              | Run the tests                                                        |
| `./gradlew build`             | Build everything                                                     |
| `buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `buildImage`                  | Build the docker image to use with the fat JAR                       |
| `publishImageToLocalRegistry` | Publish the docker image locally                                     |
| `run`                         | Run the server                                                       |
| `runDocker`                   | Run using the local docker image                                     |

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

## JDBC URL
```markdown
jdbc:postgresql://localhost:5432/li?currentSchema=li
```

## todo: make migrate file
```sql
create database li;
create schema IF NOT EXISTS li;
set schema 'li';

create table IF NOT EXISTS tenant
(
    tenant_name_id    varchar(20)              not null
            primary key,
    organization_name varchar(100)             not null,
    created_at        timestamp with time zone not null
);

create table IF NOT EXISTS task
(
    tenant_name_id varchar(20)            not null,
        foreign key (tenant_name_id) references tenant (tenant_name_id),
    task_uuid      uuid              not null
            primary key,
    title    varchar(100)             not null,
    priority  varchar(20)             not null,
    completed_at timestamp with time zone,
    created_at   timestamp with time zone not null,
    updated_at   timestamp with time zone 
);

create index IF NOT EXISTS task_uuid_index on task (task_uuid);
```

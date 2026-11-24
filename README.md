# minidocdb

A small document-oriented NoSQL database, written from scratch in Java on top of the plain file system, and meant to run on Kubernetes.

This is our project for the DataOps course (ENSEIRB-MATMECA, 3rd year, Software Engineering). The goal is simple to state and less simple to do: everything that does the actual work of a database (storage, indexes, partitioning, replication) we write ourselves. Everything around it (JSON parsing, the HTTP layer, Docker, Kubernetes, tests) we're allowed to pick off the shelf. We test it with the Stack Exchange dataset we used in the earlier labs.

## Ground rules

What we use:

- Java 17 and its standard library
- Jackson, only to read and write JSON
- a library to expose the database over the network (REST API), once we get there
- JUnit 5 for tests, Docker, standard Kubernetes objects, GitHub Actions

What we don't use: any existing database, even an embedded one (SQLite, RocksDB, Redis, MongoDB...), any library that manages indexes or queries data for us, pre-indexed file formats or dataframe libraries, and no shared volumes or off-the-shelf file sync for replication and sharding.

## Roadmap

The project is built in six steps. We'll tick them off as we go.

- [ ] **1. CRUD on disk.** A program (CLI or Java API) that handles CREATE, READ, UPDATE and DELETE and writes documents to a file on disk, one JSON document per line. That file is our "table". We measure how much data a single lookup has to read, which gives us a baseline for the next steps.
- [ ] **2. Indexes.** A function that builds an index on a given field and stores it in its own file, loosely modeled on what MongoDB does. Reads go through the index, writes keep it up to date. Same measurement as step 1, with and without the index.
- [ ] **3. Partitioning.** The table is split across several files based on a partition key, so a query that filters on that key only reads the matching partition. Open questions we'll answer here: which field to use as the key, whether to split by value, by range or by hash, and what happens to queries that don't filter on the key. We measure data read with and without partitioning.
- [ ] **4. Docker image.** Data has to survive a container restart, and instances need a way to talk to each other (most likely a REST API). Bonus: the image is built and the tests run in GitHub Actions.
- [ ] **5. Replication (and sharding as a bonus).** Several instances run side by side. Every write on one instance is copied to the others, so losing an instance loses no data. Bonus: put the partitions from step 3 on different instances and route each request to the right one.
- [ ] **6. Kubernetes.** Manifests (in `k8s/`) that deploy the database across several nodes.

## Project layout

```
minidocdb/
├── pom.xml                  Maven build (Java 17, Jackson, JUnit 5)
├── Dockerfile               container image (step 4)
├── k8s/                     Kubernetes manifests (step 6)
└── src/
    ├── main/java/com/minidocdb/
    │   └── DocumentStore.java
    └── test/java/com/minidocdb/
        └── DocumentStoreTest.java
```

Data files (`data/`, `*.jsonl`, `*.idx`) are git-ignored, so nothing generated at runtime ends up in the repo.

## Getting started

You'll need JDK 17+ and Maven.

```bash
# build and run the tests
mvn test

# package the jar
mvn package
```

Instructions for running the CLI, the Docker image and the Kubernetes deployment will be added here as each step lands.

## Presentation

The project will be demoed on December 8, 2026. The repository is frozen after the presentation: no commits after that date.

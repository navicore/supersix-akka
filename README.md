[![Build Status](https://travis-ci.org/navicore/supersix-akka.svg?branch=master)](https://travis-ci.org/navicore/supersix-akka)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6be03bf590ad4a5b88c2598b4456cb4f)](https://www.codacy.com/app/navicore/supersix-akka?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=navicore/supersix-akka&amp;utm_campaign=Badge_Grade)

Supersix Demo App for Akka Actor Stream Enrichment
===

# UNDER CONSTRUCTION

# UNDER CONSTRUCTION

# UNDER CONSTRUCTION

Processes json-formatted a Source and writes enriched versions to a Sink.

### To Run

* run Kafka with Docker
```
docker run -d --name my-kafka -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=`ifconfig | sed -En 's/127.0.0.1//;s/.*inet (addr:)?(([0-9]*\.){3}[0-9]*).*/\2/p'` --env ADVERTISED_PORT=9092 spotify/kafka
```

* run Cassandra with Docker
```
docker run -p 9042:9042 --name my-cassandra -d cassandra:3.11
```

`sbt run`


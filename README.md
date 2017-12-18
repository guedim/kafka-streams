# kafka-streams

This is a simple project to install, and set up  [Kafka](https://kafka.apache.org) and [Zookeeper](https://zookeeper.apache.org).
Also install [Kafka Manger](https://github.com/yahoo/kafka-manager) and (Zoonavigator)[https://github.com/elkozmon/zoonavigator] for monitoring the kafka cluster.

> This is a simple project using docker, to use kafka streams.

# Table of contents
1. [Install](#install)
2. [Configuration](#configuration)
3. [Monitoring](#monitoring)
4. [References](#references)


### Install services<a name="install"></a>

Download the [docker-compose](https://docs.docker.com/compose/) file:

```sh
wget https://raw.githubusercontent.com/guedim/kafka-streams/master/docker-compose.yml
```

Later, start the services using [docker-compose](https://docs.docker.com/compose/) file:
```sh
docker-compose up
```

### Configuration<a name="configuration"></a>

Temporal

KafkaManager:

```sh
localhost:9000
```

Zoonavigator:

```sh
localhost:9001
```


### Monitoring<a name="monitoring"></a>

For Monitoring, enter to [Grafana](http://grafana.com) and review the Metrix dashboard.

> Open the Grafana service and user the next credentials: 
>  - **user:** admin
>  - **password:** foobar


### References<a name="references"></a>

* https://kafka.apache.org/0110/documentation/streams/developer-guide#streams_processor 
* https://kafka.apache.org/documentation/streams/
* https://naspers.udemy.com/kafka-streams/
* https://docs.confluent.io/current/streams/developer-guide/index.html#stateless-transformations
* https://docs.confluent.io/current/streams/developer-guide/dsl-api.html#stateful-transformations
* 

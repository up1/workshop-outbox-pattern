# Workshop with Outbox pattern
* Java
* Debezium and PostgreSQL

## Step 1 :: Build debezium connect with custom transformer
```
$docker compose build debezium-connect
```

## Step 2 :: Start PostgreSQL Database
```
$docker compose up -d postgres

NAME                IMAGE               COMMAND                  SERVICE             CREATED             STATUS              PORTS
postgres            debezium/postgres   "docker-entrypoint.s…"   postgres            6 seconds ago       Up 5 seconds        0.0.0.0:5432->5432/tcp
```

Create database = `demodb`

## Step 3 :: Start Apache Kafka
```
$docker compose up -d kafka
$docker compose logs --follow
$docker compose ps

NAME                IMAGE                   COMMAND                  SERVICE             CREATED             STATUS              PORTS
kafka               confluentinc/cp-kafka   "/etc/confluent/dock…"   kafka               5 seconds ago       Up 3 seconds        0.0.0.0:9092->9092/tcp, 0.0.0.0:29092->29092/tcp
postgres            debezium/postgres       "docker-entrypoint.s…"   postgres            26 seconds ago      Up 25 seconds       0.0.0.0:5432->5432/tcp
zookeeper           zookeeper               "/docker-entrypoint.…"   zookeeper           5 seconds ago       Up 4 seconds        2888/tcp, 3888/tcp, 0.0.0.0:2181->2181/tcp, 8080/tcp
```

## Step 4 :: Start Debezium Connect with with custom transformer
```
$docker compose up -d debezium-connect

$docker compose ps

NAME                      IMAGE                     COMMAND                  SERVICE             CREATED              STATUS              PORTS
custom-debezium-connect   custom-debezium-connect   "/docker-entrypoint.…"   debezium-connect    6 seconds ago        Up 5 seconds        0.0.0.0:8083->8083/tcp, 9092/tcp
kafka                     confluentinc/cp-kafka     "/etc/confluent/dock…"   kafka               About a minute ago   Up About a minute   0.0.0.0:9092->9092/tcp, 0.0.0.0:29092->29092/tcp
postgres                  debezium/postgres         "docker-entrypoint.s…"   postgres            About a minute ago   Up About a minute   0.0.0.0:5432->5432/tcp
zookeeper                 zookeeper                 "/docker-entrypoint.…"   zookeeper           About a minute ago   Up About a minute   2888/tcp, 3888/tcp, 0.0.0.0:2181->2181/tcp, 8080/tcp
```

## Step 5 :: Create topic in Kafka
```
$docker exec -t kafka /usr/bin/kafka-topics \
      --create --bootstrap-server :9092 \
      --topic order_created \
      --partitions 1 \
      --replication-factor 1
```

## Step 7 :: Linking the Debezium Kafka Connect With the Outbox Table
```
$curl -X POST \
  http://localhost:8083/connectors/ \
  -H 'content-type: application/json' \
  -d '{
   "name": "order-outbox-connector",
   "config": {
      "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
      "tasks.max": "1",
      "database.hostname": "postgres",
      "database.port": "5432",
      "database.user": "user",
      "database.password": "password",
      "database.dbname": "demodb",
      "database.server.name": "pg-outbox-server",
      "tombstones.on.delete": "false",
      "table.include.list": "public.outbox",
      "topic.prefix": "order",
      "transforms": "outbox",
      "transforms.outbox.type": "com.example.transform.CustomTransformation"
   }
}'
```

## Step 7 :: Start consumer of Kafka
```
$docker exec -t kafka /usr/bin/kafka-console-consumer \
      --bootstrap-server :9092 \
      --from-beginning \
      --topic order_created
```

## Step 8 :: Run application again
```
$curl --location 'http://localhost:8080/order' \
--header 'Content-Type: application/json' \
--data '{
    "address": "Demo address",
    "email": "Demo email"
}'
```



## More step !!
Check data in database
```
$docker exec -it postgres bash
psql -d demodb -U user -W
\l
\c demodb
\dt
```
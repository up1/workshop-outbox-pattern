FROM eclipse-temurin:17.0.7_7-jdk as build
WORKDIR /app
COPY . .
RUN ./mvnw clean package

FROM debezium/connect
ENV DEBEZIUM_DIR=$KAFKA_CONNECT_PLUGINS_DIR/debezium-transformer
RUN mkdir $DEBEZIUM_DIR
COPY --from=build /app/target/custom-debezium-transformer.jar $DEBEZIUM_DIR
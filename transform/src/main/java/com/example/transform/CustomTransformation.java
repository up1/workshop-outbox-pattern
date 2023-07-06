package com.example.transform;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.header.Headers;
import org.apache.kafka.connect.transforms.Transformation;

import java.util.Map;

public class CustomTransformation<R extends ConnectRecord<R>> implements Transformation<R> {

    public R apply(R sourceRecord) {

        Struct kStruct = (Struct) sourceRecord.value();
        String databaseOperation = kStruct.getString("op");

        // Handle only Create
        if ("c".equalsIgnoreCase(databaseOperation)) {
            System.out.println("==========");
            System.out.println(kStruct.toString());
            System.out.println("==========");
            // Get the details
            Struct after = (Struct) kStruct.get("after");
            String UUID = after.getString("uuid");
            String payload = after.getString("payload");
            String eventType = after.getString("event_type").toLowerCase();
            String topic = eventType.toLowerCase();

            Headers headers = sourceRecord.headers();
            headers.addString("eventId", UUID);

            // Build the event to be published
            sourceRecord = sourceRecord.newRecord(topic, null, Schema.STRING_SCHEMA, UUID,
                    null, payload, sourceRecord.timestamp(), headers);
        }

        return sourceRecord;
    }

    public ConfigDef config() {
        return new ConfigDef();
    }

    public void close() {
    }

    public void configure(Map<String, ?> configs) {
    }
}

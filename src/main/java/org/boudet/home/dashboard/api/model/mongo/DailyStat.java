package org.boudet.home.dashboard.api.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

@Document(collection = "stats")
public class DailyStat {

    @Id
    private String id;

    @Field
    private String type;

    @Field
    private String room;

    @Field
    private LocalDateTime time;

    @Field
    private HashMap<String, Double> values;

    public LocalDateTime getTime() {
        return time;
    }

    public HashMap<String, Double> getValues() {
        return values;
    }

    public String getType() {
        return type;
    }
}

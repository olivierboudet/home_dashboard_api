package org.boudet.home.dashboard.api.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
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
    private SortedMap<Integer, SortedMap<Integer, Double>> values;

    public LocalDateTime getTime() {
        return time;
    }

    public SortedMap<Integer, SortedMap<Integer,Double>> getValues() {
        return values;
    }
}

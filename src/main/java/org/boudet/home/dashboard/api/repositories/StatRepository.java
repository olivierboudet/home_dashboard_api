package org.boudet.home.dashboard.api.repositories;


import org.boudet.home.dashboard.api.model.mongo.DailyStat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends MongoRepository<DailyStat, String> {
    List<DailyStat> findByType(String type);

    DailyStat findByTypeAndTime(String type, LocalDateTime datetime);

    List<DailyStat> findByTypeAndRoomAndTimeGreaterThanEqual(String type, String room, LocalDateTime start);

    List<DailyStat> findByTypeAndTimeGreaterThanEqual(String powerW, LocalDateTime minus);

    DailyStat findByTypeAndRoomAndTime(String humidity, String room, LocalDateTime localDateTime);
}


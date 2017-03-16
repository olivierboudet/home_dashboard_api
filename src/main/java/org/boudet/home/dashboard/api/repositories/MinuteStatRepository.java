package org.boudet.home.dashboard.api.repositories;


import org.boudet.home.dashboard.api.model.mongo.DailyStat;
import org.boudet.home.dashboard.api.model.mongo.MinuteStat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MinuteStatRepository extends MongoRepository<MinuteStat, String> {
    List<MinuteStat> findByType(String type);

    MinuteStat findByTypeAndTime(String type, LocalDateTime datetime);

    List<MinuteStat> findByTypeAndRoomAndTimeGreaterThanEqual(String type, String room, LocalDateTime start);

    List<MinuteStat> findByTypeAndTimeGreaterThanEqual(String type, LocalDateTime minus);

    MinuteStat findByTypeAndRoomAndTime(String type, String room, LocalDateTime localDateTime);
}


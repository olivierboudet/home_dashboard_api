package org.boudet.home.dashboard.api.repositories;


import org.boudet.home.dashboard.api.model.mongo.DailyStat;
import org.boudet.home.dashboard.api.model.mongo.MinuteStat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyStatRepository extends MongoRepository<DailyStat, String> {
    @Query("{type: { $in: ?0 }, time: { $gte: ?1 }}")
    List<DailyStat> findByTypesAndTimeGreaterThanEqual(String[] types, LocalDateTime minus);
}


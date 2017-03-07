package org.boudet.home.dashboard.api.jobs;

import org.boudet.home.dashboard.api.JobKey;
import org.boudet.home.dashboard.api.enums.TypeEnum;
import org.boudet.home.dashboard.api.model.SimpleStat;
import org.boudet.home.dashboard.api.model.mongo.DailyStat;
import org.boudet.home.dashboard.api.repositories.StatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.SortedMap;

@Service
public class CurrentHumidityJob extends AbstractJob {

    @Autowired
    private StatRepository statRepository;

    @Override
    public TypeEnum getType() {
        return TypeEnum.HUMIDITY;
    }

    @Override
    public SimpleStat fetchData(JobKey key) {
        DailyStat stat = statRepository.findByTypeAndRoomAndTime("humidity", key.getRoom(), LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
        SortedMap<Integer, SortedMap<Integer, Double>> values = stat.getValues();

        Integer keyHour = values.lastKey();
        SortedMap<Integer, Double> valuesForLastHour = values.get(values.lastKey());

        Integer keyMinute = valuesForLastHour.lastKey();

        return new SimpleStat(stat.getTime().withHour(keyHour).withMinute(keyMinute), valuesForLastHour.get(keyMinute));
    }

    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void run() {
        Set<JobKey> keys = getJobKeys();
        for(JobKey key : keys) {
            super.execute(key);
        }
    }
}

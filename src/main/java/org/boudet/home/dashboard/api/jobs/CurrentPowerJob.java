package org.boudet.home.dashboard.api.jobs;

import org.boudet.home.dashboard.api.JobKey;
import org.boudet.home.dashboard.api.enums.TypeEnum;
import org.boudet.home.dashboard.api.model.SimpleStat;
import org.boudet.home.dashboard.api.model.mongo.MinuteStat;
import org.boudet.home.dashboard.api.repositories.DailyStatRepository;
import org.boudet.home.dashboard.api.repositories.MinuteStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrentPowerJob extends AbstractJob<List<SimpleStat>> {

    @Autowired
    private MinuteStatRepository statRepository;

    @Override
    public TypeEnum getType() {
        return TypeEnum.POWER;
    }

    @Override
    public List<SimpleStat> fetchData(JobKey key) {
        LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

        List<MinuteStat> stats = statRepository.findByTypeAndTimeGreaterThanEqual("powerW", today.minus(1, ChronoUnit.DAYS));

        List<SimpleStat> simpleStatList = new ArrayList();
        stats.forEach(stat -> {
            stat.getValues().forEach((hour, statByHour) -> statByHour.forEach((minute, value) -> {
                simpleStatList.add(new SimpleStat(stat.getTime().withHour(hour).withMinute(minute), value));
            }));
        });

        return simpleStatList;
    }

    @Scheduled(fixedDelay = 1000 * 60)
    public void run() {

        super.execute(new JobKey(getType(), null));
    }
}

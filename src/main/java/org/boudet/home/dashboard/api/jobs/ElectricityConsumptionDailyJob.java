package org.boudet.home.dashboard.api.jobs;

import org.boudet.home.dashboard.api.JobKey;
import org.boudet.home.dashboard.api.enums.TypeEnum;
import org.boudet.home.dashboard.api.model.TypedSimpleStat;
import org.boudet.home.dashboard.api.model.mongo.DailyStat;
import org.boudet.home.dashboard.api.repositories.DailyStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ElectricityConsumptionDailyJob extends AbstractJob<List<TypedSimpleStat>> {

    @Autowired
    private DailyStatRepository statRepository;

    @Override
    public TypeEnum getType() {
        return TypeEnum.ELECTRICITY_CONSUMPTION_DAILY;
    }

    @Override
    public List<TypedSimpleStat> fetchData(JobKey key) {
        LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

        List<DailyStat> stats = statRepository.findByTypesAndTimeGreaterThanEqual(new  String[] {"consoHP", "consoHC"}, today.minus(90, ChronoUnit.DAYS));

        List<TypedSimpleStat> simpleStatList = new ArrayList();
        stats.forEach(stat -> simpleStatList.add(new TypedSimpleStat(stat.getType(), stat.getTime(), stat.getValues().get("value"))));

        return simpleStatList;
    }

    @Scheduled(fixedDelay = 5 * 1000 * 60)
    public void run() {

        super.execute(new JobKey(getType(), null));
    }
}

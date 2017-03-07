package org.boudet.home.dashboard.api.jobs;

import org.boudet.home.dashboard.api.enums.TypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class JobFactory {
    private Map<TypeEnum, AbstractJob> jobs = new HashMap();

    @Autowired
    private CurrentHumidityJob currentHumidityJob;

    @Autowired
    private CurrentTemperatureJob currentTemperatureJob;

    @Autowired
    private SolarProductionTodayJob solarProductionTodayJob;

    @Autowired
    private SolarProductionHistoryDailyJob solarProductionHistoryDailyJob;

    @Autowired
    private SolarProductionHistoryMonthlyJob solarProductionHistoryMonthlyJob;

    @Autowired
    private CurrentPowerJob currentPowerJob;

    @PostConstruct
    public void init() {
        jobs.put(TypeEnum.TEMPERATURE, currentTemperatureJob);
        jobs.put(TypeEnum.HUMIDITY, currentHumidityJob);
        jobs.put(TypeEnum.SOLAR, solarProductionTodayJob);
        jobs.put(TypeEnum.SOLAR_HISTORY_DAILY, solarProductionHistoryDailyJob);
        jobs.put(TypeEnum.SOLAR_HISTORY_MONTHLY, solarProductionHistoryMonthlyJob);
        jobs.put(TypeEnum.POWER, currentPowerJob);
    }

    public AbstractJob getJob(TypeEnum type) {
        return jobs.get(type);
    }
}

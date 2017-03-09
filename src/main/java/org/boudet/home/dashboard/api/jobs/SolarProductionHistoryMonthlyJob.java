package org.boudet.home.dashboard.api.jobs;

import org.boudet.home.dashboard.api.JobKey;
import org.boudet.home.dashboard.api.enums.TypeEnum;
import org.boudet.home.dashboard.api.model.SimpleStat;
import org.boudet.home.dashboard.api.model.StatWithAverage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.util.List;

@Service
public class SolarProductionHistoryMonthlyJob extends AbstractJob<List<StatWithAverage>> {

    @Autowired
    @Qualifier("solarDBJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM")
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .toFormatter();

    @Override
    public TypeEnum getType() {
        return TypeEnum.SOLAR_HISTORY_MONTHLY;
    }

    @Override
    public List<StatWithAverage> fetchData(JobKey key) {


        List<StatWithAverage> stat = jdbcTemplate.query("select strftime('%Y-%m', timestamp) as yearmonth, strftime('%m',timestamp) as month, sum(dayyield) as total, (select avg(total) from (select strftime('%Y-%m', timestamp) as yearmonth, sum(dayyield) as total from vwMonthData t2 where strftime('%m',timestamp)=strftime('%m',t1.timestamp) group by yearmonth)) as average from vwMonthData t1 group by yearmonth;",
                (rs, rowNum) -> {
                    LocalDate timestamp = LocalDate.parse(rs.getString("yearmonth"), formatter);

                    return new StatWithAverage(timestamp.atStartOfDay(), rs.getDouble("total"), rs.getDouble("average"));

                });

        return stat;
    }

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void run() {

        super.execute(new JobKey(getType(), null));
    }
}

package org.boudet.home.dashboard.api.jobs;

import org.boudet.home.dashboard.api.JobKey;
import org.boudet.home.dashboard.api.enums.TypeEnum;
import org.boudet.home.dashboard.api.model.SimpleStat;
import org.boudet.home.dashboard.api.model.StatWithAverage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SolarProductionHistoryDailyJob extends AbstractJob<List<SimpleStat>> {

    @Autowired
    @Qualifier("solarDBJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public TypeEnum getType() {
        return TypeEnum.SOLAR_HISTORY_DAILY;
    }

    @Override
    public List<SimpleStat> fetchData(JobKey key) {


        List<SimpleStat> stat = jdbcTemplate.query("select timestamp, dayyield, (select avg(dayyield) from (select strftime('%m-%d',timestamp) as day, dayyield from vwMonthData t2 where day=strftime('%m-%d',t1.timestamp))) as average from vwMonthData t1 where strftime('%Y-%m-%d', date(date('now'), '-13 months')) < timestamp order by timestamp;",
                (rs, rowNum) -> {
                    LocalDate timestamp = LocalDate.parse(rs.getString("timestamp"), formatter);

                    return new StatWithAverage(timestamp.atStartOfDay(), rs.getDouble("dayyield"), rs.getDouble("average"));

                });

        return stat;
    }

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void run() {

        super.execute(new JobKey(getType(), null));
    }
}

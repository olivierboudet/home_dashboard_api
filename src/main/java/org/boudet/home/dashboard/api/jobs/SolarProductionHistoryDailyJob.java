package org.boudet.home.dashboard.api.jobs;

import org.boudet.home.dashboard.api.JobKey;
import org.boudet.home.dashboard.api.enums.TypeEnum;
import org.boudet.home.dashboard.api.model.SimpleStat;
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


        List<SimpleStat> stat = jdbcTemplate.query("select timestamp, dayyield from vwMonthData order by timestamp;",
                new RowMapper<SimpleStat>() {
                    @Override
                    public SimpleStat mapRow(ResultSet rs, int rowNum) throws SQLException {
                        LocalDate timestamp = LocalDate.parse(rs.getString("timestamp"), formatter);

                        return new SimpleStat(timestamp.atStartOfDay(), rs.getDouble("dayyield"));
                    }
                });

        return stat;
    }

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void run() {

        super.execute(new JobKey(getType(), null));
    }
}

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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class SolarProductionTodayJob extends AbstractJob<List<SimpleStat>> {

    @Autowired
    @Qualifier("solarDBJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public TypeEnum getType() {
        return TypeEnum.SOLAR;
    }

    @Override
    public List<SimpleStat> fetchData(JobKey key) {
        List<SimpleStat> stat = jdbcTemplate.query("select timestamp,Etoday from SpotData where timestamp >= strftime('%s', 'now', 'localtime', 'start of day', 'utc') order by timestamp;",
                new RowMapper<SimpleStat>() {
                    @Override
                    public SimpleStat mapRow(ResultSet rs, int rowNum) throws SQLException {
                        LocalDateTime timestamp = LocalDateTime.ofInstant(Instant.ofEpochSecond(rs.getInt("timestamp")), ZoneId.systemDefault());

                        return new SimpleStat(timestamp, rs.getDouble("Etoday"));
                    }
                });

        return stat;
    }

    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void run() {

        super.execute(new JobKey(getType(), null));
    }
}

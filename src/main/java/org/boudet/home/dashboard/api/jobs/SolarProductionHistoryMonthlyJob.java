package org.boudet.home.dashboard.api.jobs;

import org.boudet.home.dashboard.api.JobKey;
import org.boudet.home.dashboard.api.enums.TypeEnum;
import org.boudet.home.dashboard.api.model.SimpleStat;
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
public class SolarProductionHistoryMonthlyJob extends AbstractJob<List<SimpleStat>> {

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
    public List<SimpleStat> fetchData(JobKey key) {


        List<SimpleStat> stat = jdbcTemplate.query("select strftime('%Y-%m', timestamp) as month, sum(dayyield) as total from vwMonthData group by month;",
                new RowMapper<SimpleStat>() {
                    @Override
                    public SimpleStat mapRow(ResultSet rs, int rowNum) throws SQLException {
                        LocalDate timestamp = LocalDate.parse(rs.getString("month"), formatter);

                        return new SimpleStat(timestamp.atStartOfDay(), rs.getDouble("total"));
                    }
                });

        return stat;
    }

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void run() {

        super.execute(new JobKey(getType(), null));
    }
}

package org.boudet.home.dashboard.api.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;


public class StatWithAverage extends SimpleStat {

    @JsonSerialize
    private double average;

    public StatWithAverage(LocalDateTime time, double value, double average) {
        super(time, value);
        this.average = average;
    }
}

package org.boudet.home.dashboard.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;


public class SimpleStat {
    @JsonSerialize
    private double value;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
    public LocalDateTime time;

    public SimpleStat(LocalDateTime time, double value) {
        this.time = time;
        this.value = value;
    }
}

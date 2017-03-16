package org.boudet.home.dashboard.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;


public class TypedSimpleStat {

    @JsonSerialize
    private String type;

    @JsonSerialize
    private double value;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
    public LocalDateTime time;

    public TypedSimpleStat(String type, LocalDateTime time, double value) {
        this.type = type;
        this.time = time;
        this.value = value;
    }
}

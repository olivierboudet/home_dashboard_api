package org.boudet.home.dashboard.api;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.boudet.home.dashboard.api.enums.TypeEnum;

import java.util.Objects;

public class JobKey {

    private TypeEnum type;
    private String room;

    public JobKey(TypeEnum type, String room) {
        this.type = type;
        this.room = room;
    }

    public TypeEnum getType() {
        return type;
    }

    public String getRoom() {
        return room;
    }

    @Override
    public int hashCode() {
        return type.name().concat((room != null) ? room : "").hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o != null && o instanceof JobKey) {
            JobKey key = (JobKey) o;

            return Objects.equals(type, key.getType()) && Objects.equals(room, key.getRoom());
        }
        return false;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

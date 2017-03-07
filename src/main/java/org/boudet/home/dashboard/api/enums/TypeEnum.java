package org.boudet.home.dashboard.api.enums;

public enum TypeEnum {
    TEMPERATURE("temperature"),
    HUMIDITY("humidity"),
    SOLAR("solar"),
    SOLAR_HISTORY_DAILY("solar_history_daily"),
    SOLAR_HISTORY_MONTHLY("solar_history_monthly"),
    CONSOHP("consoHP"),
    CONSOHC("consoHC"),
    POWER("power");

    public String name;

    TypeEnum(String name) {
        this.name = name;
    }

    public static TypeEnum fromString(String name) {
        if (name != null) {
            for (TypeEnum v : TypeEnum.values()) {
                if (name.equalsIgnoreCase(v.name)) {
                    return v;
                }
            }
        }
        return null;
    }
}

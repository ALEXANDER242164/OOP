package com.informaticonfing.spring.app.springboot.model;

public enum AppointmentStatus {
    PENDIENTE("pendiente"),
    COMPLETADO("completado"),
    CANCELADO("cancelado");

    private final String dbValue;

    AppointmentStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static AppointmentStatus fromDbValue(String dbValue) {
        if (dbValue == null) return null;
        for (AppointmentStatus s : values()) {
            if (s.dbValue.equalsIgnoreCase(dbValue)) return s;
        }
        return null;
    }

    @Override
    public String toString() {
        return dbValue;
    }
}

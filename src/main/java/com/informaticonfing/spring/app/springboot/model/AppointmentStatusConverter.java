package com.informaticonfing.spring.app.springboot.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class AppointmentStatusConverter implements AttributeConverter<AppointmentStatus, String> {

    @Override
    public String convertToDatabaseColumn(AppointmentStatus attribute) {
        return attribute != null ? attribute.getDbValue() : null;
    }

    @Override
    public AppointmentStatus convertToEntityAttribute(String dbData) {
        return AppointmentStatus.fromDbValue(dbData);
    }
}

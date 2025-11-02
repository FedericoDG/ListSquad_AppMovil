package com.federicodg80.listly.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String dateString = json.getAsString();

        try {
            // Caso completo: 2024-01-01T00:00:00
            return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e1) {
            try {
                // Caso solo fecha: 2024-01-01 → convertir a LocalDateTime a medianoche
                LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
                return date.atStartOfDay();
            } catch (Exception e2) {
                throw new JsonParseException("Error parsing date: " + dateString, e2);
            }
        }
    }
}

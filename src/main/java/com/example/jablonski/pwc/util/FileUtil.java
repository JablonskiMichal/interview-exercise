package com.example.jablonski.pwc.util;

import com.example.jablonski.pwc.model.Country;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

public class FileUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public static List<Country> geCountries() {
        return readFile("countries.json", new TypeReference<>() {
        });
    }

    public static <T> T readFile(String path, TypeReference<T> typeReference) {
        ClassPathResource resource = new ClassPathResource(path);
        try {
            return OBJECT_MAPPER.readValue(resource.getInputStream(), typeReference);
        } catch (IOException e) {
            throw new IllegalArgumentException("Wrong file path");
        }
    }

}

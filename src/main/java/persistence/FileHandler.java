package persistence;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;

public class FileHandler {
    private final ObjectMapper mapper;

    public FileHandler() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        this.mapper.setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.ANY);
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void save(Object data, String filePath) {
        try {
            mapper.writeValue(new File(filePath), data);
        } catch (Exception e) {
            System.err.println("Błąd zapisu pliku: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public <T> T load(String filePath, Class<T> clazz) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                return null;
            }
        }

        try {
            return mapper.readValue(file, clazz);
        } catch (Exception e) {
            System.err.println("Błąd odczytu pliku: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
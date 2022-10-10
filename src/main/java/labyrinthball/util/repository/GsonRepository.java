package labyrinthball.util.repository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import labyrinthball.util.gson.ZonedDateTimeAdapter;
import labyrinthball.util.gson.LocalDateAdapter;


public class GsonRepository<T> extends labyrinthball.util.repository.Repository<T> {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .create();

    public GsonRepository(Class<T> elementType) {
        super(elementType);
    }

    public void loadFromFile(File file) throws IOException {
        try (var reader = new FileReader(file)) {
            var listType = TypeToken.getParameterized(List.class, elementType).getType();
            elements = GSON.fromJson(reader, listType);
        }
    }

    public void saveToFile(File file) throws IOException {
        try (var writer = new FileWriter(file)) {
            GSON.toJson(elements, writer);
        }
    }

}
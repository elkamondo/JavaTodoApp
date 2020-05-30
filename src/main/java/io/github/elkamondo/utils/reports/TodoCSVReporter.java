package io.github.elkamondo.utils.reports;

import io.github.elkamondo.exceptions.FileNameNotValidException;
import io.github.elkamondo.models.Todo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import static io.github.elkamondo.utils.Constants.DEFAULT_DATETIME_FORMATTER;

public class TodoCSVReporter implements TodoReporter {

    private static final String[] CSV_HEADER = {"id", "name", "completed", "createdAt", "completedAt"};

    @Override
    public boolean save(Collection<? extends Todo> todos, String filename) throws IOException, FileNameNotValidException {
        if (filename == null || filename.isEmpty()) {
            throw new FileNameNotValidException("You should enter a valid file name.");
        }

        if (todos == null) {
            return false;
        }

        final BiFunction<LocalDateTime, DateTimeFormatter, String> formatDate =
                (dateTime, formatter) -> dateTime != null ? dateTime.format(formatter) : null;

        final FileWriter out = new FileWriter(filename);
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(CSV_HEADER))) {
            for (Todo todo : todos) {
                printer.printRecord(
                        todo.getId(), todo.getName(), todo.isCompleted(),
                        formatDate.apply(todo.getCreatedAt(), DEFAULT_DATETIME_FORMATTER),
                        formatDate.apply(todo.getCompletedAt(), DEFAULT_DATETIME_FORMATTER)
                );
            }
        }

        return true;
    }

    @Override
    public Collection<? extends Todo> load(String filename) throws IOException, FileNameNotValidException {
        if (filename == null || filename.isEmpty()) {
            throw new FileNameNotValidException("You should enter a valid file name.");
        }

        if (Files.notExists(Paths.get(filename))) {
            return Collections.emptyList();
        }

        final Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(new FileReader(filename));

        final BiFunction<String, DateTimeFormatter, LocalDateTime> parseDate =
                (dateTime, formatter) ->
                        dateTime != null && !dateTime.isEmpty()
                                ? LocalDateTime.parse(dateTime, formatter)
                                : null;

        final List<Todo> todos = new ArrayList<>();
        records.forEach(record -> {
            final String id = record.get(CSV_HEADER[0]);
            final String name = record.get(CSV_HEADER[1]);
            final boolean completed = Boolean.parseBoolean(record.get(CSV_HEADER[2]));
            final LocalDateTime createdAt = parseDate.apply(record.get(CSV_HEADER[3]), DEFAULT_DATETIME_FORMATTER);
            final LocalDateTime completedAt = parseDate.apply(record.get(CSV_HEADER[4]), DEFAULT_DATETIME_FORMATTER);
            todos.add(new Todo(id, name, completed, createdAt, completedAt));
        });

        return todos;
    }

}

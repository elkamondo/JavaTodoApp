package io.github.elkamondo.utils.reports;

import io.github.elkamondo.exceptions.FileNameNotValidException;
import io.github.elkamondo.models.Todo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

class TodoCSVReporterTest {

    private static final List<Todo> todoList = new ArrayList<>();
    private final String BACKUP_FILENAME = "test-todos.csv";
    private final TodoReporter csvReporter = new TodoCSVReporter();

    @BeforeAll
    static void setUp() {
        final Todo todo1 = new Todo("Getting started with OCaml");
        final Todo todo2 = new Todo("Eat dinner");
        final Todo todo3 = new Todo("Walk my dog");
        todoList.addAll(asList(todo1, todo2, todo3));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(BACKUP_FILENAME));
    }

    @DisplayName("Save todos to a CSV file")
    @Test
    void save() throws IOException, FileNameNotValidException {
        assertThrows(
                FileNameNotValidException.class,
                () -> csvReporter.save(todoList, null),
                "Should be a valid file name");

        assertThrows(
                FileNameNotValidException.class,
                () -> csvReporter.save(todoList, ""),
                "Should be a valid file name");

        assertFalse(csvReporter.save(null, BACKUP_FILENAME), "Should not accept a null collection");
        assertTrue(csvReporter.save(emptyList(), BACKUP_FILENAME), "Should accept empty collections");
        assertTrue(csvReporter.save(todoList, BACKUP_FILENAME), "Should save non empty collections");
    }

    @DisplayName("Load todos from a CSV file")
    @Test
    void load() throws IOException, FileNameNotValidException {
        assertThrows(
                FileNameNotValidException.class,
                () -> csvReporter.load(null),
                "Should be a valid file name");

        assertThrows(
                FileNameNotValidException.class,
                () -> csvReporter.load(""),
                "Should be a valid file name");


        assertEquals(emptyList(), csvReporter.load("FILE_NOT_EXISTS"));
        assertTrue(csvReporter.save(todoList, BACKUP_FILENAME), "Should save todos");

        final List<Todo> persistedData = new ArrayList<>(csvReporter.load(BACKUP_FILENAME));
        assertFalse(persistedData.isEmpty(), "Should return a non empty collection");
        assertEquals(3, persistedData.size(), "Should contains 3 todos");
    }

}
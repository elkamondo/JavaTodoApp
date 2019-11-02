package io.github.elkamondo.utils;

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

class TodoUtilsTest {

    private final String BACKUP_FILENAME = "test-todos.csv";
    private static List<Todo> todoList = new ArrayList<>();

    @BeforeAll
    static void setUp() {
        Todo todo1 = new Todo("Getting started with OCaml");
        Todo todo2 = new Todo("Eat dinner");
        Todo todo3 = new Todo("Walk my dog");
        todoList.addAll(asList(todo1, todo2, todo3));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(BACKUP_FILENAME));
    }

    @DisplayName("Save todos to a CSV file")
    @Test
    void saveAsCSV() throws IOException, FileNameNotValidException {
        assertThrows(
                FileNameNotValidException.class,
                () -> TodoUtils.saveAsCSV(todoList, null),
                "Should be a valid file name");

        assertThrows(
                FileNameNotValidException.class,
                () -> TodoUtils.saveAsCSV(todoList, ""),
                "Should be a valid file name");

        assertFalse(TodoUtils.saveAsCSV(null, BACKUP_FILENAME), "Should not accept a null collection");
        assertTrue(TodoUtils.saveAsCSV(emptyList(), BACKUP_FILENAME), "Should accept empty collections");
        assertTrue(TodoUtils.saveAsCSV(todoList, BACKUP_FILENAME), "Should save non empty collections");
    }

    @DisplayName("Load todos from a CSV file")
    @Test
    void loadFromCSV() throws IOException, FileNameNotValidException {
        assertThrows(
                FileNameNotValidException.class,
                () -> TodoUtils.loadFromCSV(null),
                "Should be a valid file name");

        assertThrows(
                FileNameNotValidException.class,
                () -> TodoUtils.loadFromCSV(""),
                "Should be a valid file name");


        assertEquals(emptyList(), TodoUtils.loadFromCSV("FILE_NOT_EXISTS"));
        assertTrue(TodoUtils.saveAsCSV(todoList, BACKUP_FILENAME), "Should save todos");

        List<Todo> persistedData = new ArrayList<>(TodoUtils.loadFromCSV(BACKUP_FILENAME));
        assertFalse(persistedData.isEmpty(), "Should return a non empty collection");
        assertEquals(3, persistedData.size(), "Should contains 3 todos");
    }

}
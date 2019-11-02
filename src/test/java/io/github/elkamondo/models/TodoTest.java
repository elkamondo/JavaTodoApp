package io.github.elkamondo.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @DisplayName("Create a todo")
    @Test
    void createTodo() {
        Todo todo1 = new Todo("Learn Java");
        int hash = 31 * Objects.hash(null, "Learn Java", false, todo1.getCreatedAt());

        assertTrue(String.valueOf(hash).endsWith(todo1.getId()), "Todo's ID constructed from initial hashCode");
        assertEquals(todo1.getName(), "Learn Java");
        assertFalse(todo1.isCompleted(), "Todo's completed state initialized to false");
    }

    @DisplayName("Sort a todo list by name")
    @Test
    void sortTodo() {
        Todo todo1 = new Todo("Learn Advanced Scala");
        Todo todo2 = new Todo("Do Math Homework");
        Todo todo3 = new Todo("learn JUnit 5");

        List<Todo> todoList = asList(todo1, todo2, todo3);
        todoList.sort(Comparator.comparing(Todo::getName));

        assertAll("Sort todos by name",
                () -> assertEquals(todoList.get(0), todo2),
                () -> assertEquals(todoList.get(1), todo1),
                () -> assertEquals(todoList.get(2), todo3)
        );
    }

    @DisplayName("Should be the same todos")
    @Test
    void sameTodo() {
        Todo todo1 = new Todo("Shut down Hadoop cluster");
        Todo todo2 = new Todo(todo1.getId(), todo1.getName(), todo1.isCompleted(), todo1.getCreatedAt());
        assertEquals(todo1, todo2, "todo1 same as todo2");
    }

}

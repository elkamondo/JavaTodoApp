package io.github.elkamondo.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @DisplayName("Create a todo")
    @Test
    void createTodo() {
        final Todo todo = new Todo("Learn Java");

        assertNotNull(todo.getId());
        assertEquals(8, todo.getId().length(), "Todo's ID length fixed to 8 characters");

        assertFalse(todo.isCompleted(), "Todo's completed state initialized to false");
        assertNull(todo.getCompletedAt());

        assertEquals(todo.getName(), "Learn Java");
        todo.setName("Learn Java 14: Records feature");
        assertNotEquals("Learn Java", todo.getName());
        assertEquals("Learn Java 14: Records feature", todo.getName());
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
        assertEquals(0, todo1.compareTo(todo2));
        assertEquals(todo2.hashCode(), todo1.hashCode());
        assertFalse(todo1.equals(null));
        assertFalse(todo1.equals(new Object()));
    }

}

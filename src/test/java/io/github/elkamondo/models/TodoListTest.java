package io.github.elkamondo.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class TodoListTest {

    @DisplayName("Add todos")
    @Test
    void addTodos() {
        TodoList todoList = new TodoList();

        List<Todo> anotherTodos = asList(new Todo("Learn Spark"),
                new Todo("Deploy Ubuntu container"),
                new Todo("Do shopping"));

        assertFalse(todoList.add(null), "Do not add null");
        assertTrue(todoList.add(new Todo("A simple todo")), "Add a todo");
        assertTrue(todoList.addAll(anotherTodos), "Add collection of todos");
        assertFalse(todoList.addAll(null), "Do not add a null collection");
        assertEquals(4, todoList.getAllTodos().size(), "Should contains 4 elements");
    }

    @DisplayName("Complete a todo")
    @Test
    void completeTodo() {
        TodoList todoList = new TodoList();
        todoList.add(new Todo("T1", "Watch Movies", false, LocalDateTime.now()));

        assertEquals(1, todoList.getAllTodos().size());
        assertEquals(0, todoList.getCompletedTodos().size());
        assertEquals(1, todoList.getActiveTodos().size());

        assertFalse(todoList.completeTodo(null));
        assertEquals(0, todoList.getCompletedTodos().size());
        assertEquals(1, todoList.getActiveTodos().size());

        assertTrue(todoList.completeTodo("T1"));
        assertEquals(1, todoList.getCompletedTodos().size());
        assertEquals(0, todoList.getActiveTodos().size());

        assertFalse(todoList.completeTodo("ABC"));
        assertEquals(1, todoList.getCompletedTodos().size());
        assertEquals(0, todoList.getActiveTodos().size());
    }

    @DisplayName("Mark a todo as undone")
    @Test
    void unCompleteTodo() {
        final TodoList todoList = new TodoList();
        todoList.add(new Todo("T1", "Read a book", false, LocalDateTime.now()));

        assertEquals(1, todoList.getAllTodos().size());
        assertEquals(0, todoList.getCompletedTodos().size());
        assertEquals(1, todoList.getActiveTodos().size());

        assertFalse(todoList.completeTodo(null));
        assertEquals(0, todoList.getCompletedTodos().size());
        assertEquals(1, todoList.getActiveTodos().size());

        assertTrue(todoList.completeTodo("T1"));
        assertEquals(1, todoList.getCompletedTodos().size());
        assertEquals(0, todoList.getActiveTodos().size());

        assertTrue(todoList.unCompleteTodo("T1"));
        assertEquals(0, todoList.getCompletedTodos().size());
        assertEquals(1, todoList.getActiveTodos().size());

        assertFalse(todoList.unCompleteTodo("XYZ"));
        assertEquals(0, todoList.getCompletedTodos().size());
        assertEquals(1, todoList.getActiveTodos().size());
    }

    @DisplayName("Remove a todo")
    @Test
    void removeTodo() {
        TodoList todoList = new TodoList();
        todoList.add(new Todo("T1", "Hang out with friends", true, LocalDateTime.now()));

        assertEquals(1, todoList.getAllTodos().size());
        assertEquals(1, todoList.getCompletedTodos().size());
        assertEquals(0, todoList.getActiveTodos().size());

        assertFalse(todoList.removeTodo(null));
        assertEquals(1, todoList.getAllTodos().size());

        assertFalse(todoList.removeTodo("ABC"));
        assertEquals(1, todoList.getAllTodos().size());
        assertEquals(1, todoList.getCompletedTodos().size());
        assertEquals(0, todoList.getActiveTodos().size());

        assertTrue(todoList.removeTodo("T1"));
        assertEquals(0, todoList.getAllTodos().size());
        assertEquals(0, todoList.getCompletedTodos().size());
        assertEquals(0, todoList.getActiveTodos().size());
    }

    @DisplayName("Filter active and completed todos")
    @Test
    void filterTodos() {
        Todo todo1 = new Todo("T1", "Learn Python", true, LocalDateTime.now());
        Todo todo2 = new Todo("T2", "Learn R", false, LocalDateTime.now());
        Todo todo3 = new Todo("T3", "Learn Scala", true, LocalDateTime.now());
        TodoList todoList = new TodoList(asList(todo1, todo2, todo3));

        assertEquals(3, todoList.getAllTodos().size());
        assertEquals(1, todoList.getActiveTodos().size());
        assertEquals(2, todoList.getCompletedTodos().size());

        assertAll("Should contain only non completed todos",
                () -> assertFalse(todoList.getActiveTodos().contains(todo1)),
                () -> assertTrue(todoList.getActiveTodos().contains(todo2)),
                () -> assertFalse(todoList.getActiveTodos().contains(todo3))
        );

        assertAll("Should contain only completed todos",
                () -> assertTrue(todoList.getCompletedTodos().contains(todo1)),
                () -> assertFalse(todoList.getCompletedTodos().contains(todo2)),
                () -> assertTrue(todoList.getCompletedTodos().contains(todo3))
        );
    }

}
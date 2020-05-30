package io.github.elkamondo.models;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toCollection;

public class TodoList {

    private static final Comparator<Todo> BY_ID_THEN_NAME_THEN_DATE =
            Comparator.comparing(Todo::getId)
                      .thenComparing(Todo::getName)
                      .thenComparing(Todo::getCreatedAt);

    private final Set<Todo> todos = new TreeSet<>(BY_ID_THEN_NAME_THEN_DATE);

    public TodoList() {}

    public TodoList(Collection<? extends Todo> collection) {
        if (collection != null) {
            todos.addAll(collection);
        }
    }

    public boolean isEmpty() {
        return todos.isEmpty();
    }

    public boolean add(Todo todo) {
        if (todo == null) {
            return false;
        }

        return todos.add(todo);
    }

    public boolean addAll(Collection<? extends Todo> todoList) {
        if (todoList == null) {
            return false;
        }

        return todos.addAll(todoList);
    }

    public boolean completeTodo(String todoId) {
        if (todoId == null) {
            return false;
        }

        final Optional<Todo> completedTodo =
                todos.stream()
                     .filter(todo -> todoId.equals(todo.getId()))
                     .findFirst();

        if (completedTodo.isPresent()) {
            final Todo todo = completedTodo.get();
            todo.setComplete(true);
            todo.setCompletedAt(LocalDateTime.now());
            return true;
        }

        return false;
    }

    public boolean unCompleteTodo(String todoId) {
        if (todoId == null) {
            return false;
        }

        final Optional<Todo> completedTodo =
                todos.stream()
                     .filter(todo -> todoId.equals(todo.getId()))
                     .findFirst();

        if (completedTodo.isPresent()) {
            final Todo todo = completedTodo.get();
            todo.setComplete(false);
            todo.setCompletedAt(null);
            return true;
        }

        return false;
    }

    public boolean removeTodo(String todoId) {
        if (todoId == null) {
            return false;
        }

        return todos.removeIf(todo -> todoId.equals(todo.getId()));
    }

    public Collection<? extends Todo> getAllTodos() {
        return todos;
    }

    public Collection<? extends Todo> getActiveTodos() {
        final Predicate<Todo> isCompleted = Todo::isCompleted;
        return todos.stream()
                    .filter(isCompleted.negate())
                    .collect(toCollection(() -> new TreeSet<>(BY_ID_THEN_NAME_THEN_DATE)));
    }

    public Collection<? extends Todo> getCompletedTodos() {
        return todos.stream()
                    .filter(Todo::isCompleted)
                    .collect(toCollection(() -> new TreeSet<>(BY_ID_THEN_NAME_THEN_DATE)));
    }

    @Override
    public String toString() {
        return todos.toString();
    }

}
package io.github.elkamondo.models;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toCollection;

public class TodoList {

    private static Comparator<Todo> BY_NAME_THEN_DATE =
            Comparator.comparing(Todo::getCreatedAt)
                      .thenComparing(Todo::getName);

    private Set<Todo> todos = new TreeSet<>(BY_NAME_THEN_DATE);

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

    public void addAll(Collection<? extends Todo> todoList) {
        todos.addAll(todoList);
    }

    public boolean completeTodo(String todoId) {
        if (todoId == null) {
            return false;
        }

        Todo completedTodo = todos.stream()
                .filter(todo -> todoId.equals(todo.getId()))
                .findFirst()
                .orElse(new Todo("n/a"));

        if (!completedTodo.getName().equalsIgnoreCase("n/a")) {
            completedTodo.complete();
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
                .collect(toCollection(() -> new TreeSet<>(BY_NAME_THEN_DATE)));
    }

    public Collection<? extends Todo> getCompletedTodos() {
        return todos.stream()
                .filter(Todo::isCompleted)
                .collect(toCollection(() -> new TreeSet<>(BY_NAME_THEN_DATE)));
    }

    @Override
    public String toString() {
        return todos.toString();
    }

}
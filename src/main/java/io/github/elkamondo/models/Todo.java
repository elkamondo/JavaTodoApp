package io.github.elkamondo.models;

import io.github.elkamondo.utils.GeneratedIdentifier;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Todo implements Comparable<Todo>, GeneratedIdentifier {

    private final String id;
    private String name;
    private boolean completed;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime completedAt;

    public Todo(String name) {
        this.name = name;
        this.id = generateId(8);
    }

    public Todo(String id, String name, boolean completed, LocalDateTime createdAt) {
        this.name = name;
        this.completed = completed;
        this.createdAt = createdAt;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getFormattedDate() {
        return createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy H:m:s"));
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setComplete(boolean isCompleted) {
        this.completed = isCompleted;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hash(id, name, completed, createdAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Todo todo = (Todo)obj;
        return Objects.equals(id, todo.id)
                && Objects.equals(name, todo.name)
                && Objects.equals(completed, todo.completed)
                && Objects.equals(createdAt, todo.createdAt);
    }

    @Override
    public int compareTo(Todo other) {
        return id.compareTo(other.id);
    }

    @Override
    public String toString() {
        return String.format(
                "Todo={id=%s, name='%s', completed=%b, createdAt=%s}",
                id, name, completed, getFormattedDate()
        );
    }

}
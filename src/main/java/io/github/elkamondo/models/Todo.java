package io.github.elkamondo.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Todo implements Comparable<Todo> {

    private final String id;
    private String name;
    private boolean completed;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Todo(String name) {
        this.name = name;
        this.id = generateId();
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

    private String generateId() {
        String hash = Integer.toString(Math.abs(hashCode()));
        return hash.substring(Math.max(0, hash.length() - 5));
    }

}
package io.github.elkamondo.models;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public Set<Todo> getTodos() {
        return todos;
    }

    public boolean isEmpty() {
        return todos.isEmpty();
    }

    @Override
    public String toString() {
        return todos.toString();
    }

    public boolean addTodo(Todo todo) {
        if (todo == null) {
            return false;
        }

        return todos.add(todo);
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

    public void showAllTodos() {
        if (todos.isEmpty()) {
            System.out.println("No todos.");
            return;
        }

        printTodos(todos);
    }

    public void showActiveTodos() {
        Predicate<Todo> isCompleted = Todo::isCompleted;
        TreeSet<Todo> activeTodos = todos.stream()
                .filter(isCompleted.negate())
                .collect(Collectors.toCollection(() -> new TreeSet<>(BY_NAME_THEN_DATE)));

        if (activeTodos.isEmpty()) {
            System.out.println("No active todos.");
        } else {
            printTodos(activeTodos);
        }
    }

    public void showCompletedTodos() {
        TreeSet<Todo> completedTodos = todos.stream()
                .filter(Todo::isCompleted)
                .collect(Collectors.toCollection(() -> new TreeSet<>(BY_NAME_THEN_DATE)));

        if (completedTodos.isEmpty()) {
            System.out.println("No completed todos.");
        } else {
            printTodos(completedTodos);
        }
    }

    public void saveTodos() throws IOException {
        Path path = Paths.get("todos.backup");
        if (Files.notExists(path)) {
            Files.createFile(path);
        }

        String content =
                todos.stream()
                        .map(todo ->
                                String.format(
                                        "%s | %s | %b | %s",
                                        todo.getId(), todo.getName(), todo.isCompleted(), todo.getFormattedDate()
                                )
                        )
                        .collect(Collectors.joining("\n"));

        Files.write(path, content.getBytes());
        System.out.println("Your todos has been saved successfully.");
    }

    public void retrieveStoredTodos() {
        Path path = Paths.get("todos.backup");
        if (Files.exists(path)) {
            try {
                TodoList persistedData = new TodoList();
                List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                lines.stream()
                        .forEach(line -> {
                            StringTokenizer st = new StringTokenizer(line, "|");
                            while (st.hasMoreTokens()) {
                                String id = st.nextToken().trim();
                                String name = st.nextToken().trim();
                                Boolean completed = Boolean.valueOf(st.nextToken().trim());
                                DateTimeFormatter formatter =
                                        DateTimeFormatter.ofPattern("dd/MM/yyyy H:m:s");
                                LocalDateTime createdAt = LocalDateTime.parse(
                                        st.nextToken().trim(),
                                        formatter
                                );

                                persistedData.addTodo(new Todo(id, name, completed, createdAt));
                            }
                        });
                todos.addAll(persistedData.getTodos());
            } catch (IOException e) {
            }
        }
    }

    private void printTodos(Collection<? extends Todo> todos) {
        printTableHeader();
        todos.forEach(todo ->
                System.out.printf("| %s | %-30s | %-9b | %s |%n",
                        todo.getId(), todo.getName(), todo.isCompleted(), todo.getFormattedDate())
        );
        System.out.println("+-------+--------------------------------+-----------+--------------------+");
    }

    private void printTableHeader() {
        System.out.println("+-------+--------------------------------+-----------+--------------------+");
        System.out.printf("| %-5s | %-30s | %-5s | %-18s |%n", "id", "name", "completed", "createdAt");
        System.out.println("+-------+--------------------------------+-----------+--------------------+");
    }

}
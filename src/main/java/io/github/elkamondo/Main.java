package io.github.elkamondo;

import io.github.elkamondo.exceptions.FileNameNotValidException;
import io.github.elkamondo.models.Todo;
import io.github.elkamondo.models.TodoList;
import io.github.elkamondo.utils.reports.TodoCSVReporter;
import io.github.elkamondo.utils.reports.TodoReporter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Scanner;
import java.util.function.BiFunction;

import static io.github.elkamondo.utils.Constants.DEFAULT_DATETIME_FORMATTER;

public class Main {

    public static void main(String[] args) {
        final String BACKUP_FILENAME = "todos.csv";

        final TodoReporter csvReporter = new TodoCSVReporter();

        final TodoList todos = new TodoList();
        try {
            todos.addAll(csvReporter.load(BACKUP_FILENAME));
        } catch (IOException | FileNameNotValidException e) {
            System.err.printf("Can't load data! from file '%s'.%n", BACKUP_FILENAME);
        }

        try (Scanner in = new Scanner(System.in)) {
            int userChoice = 0;

            do {
                showMenu();

                try {
                    userChoice = Integer.parseInt(in.nextLine());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid input! You must enter an integer.");
                    continue;
                }

                switch (userChoice) {
                    case 1: {
                        System.out.print("What do you want to do? ");
                        final String todoName = in.nextLine();
                        if (todos.add(new Todo(todoName))) {
                            System.out.println("Todo has been added.");
                        }
                    }
                    break;

                    case 2: {
                        final Collection<? extends Todo> activeTodos = todos.getActiveTodos();
                        printTodos(activeTodos);

                        if (activeTodos.size() > 0) {
                            System.out.printf("%nWhich one do you want to complete?%n");

                            final String todoId = prompt(in);
                            if (todoId.isEmpty()) {
                                System.out.println("Todo's name should not be empty");
                                continue;
                            }

                            if (todos.completeTodo(todoId)) {
                                System.out.println("Todo has been completed.");
                            } else {
                                System.out.println("There is no todo associated with that id.");
                            }
                        }
                    }
                    break;

                    case 3: {
                        final Collection<? extends Todo> allTodos = todos.getAllTodos();
                        printTodos(allTodos);

                        if (allTodos.size() > 0) {
                            System.out.printf("%nWhich one do you want to remove?%n");

                            final String todoId = prompt(in);
                            if (todoId.isEmpty()) continue;

                            if (todos.removeTodo(todoId)) {
                                System.out.println("Todo has been removed.");
                            } else {
                                System.out.println("There is no todo associated with that id.");
                            }
                        }
                    }
                    break;

                    case 4: {
                        final Collection<? extends Todo> allTodos = todos.getAllTodos();
                        printTodos(allTodos);

                        if (allTodos.size() > 0) {
                            System.out.printf("%nWhich one do you want to rename?%n");

                            final String todoId = prompt(in);
                            if (todoId.isEmpty()) continue;

                            System.out.print("Enter the new name -> ");
                            final String todoNewName = in.nextLine();
                            if (todoNewName.isEmpty()) continue;

                            if (todos.renameTodo(todoId, todoNewName)) {
                                System.out.println("Todo has been renamed.");
                            } else {
                                System.out.println("There is no todo associated with that id.");
                            }
                        }
                    }
                    break;

                    case 5:
                        printTodos(todos.getAllTodos());
                        break;

                    case 6:
                        printTodos(todos.getActiveTodos());
                        break;

                    case 7:
                        printTodos(todos.getCompletedTodos());
                        break;

                    case 8:
                        System.out.println("Bye!");
                        break;

                    default:
                        System.err.println("Please enter a valid option!");
                }
            } while (userChoice != 8);
        }

        try {
            if (todos.isEmpty()) {
                System.out.println("There is no todos to save.");
            } else {
                csvReporter.save(todos.getAllTodos(), BACKUP_FILENAME);
            }
        } catch (IOException | FileNameNotValidException e) {
            System.err.println("Can't backup your data!");
        }
    }

    private static String prompt(Scanner in) {
        System.out.print("Enter the id -> ");

        final String todoId = in.next();
        in.nextLine();                  // Consume the newline character

        return todoId;
    }

    private static void showMenu() {
        System.out.printf("%n-------------------------%n");
        System.out.println("Menu:");
        System.out.println(" 1) Add todo");
        System.out.println(" 2) Complete todo");
        System.out.println(" 3) Remove todo");
        System.out.println(" 4) Rename todo");
        System.out.println(" 5) Show all todos");
        System.out.println(" 6) Show active todos");
        System.out.println(" 7) Show completed todos");
        System.out.println(" 8) Quit");
        System.out.printf("%n> ");
    }

    private static void printTodos(Collection<? extends Todo> todos) {
        if (todos.isEmpty()) {
            System.out.println("No todos found.");
            return;
        }

        // Print table header
        final String tableBorder = "+----------+--------------------------------+-----------+---------------------+---------------------+";
        final String tableRowFormat = "| %-8s | %-30s | %-9s | %-19s | %-19s |%n";
        System.out.println(tableBorder);
        System.out.printf(tableRowFormat, "id", "name", "completed", "createdAt", "completedAt");
        System.out.println(tableBorder);

        final BiFunction<String, Integer, String> truncate =
                (string, maxWidth) ->
                        string.length() > maxWidth
                                ? string.substring(0, maxWidth - 3) + "..."
                                : string;

        final BiFunction<LocalDateTime, DateTimeFormatter, String> formatDate =
                (dateTime, formatter) -> dateTime != null ? dateTime.format(formatter) : null;

        todos.forEach(todo ->
                System.out.printf(tableRowFormat,
                        todo.getId(), truncate.apply(todo.getName(), 30), todo.isCompleted() ? "✅" : "❌",
                        formatDate.apply(todo.getCreatedAt(), DEFAULT_DATETIME_FORMATTER),
                        formatDate.apply(todo.getCompletedAt(), DEFAULT_DATETIME_FORMATTER)
                )
        );
        System.out.println(tableBorder);
    }

}
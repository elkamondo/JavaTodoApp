package io.github.elkamondo;

import io.github.elkamondo.exceptions.FileNameNotValidException;
import io.github.elkamondo.models.Todo;
import io.github.elkamondo.models.TodoList;
import io.github.elkamondo.utils.reports.TodoCSVReporter;
import io.github.elkamondo.utils.reports.TodoReporter;

import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

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
                        show(todos.getActiveTodos());
                        System.out.printf("%nWhich one do you want to complete?%n");

                        final String todoId = prompt(in);
                        if (todoId.isEmpty()) continue;

                        if (todos.completeTodo(todoId)) {
                            System.out.println("Todo has been completed.");
                        } else {
                            System.out.println("There is no todo associated with that id.");
                        }
                    }
                    break;

                    case 3: {
                        show(todos.getAllTodos());
                        System.out.printf("%nWhich one do you want to remove?%n");

                        final String todoId = prompt(in);
                        if (todoId.isEmpty()) continue;

                        if (todos.removeTodo(todoId)) {
                            System.out.println("Todo has been removed.");
                        } else {
                            System.out.println("There is no todo associated with that id.");
                        }
                    }
                    break;

                    case 4:
                        show(todos.getAllTodos());
                        break;

                    case 5:
                        show(todos.getActiveTodos());
                        break;

                    case 6:
                        show(todos.getCompletedTodos());
                        break;

                    case 7:
                        System.out.println("Bye!");
                        break;

                    default:
                        System.err.println("Please enter a valid option!");
                }
            } while (userChoice != 7);
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

        if (!todoId.matches("^[0-9]+$")) {
            System.err.println("Invalid input! You must enter an integer.");
            return "";
        }

        return todoId;
    }

    private static void showMenu() {
        System.out.printf("%n-------------------------%n");
        System.out.println("Menu:");
        System.out.println(" 1) Add todo");
        System.out.println(" 2) Complete todo");
        System.out.println(" 3) Remove todo");
        System.out.println(" 4) Show all todos");
        System.out.println(" 5) Show active todos");
        System.out.println(" 6) Show completed todos");
        System.out.println(" 7) Quit");
        System.out.printf("%n> ");
    }

    private static void show(Collection<? extends Todo> todos) {
        if (todos.isEmpty()) {
            System.out.println("No todos found.");
            return;
        }

        // Print table header
        System.out.println("+-------+--------------------------------+-----------+--------------------+");
        System.out.printf("| %-5s | %-30s | %-5s | %-18s |%n",
                "id", "name", "completed", "createdAt");
        System.out.println("+-------+--------------------------------+-----------+--------------------+");

        todos.forEach(todo ->
                System.out.printf("| %s | %-30s | %-9b | %s |%n",
                        todo.getId(), todo.getName(), todo.isCompleted(), todo.getFormattedDate())
        );
        System.out.println("+-------+--------------------------------+-----------+--------------------+");
    }

}
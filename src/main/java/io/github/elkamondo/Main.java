package io.github.elkamondo;

import io.github.elkamondo.exceptions.FileNameNotValidException;
import io.github.elkamondo.models.Todo;
import io.github.elkamondo.models.TodoList;
import io.github.elkamondo.utils.TodoUtils;

import java.io.IOException;
import java.util.Scanner;

import static io.github.elkamondo.utils.TodoUtils.loadFromCSV;

public class Main {

  public static void main(String[] args) {
    final String BACKUP_FILENAME = "";
    TodoList todos = new TodoList();
    try {
      todos.addAll(loadFromCSV(BACKUP_FILENAME));
    } catch (IOException e) {
      System.err.println(e.getMessage());
    } catch (FileNameNotValidException e) {
      System.err.println(e.getMessage());
    }

    Scanner in = new Scanner(System.in);
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
          System.out.print("Enter the name -> ");
          String todoName = in.nextLine();
          if (todos.add(new Todo(todoName))) {
            System.out.println("Todo has been added.");
          }
        }
          break;

        case 2: {
          TodoUtils.print(todos.getActiveTodos());
          System.out.println("\nWhich one do you want to complete?");
          System.out.print("Enter the id -> ");

          String todoId = in.next();
          in.nextLine();    // Consume the newline character

          if (!todoId.matches("^[0-9]+$")) {
            System.err.println("Invalid input! You must enter an integer.");
            continue;
          }

          if (todos.completeTodo(todoId)) {
            System.out.println("Todo has been completed.");
          } else {
            System.out.println("There is no todo associated with that id.");
          }
        }
          break;

        case 3: {
          TodoUtils.print(todos.getAllTodos());
          System.out.println("\nWhich one do you want to remove?");
          System.out.print("Enter the id -> ");

          String todoId = in.next();
          in.nextLine();    // Consume the newline character

          if (!todoId.matches("^[0-9]+$")) {
            System.err.println("Invalid input! You must enter an integer.");
            continue;
          }

          if (todos.removeTodo(todoId)) {
            System.out.println("Todo has been removed.");
          } else {
            System.out.println("There is no todo associated with that id.");
          }
        }
          break;

        case 4:
          TodoUtils.print(todos.getAllTodos());
          break;

        case 5:
          TodoUtils.print(todos.getActiveTodos());
          break;

        case 6:
          TodoUtils.print(todos.getCompletedTodos());
          break;

        case 7:
          System.out.println("Bye!");
          break;

        default:
          userChoice = 7;
          System.out.println("Bye!");
          break;
      }
    } while (userChoice != 7);

    in.close();

    try {
      if (todos.isEmpty()) {
        System.out.println("There is no todos to save.");
      } else {
        TodoUtils.saveAsCSV(todos.getAllTodos(), BACKUP_FILENAME);
      }
    } catch (IOException e) {
      System.err.println("Can't backup your data!");
    } catch (FileNameNotValidException e) {
      e.printStackTrace();
    }
  }

  private static void showMenu() {
    System.out.println("\n------------------------");
    System.out.println("Menu:");
    System.out.println(" 1) Add todo");
    System.out.println(" 2) Complete todo");
    System.out.println(" 3) Remove todo");
    System.out.println(" 4) Show all todos");
    System.out.println(" 5) Show active todos");
    System.out.println(" 6) Show completed todos");
    System.out.println(" 7) Quit");
    System.out.print("\n> ");
  }
}
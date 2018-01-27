import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {

    TodoList todos = new TodoList(getListOfTodos());
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
          if (todos.addTodo(new Todo(todoName))) {
            System.out.println("Todo has been added");
          }
        }
          break;

        case 2: {
          todos.showActiveTodos();
          System.out.println("\nWhich one do you want to complete?");
          System.out.print("Enter the id -> ");
          String todoId = in.next();
          in.nextLine();    // Consume the newline character
          if (todos.completeTodo(todoId)) {
            System.out.println("Todo has been completed");
          }
        }
          break;

        case 3: {
          todos.showAllTodos();
          System.out.println("\nWhich one do you want to remove?");
          System.out.print("Enter the id -> ");
          String todoId = in.next();
          in.nextLine();    // Consume the newline character
          if (todos.removeTodo(todoId)) {
            System.out.println("Todo has been removed");
          }
        }
          break;

        case 4:
          todos.showAllTodos();
          break;

        case 5:
          todos.showActiveTodos();
          break;

        case 6:
          todos.showCompletedTodos();
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

  private static List<Todo> getListOfTodos() {
    Todo todo1 = new Todo("Learn Git");
    Todo todo2 = new Todo("Buy milk");
    Todo todo3 = new Todo("Learn Java8");

    return Arrays.asList(todo1, todo2, todo3);
  }
}
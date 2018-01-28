import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TodoList {

  private Set<Todo> todos = new TreeSet<>(
    Comparator.comparing(Todo::getCreatedAt)
              .thenComparing(Todo::getName)
  );

  public TodoList() {
  }

  public TodoList(Collection<? extends Todo> collection) {
    if (collection != null) {
      todos.addAll(collection);
    }
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

    todos.stream()
         .filter(todo -> todoId.equals(todo.getId()))
         .findFirst()
         .get()
         .complete();

    return true;
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

    printTableHeader();
    todos.forEach(Todo::print);
  }

  public void showActiveTodos() {
    Predicate<Todo> isCompleted = Todo::isCompleted;
    TreeSet<Todo> activeTodos = todos.stream()
      .filter(isCompleted.negate())
      .collect(Collectors.toCollection(TreeSet::new));

    if (activeTodos.isEmpty()) {
      System.out.println("No active todos.");
    } else {
      printTableHeader();
      activeTodos.forEach(Todo::print);
    }
  }

  public void showCompletedTodos() {
    TreeSet<Todo> completedTodos = todos.stream()
      .filter(Todo::isCompleted)
      .collect(Collectors.toCollection(TreeSet::new));

    if (completedTodos.isEmpty()) {
      System.out.println("No completed todos.");
    } else {
      printTableHeader();
      completedTodos.forEach(Todo::print);
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
              "%s | %b | %s",
              todo.getName(), todo.isCompleted(), todo.getFormattedDate()
            )
          )
          .collect(Collectors.joining("\n"));

    Files.write(path, content.getBytes());
    System.out.println("Your todos has been saved successfully.");
  }

  private void printTableHeader() {
    System.out.printf(
     "  %-5s | %-15s | %-5s | %s\n",
     "id", "name", "completed", "createdAt"
    );
    System.out.println("--------------------------------------------------");
  }
}
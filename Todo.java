import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Todo implements Comparable<Todo> {

  private String id;
  private String name;
  private boolean completed;
  private LocalDate createdAt = LocalDate.now();

  public Todo(String name) {
    this.name = name;
    this.id = String.valueOf(Math.abs(hashCode())).substring(0, 5);
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

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public String getFormattedDate() {
    return createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
  }

  public boolean isCompleted() {
    return completed;
  }

  public void complete() {
   this.completed = true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, completed, createdAt);
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

    Todo todo = (Todo)obj;
    return Objects.equals(id, todo.id);
  }

  @Override
  public int compareTo(Todo other) {
    return name.compareTo(other.name);
  }

  @Override
  public String toString() {
    return String.format(
      "Todo={id=%s, name='%s', completed=%b, createdAt=%s}",
      id, name, completed, getFormattedDate()
    );
  }

  public void print() {
    System.out.println(
      String.format(
        "  %s | %-15s | %-9b | %s",
        id, name, completed, getFormattedDate()
      )
    );
  }
}
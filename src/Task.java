import java.io.*;
import java.time.DayOfWeek;
import java.util.*;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private Set<DayOfWeek> days;
    private boolean inLoop;

    public Task(String title, String description, Set<DayOfWeek> days, boolean inLoop) {
        this.title = title;
        this.description = description;
        this.days = new HashSet<>(days);
        this.inLoop = inLoop;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Set<DayOfWeek> getDays() { return new HashSet<>(days); }
    public boolean isInLoop() { return inLoop; }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", days=" + days +
                ", inLoop=" + inLoop +
                '}';
    }
}

import java.io.*;
import java.time.DayOfWeek;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private DayOfWeek day; // ahora solo UN día
    private boolean inLoop;

    public Task(String title, String description, DayOfWeek day, boolean inLoop) {
        this.title = title;
        this.description = description;
        this.day = day;
        this.inLoop = inLoop;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public DayOfWeek getDay() { return day; }
    public boolean isInLoop() { return inLoop; }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", day=" + day +
                ", inLoop=" + inLoop +
                '}';
    }
}

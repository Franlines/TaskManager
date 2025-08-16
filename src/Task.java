import java.io.*;
import java.time.DayOfWeek;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private DayOfWeek day; // ahora solo UN día


    public Task(String title, String description, DayOfWeek day) {
        this.title = title;
        this.description = description;
        this.day = day;

    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public DayOfWeek getDay() { return day; }


    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", day=" + day +
                '}';
    }
}

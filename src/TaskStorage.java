import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskStorage {

    private static final String FILE_NAME = "tasks.dat";

    public static void saveTasks(List<Task> tasks) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(tasks);
        }
    }

    public static List<Task> loadTasks() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Task>) in.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
    }
}

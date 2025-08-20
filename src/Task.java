import java.awt.Color;
import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class Task implements Serializable {
    private static final long serialVersionUID = 2L; // Incrementado por el nuevo campo

    private String title;
    private String description;
    private DayOfWeek day;
    private boolean inLoop;
    private LocalTime startTime;
    private LocalTime endTime;
    private Color color; // Nuevo campo para el color

    public Task(String title, String description, DayOfWeek day, boolean inLoop,
                LocalTime startTime, LocalTime endTime) {
        this.title = title;
        this.description = description;
        this.day = day;
        this.inLoop = inLoop;
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = new Color(70, 130, 180); // Color por defecto (Steel Blue)
    }

    // Constructor con color
    public Task(String title, String description, DayOfWeek day, boolean inLoop,
                LocalTime startTime, LocalTime endTime, Color color) {
        this(title, description, day, inLoop, startTime, endTime);
        this.color = color;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public DayOfWeek getDay() { return day; }
    public boolean isInLoop() { return inLoop; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public Color getColor() { return color; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDay(DayOfWeek day) { this.day = day; }
    public void setInLoop(boolean inLoop) { this.inLoop = inLoop; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public void setColor(Color color) { this.color = color; }

    // Método para obtener el color en formato hexadecimal (útil para debugging)
    public String getColorHex() {
        return String.format("#%06X", color.getRGB() & 0xFFFFFF);
    }

    // Método para verificar si el color es oscuro (para determinar el color del texto)
    public boolean isDarkColor() {
        // Calcular la luminancia del color
        double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
        return luminance < 0.5;
    }

    // Método para obtener el color del texto basado en el color de fondo
    public Color getTextColor() {
        return isDarkColor() ? Color.WHITE : Color.BLACK;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", day=" + day +
                ", inLoop=" + inLoop +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", color=" + getColorHex() +
                '}';
    }

    // Método para calcular la duración de la tarea en minutos
    public long getDurationMinutes() {
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }

    // Método para verificar si dos tareas se solapan en tiempo
    public boolean overlapsWith(Task other) {
        if (!this.day.equals(other.day)) {
            return false;
        }

        return !(this.endTime.isBefore(other.startTime) ||
                this.startTime.isAfter(other.endTime));
    }
}
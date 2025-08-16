import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.DayOfWeek;
import java.util.*;
import java.util.List;

public class AddTaskWindow extends JFrame {

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JCheckBox[] dayCheckboxes;
    private JCheckBox loopCheckbox;
    private Calendar calendar; // referencia al calendario

    public AddTaskWindow(Calendar calendar) {
        this.calendar = calendar;
        setTitle("Add Task");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de inputs
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 5, 5));

        // Título
        formPanel.add(new JLabel("Título:"));
        titleField = new JTextField();
        formPanel.add(titleField);

        // Descripción
        formPanel.add(new JLabel("Descripción:"));
        descriptionArea = new JTextArea(4, 20);
        JScrollPane scroll = new JScrollPane(descriptionArea);
        formPanel.add(scroll);

        // Selección de días
        formPanel.add(new JLabel("Días de la semana:"));
        JPanel daysPanel = new JPanel(new GridLayout(2, 4));
        DayOfWeek[] days = DayOfWeek.values();
        dayCheckboxes = new JCheckBox[days.length];
        for (int i = 0; i < days.length; i++) {
            dayCheckboxes[i] = new JCheckBox(days[i].toString());
            daysPanel.add(dayCheckboxes[i]);
        }
        formPanel.add(daysPanel);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Botón crear
        JButton createButton = new JButton("Crear Tarea");
        createButton.addActionListener((ActionEvent e) -> {
            List<Task> newTasks = createTasksFromForm();
            if (newTasks != null && !newTasks.isEmpty()) {
                try {
                    List<Task> tasks = TaskStorage.loadTasks();
                    tasks.addAll(newTasks); // añadimos todas
                    TaskStorage.saveTasks(tasks);

                    // Actualizar el calendario
                    if (calendar != null) {
                        calendar.refreshTasks();
                    }

                    JOptionPane.showMessageDialog(AddTaskWindow.this,
                            "Tareas creadas y guardadas:\n" + newTasks,
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AddTaskWindow.this,
                            "Error al guardar la tarea: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        mainPanel.add(createButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private List<Task> createTasksFromForm() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título y descripción son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        List<Task> newTasks = new ArrayList<>();
        DayOfWeek[] days = DayOfWeek.values();

        boolean inLoop = loopCheckbox.isSelected();

        for (int i = 0; i < dayCheckboxes.length; i++) {
            if (dayCheckboxes[i].isSelected()) {
                // una tarea por cada día seleccionado
                newTasks.add(new Task(title, description, days[i]));
            }
        }

        if (newTasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar al menos un día", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return newTasks;
    }
}

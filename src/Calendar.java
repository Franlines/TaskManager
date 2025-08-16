import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.util.List;

public class Calendar extends JFrame {

    private JPanel[] dayPanels;

    public Calendar() {
        setTitle("Weekly Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel weekPanel = new JPanel(new GridLayout(1, 7, 5, 5));
        String[] days = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
        dayPanels = new JPanel[7];

        for (int i = 0; i < days.length; i++) {
            JPanel dayPanel = new JPanel();
            dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
            dayPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            JLabel dayLabel = new JLabel(days[i], JLabel.CENTER);
            dayLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            dayPanel.add(dayLabel);

            dayPanels[i] = dayPanel;
            weekPanel.add(dayPanel);
        }

        mainPanel.add(weekPanel, BorderLayout.CENTER);

        // Botón Add Task con doble altura
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.setPreferredSize(new Dimension(0, 80)); // altura 80px
        addTaskButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        addTaskButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addTaskButton.addActionListener(e -> {
            AddTaskWindow addTaskWindow = new AddTaskWindow(this); // pasamos referencia al calendario
            addTaskWindow.setVisible(true);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createVerticalStrut(10)); // margen superior
        buttonPanel.add(addTaskButton);
        buttonPanel.add(Box.createVerticalStrut(10)); // margen inferior

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        loadTasksIntoCalendar();
    }

    private void loadTasksIntoCalendar() {
        try {
            List<Task> tasks = TaskStorage.loadTasks(); // cargar siempre desde archivo

            for (Task task : tasks) {
                DayOfWeek day = task.getDay(); // ahora es solo un día
                int index = day.getValue() - 1; // Lunes=1 → index 0

                JButton taskButton = new JButton(task.getTitle());
                taskButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                taskButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

                taskButton.addActionListener(e -> {
                    JDialog dialog = new JDialog(this, task.getTitle(), true);
                    dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
                    dialog.setSize(300, 200);
                    dialog.setLocationRelativeTo(this);

                    JLabel descriptionLabel = new JLabel("<html>" + task.getDescription() + "</html>");
                    descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    dialog.add(Box.createVerticalStrut(10));
                    dialog.add(descriptionLabel);
                    dialog.add(Box.createVerticalStrut(20));

                    // Botón eliminar tarea
                    JButton deleteButton = new JButton("Eliminar tarea");
                    deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    deleteButton.addActionListener(ev -> {
                        int confirm = JOptionPane.showConfirmDialog(dialog,
                                "¿Seguro que quieres eliminar esta tarea?",
                                "Confirmar eliminación",
                                JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            try {
                                // Volvemos a cargar la lista desde almacenamiento
                                List<Task> currentTasks = TaskStorage.loadTasks();
                                currentTasks.removeIf(t ->
                                        t.getTitle().equals(task.getTitle()) &&
                                                t.getDescription().equals(task.getDescription()) &&
                                                t.getDay().equals(task.getDay())
                                );
                                TaskStorage.saveTasks(currentTasks);
                                refreshTasks(); // refrescamos calendario
                                dialog.dispose();
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(this,
                                        "Error al eliminar la tarea: " + ex.getMessage(),
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });

                    dialog.add(deleteButton);

                    dialog.setVisible(true);
                });

                dayPanels[index].add(Box.createVerticalStrut(5));
                dayPanels[index].add(taskButton);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando tareas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    public void refreshTasks() {
        // Primero limpiamos los botones de tareas existentes
        for (JPanel dayPanel : dayPanels) {
            // Eliminamos todos los componentes excepto la etiqueta del día
            Component[] components = dayPanel.getComponents();
            for (int i = components.length - 1; i >= 0; i--) {
                if (!(components[i] instanceof JLabel)) {
                    dayPanel.remove(components[i]);
                }
            }
        }
        loadTasksIntoCalendar();
        revalidate();
        repaint();
    }

}

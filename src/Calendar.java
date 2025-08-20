import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public class Calendar extends JFrame {

    private JPanel[][] timeSlots; // [día][slot de tiempo]
    private static final int HOURS_START = 8;
    private static final int HOURS_END = 20;
    private static final int SLOTS_PER_HOUR = 1; // cada hora completa
    private static final int TOTAL_SLOTS = (HOURS_END - HOURS_START) * SLOTS_PER_HOUR;

    public Calendar() {
        setTitle("Weekly Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // ---------------- MENÚ SUPERIOR ----------------
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(240, 240, 240));

        Dimension buttonSize = new Dimension(120, 40);

        JMenuItem newTaskItem = new JMenuItem("Nueva Tarea");
        newTaskItem.setPreferredSize(buttonSize);
        newTaskItem.addActionListener(e -> {
            AddTaskWindow addTaskWindow = new AddTaskWindow(this);
            addTaskWindow.setVisible(true);
        });
        menuBar.add(newTaskItem);

        JMenuItem settingsItem = new JMenuItem("Ajustes");
        settingsItem.setPreferredSize(buttonSize);
        settingsItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Aquí irían los ajustes de la aplicación",
                    "Ajustes",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        menuBar.add(settingsItem);

        JMenuItem exitItem = new JMenuItem("Salir");
        exitItem.setPreferredSize(buttonSize);
        exitItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que quieres salir?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        menuBar.add(exitItem);

        menuBar.add(Box.createHorizontalGlue());
        setJMenuBar(menuBar);
        // -------------------------------------------------

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Panel principal del calendario
        JPanel calendarPanel = createCalendarGrid();
        JScrollPane scrollPane = new JScrollPane(calendarPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Botón Add Task abajo
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.setPreferredSize(new Dimension(0, 50));
        addTaskButton.setBackground(new Color(70, 130, 180));
        addTaskButton.setForeground(Color.WHITE);
        addTaskButton.setFont(new Font("Arial", Font.BOLD, 16));
        addTaskButton.addActionListener(e -> {
            AddTaskWindow addTaskWindow = new AddTaskWindow(this);
            addTaskWindow.setVisible(true);
        });

        mainPanel.add(addTaskButton, BorderLayout.SOUTH);
        add(mainPanel);

        loadTasksIntoCalendar();
    }

    private JPanel createCalendarGrid() {
        JPanel calendarPanel = new JPanel();
        calendarPanel.setLayout(new BorderLayout());
        calendarPanel.setBackground(Color.WHITE);

        // Crear header con los días de la semana
        JPanel headerPanel = new JPanel(new GridLayout(1, 8));
        headerPanel.setBackground(new Color(230, 230, 230));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY));

        // Celda vacía para la columna de horas
        JLabel emptyHeader = new JLabel("Hora", SwingConstants.CENTER);
        emptyHeader.setFont(new Font("Arial", Font.BOLD, 14));
        emptyHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        headerPanel.add(emptyHeader);

        // Headers de los días
        String[] dayNames = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        for (String dayName : dayNames) {
            JLabel dayLabel = new JLabel(dayName, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
            dayLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
            headerPanel.add(dayLabel);
        }

        calendarPanel.add(headerPanel, BorderLayout.NORTH);

        // Crear el grid principal
        JPanel gridPanel = new JPanel(new GridLayout(TOTAL_SLOTS, 8));
        gridPanel.setBackground(Color.WHITE);

        // Inicializar array de slots de tiempo
        timeSlots = new JPanel[7][TOTAL_SLOTS];

        // Crear filas para cada slot de tiempo
        for (int slot = 0; slot < TOTAL_SLOTS; slot++) {
            // Columna de horas
            int hour = HOURS_START + slot;

            JLabel timeLabel = new JLabel(String.format("%02d:00", hour), SwingConstants.CENTER);
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            timeLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.LIGHT_GRAY));
            timeLabel.setBackground(new Color(248, 248, 248));
            timeLabel.setOpaque(true);
            gridPanel.add(timeLabel);

            // Celdas para cada día
            for (int day = 0; day < 7; day++) {
                JPanel cell = new JPanel();
                cell.setLayout(new BorderLayout());
                cell.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.LIGHT_GRAY));
                cell.setBackground(Color.WHITE);
                cell.setPreferredSize(new Dimension(120, 40)); // Aumentamos la altura

                timeSlots[day][slot] = cell;
                gridPanel.add(cell);
            }
        }

        calendarPanel.add(gridPanel, BorderLayout.CENTER);
        return calendarPanel;
    }

    private void loadTasksIntoCalendar() {
        try {
            List<Task> tasks = TaskStorage.loadTasks();

            for (Task task : tasks) {
                addTaskToCalendar(task);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando tareas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTaskToCalendar(Task task) {
        DayOfWeek day = task.getDay();
        int dayIndex = day.getValue() - 1; // lunes=0

        LocalTime startTime = task.getStartTime();
        LocalTime endTime = task.getEndTime();

        // Calcular slots de inicio y fin
        int startSlot = getTimeSlot(startTime);
        int endSlot = getTimeSlot(endTime);

        if (startSlot < 0 || endSlot > TOTAL_SLOTS || startSlot >= endSlot) {
            return; // Fuera de rango o tiempo inválido
        }

        // Crear el botón de la tarea
        JButton taskButton = new JButton();
        taskButton.setText("<html><center>" + task.getTitle() + "<br>" +
                startTime.toString() + " - " + endTime.toString() + "</center></html>");
        taskButton.setBackground(task.getColor());
        taskButton.setForeground(task.getTextColor()); // Usar el color de texto apropiado
        taskButton.setFont(new Font("Arial", Font.BOLD, 11));
        taskButton.setOpaque(true);
        taskButton.setBorderPainted(false);
        taskButton.setFocusPainted(false);

        // Agregar tooltip con descripción
        if (task.getDescription() != null && !task.getDescription().trim().isEmpty()) {
            taskButton.setToolTipText(task.getDescription());
        }

        // Calcular altura del botón basada en duración
        int slotHeight = 40; // Aumentado porque ahora cada slot es una hora
        int buttonHeight = (endSlot - startSlot) * slotHeight;

        // Limpiar las celdas que ocupará la tarea
        for (int slot = startSlot; slot < endSlot; slot++) {
            timeSlots[dayIndex][slot].removeAll();
            timeSlots[dayIndex][slot].setBackground(Color.WHITE);
        }

        // Colocar el botón en la primera celda y configurar su tamaño
        JPanel startCell = timeSlots[dayIndex][startSlot];
        startCell.setLayout(new BorderLayout());
        startCell.add(taskButton, BorderLayout.CENTER);

        // Hacer que las celdas siguientes sean invisibles visualmente
        for (int slot = startSlot + 1; slot < endSlot; slot++) {
            timeSlots[dayIndex][slot].setBackground(task.getColor());
        }

        // Configurar el tamaño preferido del botón
        taskButton.setPreferredSize(new Dimension(120, buttonHeight));
    }

    private int getTimeSlot(LocalTime time) {
        int hour = time.getHour();

        if (hour < HOURS_START || hour >= HOURS_END) {
            return -1; // Fuera del rango
        }

        return hour - HOURS_START;
    }

    private Color getTaskColor(Task task) {
        // Colores diferentes basados en el tipo de tarea o día
        Color[] colors = {
                new Color(70, 130, 180),   // Steel Blue
                new Color(60, 179, 113),   // Medium Sea Green
                new Color(255, 140, 0),    // Dark Orange
                new Color(147, 112, 219),  // Medium Purple
                new Color(220, 20, 60),    // Crimson
                new Color(0, 139, 139),    // Dark Cyan
                new Color(184, 134, 11)    // Dark Goldenrod
        };

        return colors[task.getDay().getValue() % colors.length];
    }

    public void refreshTasks() {
        // Limpiar todas las celdas
        for (int day = 0; day < 7; day++) {
            for (int slot = 0; slot < TOTAL_SLOTS; slot++) {
                timeSlots[day][slot].removeAll();
                timeSlots[day][slot].setBackground(Color.WHITE);
                timeSlots[day][slot].setLayout(new BorderLayout());
            }
        }

        // Recargar las tareas
        loadTasksIntoCalendar();
        revalidate();
        repaint();
    }

    // Método para obtener conflictos de horario (útil para validación)
    public boolean hasTimeConflict(Task newTask) {
        try {
            List<Task> existingTasks = TaskStorage.loadTasks();

            for (Task task : existingTasks) {
                if (task.getDay() == newTask.getDay()) {
                    // Verificar si hay solapamiento
                    boolean overlap = !(newTask.getEndTime().isBefore(task.getStartTime()) ||
                            newTask.getStartTime().isAfter(task.getEndTime()));
                    if (overlap) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
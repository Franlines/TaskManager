import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

public class AddTaskWindow extends JFrame {

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JCheckBox[] dayCheckboxes;
    private JSpinner startTimeSpinner;
    private JSpinner endTimeSpinner;
    private JPanel colorPreview;
    private Color selectedColor;
    private Calendar calendar; // referencia al calendario

    // Colores predefinidos
    private static final Color[] PRESET_COLORS = {
            new Color(70, 130, 180),    // Steel Blue
            new Color(60, 179, 113),    // Medium Sea Green
            new Color(255, 140, 0),     // Dark Orange
            new Color(147, 112, 219),   // Medium Purple
            new Color(220, 20, 60),     // Crimson
            new Color(0, 139, 139),     // Dark Cyan
            new Color(184, 134, 11),    // Dark Goldenrod
            new Color(255, 99, 71),     // Tomato
            new Color(75, 0, 130),      // Indigo
            new Color(34, 139, 34),     // Forest Green
            new Color(255, 20, 147),    // Deep Pink
            new Color(139, 69, 19)      // Saddle Brown
    };

    public AddTaskWindow(Calendar calendar) {
        this.calendar = calendar;
        this.selectedColor = PRESET_COLORS[0]; // Color por defecto

        setTitle("Agregar Nueva Tarea");
        setSize(450, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        // Panel de inputs
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Título
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        JLabel titleLabel = new JLabel("Título:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(titleLabel, gbc);

        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        titleField = new JTextField();
        titleField.setPreferredSize(new Dimension(0, 30));
        titleField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(titleField, gbc);

        // Descripción
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        JLabel descLabel = new JLabel("Descripción:");
        descLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(descLabel, gbc);

        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(descriptionArea);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scroll.setPreferredSize(new Dimension(0, 100));
        formPanel.add(scroll, gbc);

        // Selección de días
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        JLabel daysLabel = new JLabel("Días de la semana:");
        daysLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(daysLabel, gbc);

        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel daysPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        daysPanel.setBackground(Color.WHITE);
        daysPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        String[] dayNames = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        DayOfWeek[] days = DayOfWeek.values();
        dayCheckboxes = new JCheckBox[days.length];
        for (int i = 0; i < days.length; i++) {
            dayCheckboxes[i] = new JCheckBox(dayNames[i]);
            dayCheckboxes[i].setBackground(Color.WHITE);
            dayCheckboxes[i].setFont(new Font("Arial", Font.PLAIN, 11));
            daysPanel.add(dayCheckboxes[i]);
        }
        formPanel.add(daysPanel, gbc);

        // Hora de inicio
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        JLabel startLabel = new JLabel("Hora de inicio:");
        startLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(startLabel, gbc);

        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        startTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startTimeSpinner, "HH:mm");
        startTimeSpinner.setEditor(startEditor);
        startTimeSpinner.setPreferredSize(new Dimension(100, 30));
        formPanel.add(startTimeSpinner, gbc);

        // Hora de fin
        gbc.gridx = 1; gbc.gridy = row - 1; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        JLabel endLabel = new JLabel("Hora de fin:");
        endLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(endLabel, gbc);

        gbc.gridx = 1; gbc.gridy = row; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        endTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm");
        endTimeSpinner.setEditor(endEditor);
        endTimeSpinner.setPreferredSize(new Dimension(100, 30));
        formPanel.add(endTimeSpinner, gbc);

        // Selector de color
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        JLabel colorLabel = new JLabel("Color de la tarea:");
        colorLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(colorLabel, gbc);

        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel colorPanel = createColorSelector();
        formPanel.add(colorPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setBackground(new Color(240, 240, 240));
        cancelButton.addActionListener(e -> dispose());

        JButton createButton = new JButton("Crear Tarea");
        createButton.setPreferredSize(new Dimension(120, 35));
        createButton.setBackground(new Color(70, 130, 180));
        createButton.setForeground(Color.WHITE);
        createButton.setFont(new Font("Arial", Font.BOLD, 12));
        createButton.addActionListener((ActionEvent e) -> createTask());

        buttonPanel.add(cancelButton);
        buttonPanel.add(createButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createColorSelector() {
        JPanel colorPanel = new JPanel(new BorderLayout(10, 10));
        colorPanel.setBackground(Color.WHITE);
        colorPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Panel con colores predefinidos
        JPanel presetPanel = new JPanel(new GridLayout(2, 6, 5, 5));
        presetPanel.setBackground(Color.WHITE);

        ButtonGroup colorGroup = new ButtonGroup();

        for (int i = 0; i < PRESET_COLORS.length; i++) {
            JToggleButton colorButton = new JToggleButton();
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.setBackground(PRESET_COLORS[i]);
            colorButton.setOpaque(true);
            colorButton.setBorder(BorderFactory.createRaisedBevelBorder());

            final int index = i;
            colorButton.addActionListener(e -> {
                selectedColor = PRESET_COLORS[index];
                updateColorPreview();
            });

            colorGroup.add(colorButton);
            presetPanel.add(colorButton);

            // Seleccionar el primer color por defecto
            if (i == 0) {
                colorButton.setSelected(true);
            }
        }

        // Panel inferior con preview y selector personalizado
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(Color.WHITE);

        bottomPanel.add(new JLabel("Vista previa: "));

        colorPreview = new JPanel();
        colorPreview.setPreferredSize(new Dimension(40, 25));
        colorPreview.setBackground(selectedColor);
        colorPreview.setBorder(BorderFactory.createLoweredBevelBorder());
        bottomPanel.add(colorPreview);

        JButton customColorButton = new JButton("Personalizar...");
        customColorButton.setFont(new Font("Arial", Font.PLAIN, 11));
        customColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Seleccionar Color", selectedColor);
            if (newColor != null) {
                selectedColor = newColor;
                updateColorPreview();
                // Deseleccionar botones predefinidos
                for (AbstractButton button : java.util.Collections.list(colorGroup.getElements())) {
                    button.setSelected(false);
                }
            }
        });
        bottomPanel.add(customColorButton);

        colorPanel.add(new JLabel("Colores predefinidos:"), BorderLayout.NORTH);
        colorPanel.add(presetPanel, BorderLayout.CENTER);
        colorPanel.add(bottomPanel, BorderLayout.SOUTH);

        return colorPanel;
    }

    private void updateColorPreview() {
        colorPreview.setBackground(selectedColor);
        colorPreview.repaint();
    }

    private void createTask() {
        List<Task> newTasks = createTasksFromForm();
        if (newTasks != null && !newTasks.isEmpty()) {
            try {
                List<Task> tasks = TaskStorage.loadTasks();

                // Verificar conflictos de horario
                boolean hasConflict = false;
                for (Task newTask : newTasks) {
                    if (calendar != null && calendar.hasTimeConflict(newTask)) {
                        hasConflict = true;
                        break;
                    }
                }

                if (hasConflict) {
                    int option = JOptionPane.showConfirmDialog(this,
                            "Una o más tareas tienen conflictos de horario.\n¿Deseas crearlas de todas formas?",
                            "Conflicto de Horario",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (option != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                tasks.addAll(newTasks);
                TaskStorage.saveTasks(tasks);

                // Actualizar el calendario
                if (calendar != null) {
                    calendar.refreshTasks();
                }

                String message = String.format("✓ %d tarea(s) creada(s) exitosamente", newTasks.size());
                JOptionPane.showMessageDialog(this,
                        message,
                        "Tareas Creadas",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar las tareas: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private List<Task> createTasksFromForm() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Obtener horas seleccionadas
        Date startDate = (Date) startTimeSpinner.getValue();
        Date endDate = (Date) endTimeSpinner.getValue();

        LocalTime startTime = LocalTime.of(startDate.getHours(), startDate.getMinutes());
        LocalTime endTime = LocalTime.of(endDate.getHours(), endDate.getMinutes());

        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            JOptionPane.showMessageDialog(this, "La hora de fin debe ser posterior a la de inicio", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        List<Task> newTasks = new ArrayList<>();
        DayOfWeek[] days = DayOfWeek.values();

        for (int i = 0; i < dayCheckboxes.length; i++) {
            if (dayCheckboxes[i].isSelected()) {
                // Crear tarea con color personalizado
                Task task = new Task(title, description, days[i], false, startTime, endTime);
                task.setColor(selectedColor); // Establecer el color seleccionado
                newTasks.add(task);
            }
        }

        if (newTasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar al menos un día", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return newTasks;
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddTaskWindow extends JFrame {

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField endDateField;

    public AddTaskWindow() {
        setTitle("Add Task");
        setSize(400, 300);
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

        // Fecha de fin
        formPanel.add(new JLabel("Fecha de fin (dd/MM/yyyy):"));
        endDateField = new JTextField();
        formPanel.add(endDateField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Botón crear
        JButton createButton = new JButton("Crear Tarea");
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Task newTask = createTaskFromForm();
                if (newTask != null) {
                    JOptionPane.showMessageDialog(AddTaskWindow.this,
                            "Tarea creada:\n" + newTask,
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
            }
        });

        mainPanel.add(createButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private Task createTaskFromForm() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String dateStr = endDateField.getText().trim();

        if (title.isEmpty() || description.isEmpty() || dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        try {
            Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
            return new Task(title, description, endDate);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. Usa formato dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AddTaskWindow().setVisible(true);
            }
        });
    }
}

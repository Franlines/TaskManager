import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class Calendar extends JFrame {

    public Calendar() {
        setTitle("Calendar App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        // Fecha actual
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Etiqueta con mes y año
        JLabel monthLabel = new JLabel(today.getMonth().toString() + " " + year, JLabel.CENTER);
        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(monthLabel, BorderLayout.NORTH);

        // Panel de calendario (7 columnas: Lunes-Domingo)
        JPanel calendarPanel = new JPanel(new GridLayout(0, 7, 5, 5));

        // Encabezados de días
        String[] days = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
        for (String day : days) {
            JLabel dayLabel = new JLabel(day, JLabel.CENTER);
            dayLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            calendarPanel.add(dayLabel);
        }

        // Calcular en qué día de la semana empieza el mes
        LocalDate firstDay = LocalDate.of(year, month, 1);
        int firstDayOfWeek = firstDay.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday
        int daysInMonth = firstDay.lengthOfMonth();

        // Rellenar huecos antes del primer día
        for (int i = 1; i < firstDayOfWeek; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // Añadir botones para cada día
        for (int dayNum = 1; dayNum <= daysInMonth; dayNum++) {
            JButton dayButton = new JButton(String.valueOf(dayNum));
            calendarPanel.add(dayButton);
        }

        // Añadir calendario al panel
        mainPanel.add(calendarPanel, BorderLayout.CENTER);

        // Botón Add Task
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddTaskWindow addTaskWindow = new AddTaskWindow();
                addTaskWindow.setVisible(true);
            }
        });
        mainPanel.add(addTaskButton, BorderLayout.SOUTH);

        add(mainPanel);
    }
}

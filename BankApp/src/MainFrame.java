import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MainFrame extends JFrame {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JButton wplataButton;
    private JButton wyplataButton;
    private JButton przelewButton;
    private JButton wyjdzButton;
    private JTable kartyTable;
    private JTable historiaTable;
    private JLabel nameField;
    private JLabel idValueLabel;
    private JLabel saldoValueLabel;

    // URL for connection with database
    private static final String DB_URL = "jdbc:mysql://localhost:3306/BankApp";

    public MainFrame(String client_number) {
        super("MainFrame");
        this.setContentPane(this.panel1); // wyswietlanie okienka na ekranie
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(700, 600);

        String client_nr = client_number;

        try {
            Connection connection = (Connection) DriverManager.getConnection(DB_URL,
                    "root", "rootroot");

            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("SELECT * FROM BankApp.Client WHERE client_nr=?");

            st.setString(1, client_nr);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                // Jeśli znajdziemy usera w bazie z podanym numerem klienta:

                String clientName = rs.getString("client_fname");

                nameField.setText("Witaj, " + clientName);

            } else {
                // Error 123: Data in database are not comipled!
                JOptionPane.showMessageDialog(MainFrame.this, "Error 123!");
            }
        } catch (SQLException sqlException) {
            // Error 12: Database is off or Your connection is invalid!
            JOptionPane.showMessageDialog(MainFrame.this, "Error 12!");
        }


//        String[] columnNames = {"Nr Karty",
//                "Rodzaj karty"};
//
//        Object[][] data = {
//                {"14151515", "Kredytowa"},
//                {"23523525", "Debetowa"}
//        };

//        kartyTable = new JTable(data, columnNames);

        wyjdzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
            }
        });
        wplataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                WplataFrame wplata = new WplataFrame();
                wplata.setVisible(true);
            }
        });
        wyplataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                WyplataFrame wyplata = new WyplataFrame();
                wyplata.setVisible(true);
            }
        });
    }


}

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    private JLabel saldoField;
    private JButton wylogujButton;
    private JButton dodajButton;
    private JButton usunButton;
    private JPanel tabsPanel;
    private JPanel buttonsPanel;
    private JPanel kartyPanel;
    private JPanel historiaPanel;
    private JScrollPane kartyScroll;
    private JLabel kartyLabel;
    private JLabel historiaLabel;
    private JScrollPane historiaScroll;
    private JPanel logoutButtons;

    // URL for connection with database
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bankapp";

    public MainFrame(String client_number) {
        super("MainFrame");
        StylesFunction();
        this.setContentPane(this.panel1); // wyswietlanie okienka na ekranie
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setSize(700,600);
        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,5,10,5);
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        panel1.add(nameField,c);
        c.gridx = 1;
        panel1.add(saldoField,c);
        c.gridx = 2;
        panel1.add(wylogujButton,c);
        c.gridx = 3;
        panel1.add(wyjdzButton,c);
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 4;
        c.gridx = 0;
        panel1.add(tabsPanel,c);
        c.gridy = 2;
        c.gridx = 0;
        panel1.add(buttonsPanel,c);

        /** center window **/
        this.pack();


//
//        panel1.setMinimumSize(new Dimension(1700,500));
//        panel1.setPreferredSize(new Dimension(1700,500));
        this.setLocationRelativeTo(null);
        DatabaseQueries(client_number);

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
                WplataFrame wplata = new WplataFrame(client_number);
                wplata.setVisible(true);
            }
        });
        wyplataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                WyplataFrame wyplata = new WyplataFrame(client_number);
                wyplata.setVisible(true);
            }
        });
        przelewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                PrzelewyFrame przelewy = new PrzelewyFrame(client_number);
                przelewy.setVisible(true);
            }
        });
        dodajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                AddCard addCard = new AddCard(client_number);
                addCard.setVisible(true);
            }
        });
        usunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                RemoveCard removeCard = new RemoveCard(client_number);
                removeCard.setVisible(true);
            }
        });
    }

    private void DatabaseQueries(String client_nr)
    {
        try {
            //connection with database
            Connection connection = (Connection) DriverManager.getConnection(DB_URL,
                    "root", "rootroot");
/**
 * Welcome Query
 */
            /** Query which is getting first name out client **/
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("SELECT client_fname FROM bankapp.client WHERE client_nr=?");

            st.setString(1, client_nr); // replacment first '?' in query by clinet number
            ResultSet rs = st.executeQuery();        // execute query

            // chech if some rows are existing in database
            // if yes then download first name from db and output in nameField
            //if no then ouput error
            if (rs.next()) {
                String clientName = rs.getString("client_fname");
                nameField.setText("Witaj, " + clientName);
            } else {
                // Error 123: Data in database are not comipled!
                JOptionPane.showMessageDialog(MainFrame.this, "Error 123!");
            }// END QUERY

            /** Query which gets client's all balance **/
            PreparedStatement sumBalance = (PreparedStatement) connection
                    .prepareStatement("SELECT ROUND(SUM(card_balance), 2) AS balance FROM card WHERE client_nr=?");

            sumBalance.setString(1, client_nr);
            ResultSet sumBalanceResult = sumBalance.executeQuery();

            if (sumBalanceResult.next()) {
                saldoField.setText("Saldo: " + sumBalanceResult.getString("balance"));

            } else {
                // Error 123: Data in database dose not exist!
                JOptionPane.showMessageDialog(MainFrame.this, "Error 123!");
            }// END QUERY

/**
 *  Cards Queries
 */

            /** Query which return count of client's cards **/
            PreparedStatement countRows = (PreparedStatement) connection
                    .prepareStatement("SELECT count(card_nr) as countRows FROM bankapp.card WHERE client_nr =?");

            countRows.setString(1, client_nr);
            ResultSet countRowsResult = countRows.executeQuery();
            int rows = 0;

            // download how many rows/cards have our client
            if(countRowsResult.next()) rows = countRowsResult.getInt(1);
            // END QUERY

            /** Query which gets balance for our client's each card **/
            PreparedStatement allBalances = (PreparedStatement) connection
                    .prepareStatement("SELECT card_nr, card_term_data, card_type, card_balance FROM bankapp.card WHERE client_nr =?;");

            allBalances.setString(1, client_nr);
            ResultSet sumAllBalances = allBalances.executeQuery();

            //declaration 2d object of data which we are gonna use later
            Object[][] data = new Object[rows][4];

            // mechanism to write all data from database to out variable "data", dirst rows then columns
            for(int r=0;r<rows;r++) {
                while(true){
                    int i = 0;
                    if (sumAllBalances.next()) {
                        String card_nr = sumAllBalances.getString("card_nr");
                        String card_term_data = sumAllBalances.getString("card_term_data");
                        String card_type = sumAllBalances.getString("card_type");
                        String card_balance = sumAllBalances.getString("card_balance");

                        data[r][i] = card_nr;
                        data[r][i+1] = card_term_data;
                        data[r][i+2] = card_type;
                        data[r][i+3] = card_balance;
                        break;
                    }
                }
            }

            // add headers to table and then display it (table)
            String[] headers = {"Numer Karty", "Data Ważności", "Typ Karty", "Saldo Karty"};
            kartyTable.setModel(new DefaultTableModel(data, headers));
            // END QUERY

/**
 *  Overflows Queries
 */

            /** Query which return count of client's overflows **/
            PreparedStatement countOverflowRows = (PreparedStatement) connection
                    .prepareStatement(
                            "SELECT COUNT(overflow_id)" +
                                    "FROM bankapp.overflow " +
                                    "LEFT JOIN Card ON overflow.overflow_send_number = card.card_nr OR overflow.overflow_recipent_number = card.card_nr " +
                                    "WHERE client_nr =?;");

            countOverflowRows.setString(1, client_nr);
            ResultSet countAllOverflowRows = countOverflowRows.executeQuery();
            int overflowRows = 0;

            // download how many overflows have our client
            if(countAllOverflowRows.next()) overflowRows = countAllOverflowRows.getInt(1);

            /** Query which gets balance for our client's each card **/
            PreparedStatement allOverflows = (PreparedStatement) connection
                    .prepareStatement(
                            "SELECT overflow_send_number, overflow_recipent_number, overflow_data, overflow_amount FROM bankapp.overflow  " +
                            "LEFT JOIN card ON overflow.overflow_send_number = card.card_nr OR overflow.overflow_recipent_number = card.card_nr  " +
                            "WHERE client_nr =?");

            allOverflows.setString(1, client_nr);
            ResultSet sumAllOverflows = allOverflows.executeQuery();

            //declaration 2d object of data which we are gonna use later
            Object[][] oferflowData = new Object[overflowRows][4];

            // mechanism to write all data from database to out variable "data", dirst rows then columns
            for(int r=0;r<overflowRows;r++) {
                while(true){
                    int i = 0;
                    if (sumAllOverflows.next()) {
                        String overflow_send_number = sumAllOverflows.getString("overflow_send_number");
                        String overflow_recipent_number = sumAllOverflows.getString("overflow_recipent_number");
                        String overflow_data = sumAllOverflows.getString("overflow_data");
                        String overflow_amount = sumAllOverflows.getString("overflow_amount");

                        oferflowData[r][i] = overflow_send_number;
                        oferflowData[r][i+1] = overflow_recipent_number;
                        oferflowData[r][i+2] = overflow_data;
                        oferflowData[r][i+3] = overflow_amount;
                        break;
                    }
                }
            }

            // add headers to table and then display it (table)
            String[] oferflowHeaders = {"Nr Wysyłającego", "Nr Odbiorcy", "Data Przelewu", "Wartość przelewu"};
            historiaTable.setModel(new DefaultTableModel(oferflowData, oferflowHeaders));
            // END QUERY

        } catch (SQLException sqlException) {
            // Error 12: Database is off or Your connection is invalid!
            JOptionPane.showMessageDialog(MainFrame.this, "Error 12!");
        }
    }

    private void StylesFunction() {
        Color main_green = new Color(108, 220, 96);
        Color foreground_white = new Color(222, 222, 222);
        Color dark_gray = new Color(42, 42, 42);
        Color light_gray = new Color(63, 63, 63);
        Color button_gray = new Color(152, 152, 152);
        Color button_green = new Color(129, 161, 125);

        /** buttons style **/
        wplataButton.setBackground(main_green);
        wyplataButton.setBackground(main_green);
        przelewButton.setBackground(main_green);
        wyjdzButton.setBackground(button_gray);
        wylogujButton.setBackground(button_gray);
        dodajButton.setBackground(button_green);
        usunButton.setBackground(button_green);

        /** background style **/
        panel1.setBackground(dark_gray);
        tabsPanel.setBackground(dark_gray);
        buttonsPanel.setBackground(dark_gray);
        tabbedPane1.setBackground(light_gray);
        kartyPanel.setBackground(light_gray);
        historiaPanel.setBackground(light_gray);
        kartyScroll.getViewport().setBackground(light_gray);
        kartyTable.setBackground(light_gray);
        kartyScroll.setBackground(light_gray);
        kartyTable.getTableHeader().setBackground(light_gray);
        historiaScroll.getViewport().setBackground(light_gray);
        historiaTable.setBackground(light_gray);
        historiaScroll.setBackground(light_gray);
        historiaTable.getTableHeader().setBackground(light_gray);
        
        /** selection **/
        kartyTable.setSelectionBackground(main_green);
        kartyTable.setSelectionForeground(light_gray);
        historiaTable.setSelectionBackground(main_green);
        historiaTable.setSelectionForeground(light_gray);
        /** labels **/
        nameField.setForeground(main_green);
        saldoField.setForeground(foreground_white);
        tabbedPane1.setForeground(main_green);
        kartyTable.setForeground(foreground_white);
        kartyPanel.setForeground(foreground_white);
        kartyScroll.setForeground(foreground_white);
        kartyLabel.setForeground(foreground_white);
        historiaLabel.setForeground(foreground_white);
        kartyTable.getTableHeader().setForeground(foreground_white);

        historiaTable.setForeground(foreground_white);
        historiaScroll.setForeground(foreground_white);
        historiaTable.getTableHeader().setForeground(foreground_white);
        historiaPanel.setForeground(foreground_white);
        /** margin **/
        panel1.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

    }
}

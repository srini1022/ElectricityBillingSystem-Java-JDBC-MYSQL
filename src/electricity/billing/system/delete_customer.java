 package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class delete_customer extends JFrame implements ActionListener {

    JTextField tfMeter;
    JLabel lblName, lblAddress, lblCity, lblState, lblEmail, lblPhone;
    JButton searchBtn, deleteBtn, cancelBtn;

    // Constructor
    delete_customer() {
        super("Delete Customer");

        getContentPane().setBackground(new Color(240, 230, 255));
        setSize(700, 500);
        setLocation(400, 200);
        setLayout(null);

        JLabel heading = new JLabel("Delete Customer Record");
        heading.setFont(new Font("Tahoma", Font.BOLD, 20));
        heading.setBounds(200, 20, 350, 30);
        add(heading);

        JLabel meterLabel = new JLabel("Enter Meter No:");
        meterLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        meterLabel.setBounds(100, 80, 150, 25);
        add(meterLabel);

        tfMeter = new JTextField();
        tfMeter.setBounds(250, 80, 200, 25);
        add(tfMeter);

        searchBtn = new JButton("Search");
        searchBtn.setBackground(Color.BLACK);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setBounds(480, 80, 100, 25);
        searchBtn.addActionListener(this);
        add(searchBtn);

        // Labels for displaying customer info
        JLabel name = new JLabel("Name:");
        name.setFont(new Font("Tahoma", Font.PLAIN, 14));
        name.setBounds(100, 140, 150, 25);
        add(name);
        lblName = new JLabel();
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblName.setBounds(250, 140, 300, 25);
        add(lblName);

        JLabel address = new JLabel("Address:");
        address.setFont(new Font("Tahoma", Font.PLAIN, 14));
        address.setBounds(100, 180, 150, 25);
        add(address);
        lblAddress = new JLabel();
        lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblAddress.setBounds(250, 180, 300, 25);
        add(lblAddress);

        JLabel city = new JLabel("City:");
        city.setFont(new Font("Tahoma", Font.PLAIN, 14));
        city.setBounds(100, 220, 150, 25);
        add(city);
        lblCity = new JLabel();
        lblCity.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblCity.setBounds(250, 220, 300, 25);
        add(lblCity);

        JLabel state = new JLabel("State:");
        state.setFont(new Font("Tahoma", Font.PLAIN, 14));
        state.setBounds(100, 260, 150, 25);
        add(state);
        lblState = new JLabel();
        lblState.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblState.setBounds(250, 260, 300, 25);
        add(lblState);

        JLabel email = new JLabel("Email:");
        email.setFont(new Font("Tahoma", Font.PLAIN, 14));
        email.setBounds(100, 300, 150, 25);
        add(email);
        lblEmail = new JLabel();
        lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblEmail.setBounds(250, 300, 300, 25);
        add(lblEmail);

        JLabel phone = new JLabel("Phone:");
        phone.setFont(new Font("Tahoma", Font.PLAIN, 14));
        phone.setBounds(100, 340, 150, 25);
        add(phone);
        lblPhone = new JLabel();
        lblPhone.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblPhone.setBounds(250, 340, 300, 25);
        add(lblPhone);

        // Buttons
        deleteBtn = new JButton("Delete Customer");
        deleteBtn.setBackground(Color.RED);
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBounds(180, 400, 150, 30);
        deleteBtn.addActionListener(this);
        add(deleteBtn);

        cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(Color.GRAY);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBounds(360, 400, 150, 30);
        cancelBtn.addActionListener(this);
        add(cancelBtn);

        setVisible(true);
    }

    // Event Handling
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchBtn) {
            String meter = tfMeter.getText().trim();

            if (meter.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter a meter number");
                return;
            }

            try {
                database c = new database();
                ResultSet rs = c.statement.executeQuery(
                        "SELECT * FROM new_customer WHERE meter_no = '" + meter + "'");

                if (rs.next()) {
                    lblName.setText(rs.getString("name"));
                    lblAddress.setText(rs.getString("address"));
                    lblCity.setText(rs.getString("city"));
                    lblState.setText(rs.getString("state"));
                    lblEmail.setText(rs.getString("email"));
                    lblPhone.setText(rs.getString("phone_no"));
                } else {
                    JOptionPane.showMessageDialog(null, "No customer found with Meter No: " + meter);
                    clearLabels();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error fetching customer details: " + ex.getMessage());
            }

        } else if (e.getSource() == deleteBtn) {
            String meter = tfMeter.getText().trim();

            if (meter.equals("")) {
                JOptionPane.showMessageDialog(null, "Please search a meter number first!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to delete Customer with Meter No: " + meter + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    database c = new database();

                    // Delete from all linked tables
                    c.statement.executeUpdate("DELETE FROM bill WHERE meter_no = '" + meter + "'");
                    c.statement.executeUpdate("DELETE FROM meter_info WHERE meter_number = '" + meter + "'");
                    c.statement.executeUpdate("DELETE FROM Signup WHERE meter_no = '" + meter + "'");
                    c.statement.executeUpdate("DELETE FROM new_customer WHERE meter_no = '" + meter + "'");

                    JOptionPane.showMessageDialog(null, "Customer and related records deleted successfully!");
                    clearLabels();
                    tfMeter.setText("");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error deleting record: " + ex.getMessage());
                }
            }

        } else if (e.getSource() == cancelBtn) {
            setVisible(false);
        }
    }

    private void clearLabels() {
        lblName.setText("");
        lblAddress.setText("");
        lblCity.setText("");
        lblState.setText("");
        lblEmail.setText("");
        lblPhone.setText("");
    }

    public static void main(String[] args) {
        new delete_customer();
    }
}

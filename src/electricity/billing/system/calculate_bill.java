package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;

public class calculate_bill extends JFrame implements ActionListener {
    JLabel nameText, addressText;
    TextField unitText;
    Choice meternumCho, monthCho;
    JButton submit, cancel;

    calculate_bill() {

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(214, 195, 247));
        add(panel);

        JLabel heading = new JLabel("Calculate Electricity Bill");
        heading.setBounds(70, 10, 300, 20);
        heading.setFont(new Font("Tahoma", Font.BOLD, 20));
        panel.add(heading);

        JLabel meternum = new JLabel("Meter Number");
        meternum.setBounds(50, 80, 100, 20);
        panel.add(meternum);

        meternumCho = new Choice();
        try {
            database c = new database();
            java.sql.PreparedStatement ps = c.prepareStatement("SELECT meter_number FROM meters");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                meternumCho.add(resultSet.getString("meter_number"));
            }
            c.closeStatement(ps);
            c.closeConnection();
        } catch (Exception E) {
            E.printStackTrace();
        }
        meternumCho.setBounds(180, 80, 100, 20);
        panel.add(meternumCho);

        JLabel name = new JLabel("Name");
        name.setBounds(50, 120, 100, 20);
        panel.add(name);

        nameText = new JLabel("");
        nameText.setBounds(180, 120, 150, 20);
        panel.add(nameText);

        JLabel address = new JLabel("Address");
        address.setBounds(50, 160, 100, 20);
        panel.add(address);

        addressText = new JLabel("");
        addressText.setBounds(180, 160, 150, 20);
        panel.add(addressText);

        try {
            database c = new database();
            java.sql.PreparedStatement ps = c.prepareStatement(
                    "SELECT c.name, c.address FROM customers c JOIN meters m ON c.id = m.customer_id WHERE m.meter_number = ?");
            ps.setString(1, meternumCho.getSelectedItem());
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                nameText.setText(resultSet.getString("name"));
                addressText.setText(resultSet.getString("address"));
            }
            c.closeStatement(ps);
            c.closeConnection();
        } catch (Exception E) {
            E.printStackTrace();
        }
        meternumCho.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                try {
                    database c = new database();
                    java.sql.PreparedStatement ps = c.prepareStatement(
                            "SELECT c.name, c.address FROM customers c JOIN meters m ON c.id = m.customer_id WHERE m.meter_number = ?");
                    ps.setString(1, meternumCho.getSelectedItem());
                    ResultSet resultSet = ps.executeQuery();
                    while (resultSet.next()) {
                        nameText.setText(resultSet.getString("name"));
                        addressText.setText(resultSet.getString("address"));
                    }
                    c.closeStatement(ps);
                    c.closeConnection();
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });

        JLabel unitconsumed = new JLabel("Unit Consumed");
        unitconsumed.setBounds(50, 200, 100, 20);
        panel.add(unitconsumed);

        unitText = new TextField();
        unitText.setBounds(180, 200, 150, 20);
        panel.add(unitText);

        JLabel month = new JLabel("Month");
        month.setBounds(50, 240, 100, 20);
        panel.add(month);

        monthCho = new Choice();
        monthCho.add("January");
        monthCho.add("February");
        monthCho.add("March");
        monthCho.add("April");
        monthCho.add("May");
        monthCho.add("June");
        monthCho.add("July");
        monthCho.add("August");
        monthCho.add("September");
        monthCho.add("October");
        monthCho.add("November");
        monthCho.add("December");
        monthCho.setBounds(180, 240, 150, 20);
        panel.add(monthCho);

        submit = new JButton("Submit");
        submit.setBounds(80, 300, 100, 25);
        submit.setBackground(Color.black);
        submit.setForeground(Color.white);
        submit.addActionListener(this);
        panel.add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(220, 300, 100, 25);
        cancel.setBackground(Color.black);
        cancel.setForeground(Color.white);
        cancel.addActionListener(this);
        panel.add(cancel);

        setLayout(new BorderLayout());
        add(panel, "Center");
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/budget.png"));
        Image image = imageIcon.getImage().getScaledInstance(250, 200, Image.SCALE_DEFAULT);
        ImageIcon imageIcon1 = new ImageIcon(image);
        JLabel imageLabel = new JLabel(imageIcon1);
        add(imageLabel, "East");

        setSize(650, 400);
        setLocation(400, 200);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            String smeterNo = meternumCho.getSelectedItem();
            String sunit = unitText.getText();
            String smonth = monthCho.getSelectedItem();

            double totalBill = 0;
            int units = Integer.parseInt(sunit);
            try {
                database c = new database();

                java.sql.PreparedStatement psTax = c
                        .prepareStatement("SELECT * FROM taxes ORDER BY effective_from DESC LIMIT 1");
                ResultSet rsTax = psTax.executeQuery();
                if (rsTax.next()) {
                    totalBill += units * rsTax.getDouble("cost_per_unit");
                    totalBill += rsTax.getDouble("meter_rent");
                    totalBill += rsTax.getDouble("service_charge");
                    totalBill += rsTax.getDouble("swachh_bharat");
                    totalBill += rsTax.getDouble("fixed_tax");
                }
                c.closeStatement(psTax);

                // Find meter id for selected meter_number
                java.sql.PreparedStatement psMeter = c.prepareStatement("SELECT id FROM meters WHERE meter_number = ?");
                psMeter.setString(1, smeterNo);
                ResultSet rsMeter = psMeter.executeQuery();
                int meterId = -1;
                if (rsMeter.next())
                    meterId = rsMeter.getInt("id");

                // If meter_id not found, we can't proceed
                if (meterId < 0) {
                    JOptionPane.showMessageDialog(null, "Meter record not found. Please add meter info first.");
                    c.closeStatement(psMeter);
                    c.closeConnection();
                    return;
                }

                // Ensure we don't create duplicate bills for the same month/year
                java.sql.PreparedStatement psCheck = c.prepareStatement(
                        "SELECT id FROM bills WHERE meter_id = ? AND month = ? AND year = YEAR(CURDATE()) LIMIT 1");
                psCheck.setInt(1, meterId);
                psCheck.setString(2, smonth);
                ResultSet rsCheck = psCheck.executeQuery();
                if (rsCheck.next()) {
                    JOptionPane.showMessageDialog(null,
                            "A bill already exists for this meter and month. Please use the payment screen to pay the existing bill.");
                    c.closeStatement(psCheck);
                    c.closeStatement(psMeter);
                    c.closeConnection();
                    return;
                }
                c.closeStatement(psCheck);

                // Insert into bills (meter_id, month, year, units, total_amount, status)
                java.sql.PreparedStatement psInsert = c.prepareStatement(
                        "INSERT INTO bills (meter_id, month, year, units, total_amount, status) VALUES (?, ?, YEAR(CURDATE()), ?, ?, 'Not Paid')");
                psInsert.setInt(1, meterId);
                psInsert.setString(2, smonth);
                psInsert.setInt(3, units);
                psInsert.setDouble(4, totalBill);
                psInsert.executeUpdate();

                c.closeStatement(psMeter);
                c.closeStatement(psInsert);
                c.closeConnection();

                JOptionPane.showMessageDialog(null, "Customer Bill Updated Successfully");
                setVisible(false);
            } catch (Exception E) {
                E.printStackTrace();
            }

        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new calculate_bill();
    }

}

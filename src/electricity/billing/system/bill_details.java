package electricity.billing.system;

import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class bill_details extends JFrame {
    String meter;
    bill_details(String meter){
        this.meter =meter;
        setSize(700,650);
        setLocation(400,150);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JTable table = new JTable();

        try{
            database c = new database();
            java.sql.PreparedStatement psMeter = c.prepareStatement("SELECT id FROM meters WHERE meter_number = ?");
            psMeter.setString(1, meter);
            ResultSet rsMeter = psMeter.executeQuery();
            int meterId = -1;
            if (rsMeter.next()) meterId = rsMeter.getInt("id");

            java.sql.PreparedStatement ps = c.prepareStatement("SELECT * FROM bills WHERE meter_id = ? ORDER BY created_at DESC");
            ps.setInt(1, meterId);
            ResultSet resultSet = ps.executeQuery();
            table.setModel(DbUtils.resultSetToTableModel(resultSet));

            c.closeStatement(psMeter);
            c.closeStatement(ps);
            c.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(0,0,700,650);
        add(sp);

        setVisible(true);
    }
    public static void main(String[] args) {
        new bill_details("");
    }
}

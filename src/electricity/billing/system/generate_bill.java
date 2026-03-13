package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class generate_bill extends JFrame implements ActionListener {
    Choice searchmonthcho;
    String meter;
    JTextArea area;
    JButton bill;
    generate_bill(String meter){
        this.meter=meter;
        setSize(500,700);
        setLocation(500,30);
        setLayout( new BorderLayout());
        JPanel panel = new JPanel();

        JLabel heading = new JLabel("Generate Bill");

        JLabel meter_no = new JLabel(meter);

        searchmonthcho = new Choice();
        searchmonthcho.add("January");
        searchmonthcho.add("February");
        searchmonthcho.add("March");
        searchmonthcho.add("April");
        searchmonthcho.add("May");
        searchmonthcho.add("June");
        searchmonthcho.add("July");
        searchmonthcho.add("August");
        searchmonthcho.add("September");
        searchmonthcho.add("October");
        searchmonthcho.add("November");
        searchmonthcho.add("December");

        area= new JTextArea(50,15);
        area.setText("\n \n \t ------------------- Click on the ---------------\n \t ------------------- Generate Bill");
        area.setFont(new Font("Senserif",Font.ITALIC,15));
        JScrollPane pane = new JScrollPane(area);
        bill = new JButton("Generate Bill");
        bill.addActionListener(this);

        add(pane);

        panel.add(heading);
        panel.add(meter_no);
        panel.add(searchmonthcho);
        add(panel,"North");
        add(bill,"South");

        setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try{
            database c = new database();
            String smonth =  searchmonthcho.getSelectedItem();
            area.setText("\n Power Limited \n Electricity Bill For Month of "+smonth+",2023\n\n\n");
            java.sql.PreparedStatement psCust = c.prepareStatement("SELECT c.* FROM customers c WHERE c.meter_no = ?");
            psCust.setString(1, meter);
            ResultSet resultSet = psCust.executeQuery();
            if (resultSet.next()){
                area.append("\n    Customer Name        : "+resultSet.getString("name"));
                area.append("\n    Customer Meter Number: "+resultSet.getString("meter_no"));
                area.append("\n    Customer Address     : "+resultSet.getString("address"));
                area.append("\n    Customer City        : "+resultSet.getString("city"));
                area.append("\n    Customer State       : "+resultSet.getString("state"));
                area.append("\n    Customer Email       : "+resultSet.getString("email"));
                area.append("\n    Customer Phone Number       : "+resultSet.getString("phone_no"));

            }

            java.sql.PreparedStatement psMeter = c.prepareStatement("SELECT * FROM meters WHERE meter_number = ?");
            psMeter.setString(1, meter);
            resultSet = psMeter.executeQuery();
            if (resultSet.next()){
                area.append("\n    Customer Meter Location        : "+resultSet.getString("meter_location"));
                area.append("\n    Customer Meter Type: "+resultSet.getString("meter_type"));
                area.append("\n    Customer Phase Code   : "+resultSet.getString("phase_code"));
                area.append("\n    Customer Bill Type        : "+resultSet.getString("bill_type"));
                area.append("\n    Customer Days      : "+resultSet.getString("days"));

            }

            java.sql.PreparedStatement psTax = c.prepareStatement("SELECT * FROM taxes ORDER BY effective_from DESC LIMIT 1");
            resultSet = psTax.executeQuery();
            if (resultSet.next()){
                area.append("\n    Cost Per Unit        : "+resultSet.getString("cost_per_unit"));
                area.append("\n   Meter Rent: "+resultSet.getString("meter_rent"));
                area.append("\n   Service Charge   : "+resultSet.getString("service_charge"));
                area.append("\n   Service Tax        : "+resultSet.getString("service_tax"));
                area.append("\n   Swacch Bharat      : "+resultSet.getString("swachh_bharat"));
                area.append("\n   Fixed Tax     : "+resultSet.getString("fixed_tax"));

            }

            java.sql.PreparedStatement psBill = c.prepareStatement("SELECT b.* FROM bills b JOIN meters m ON b.meter_id = m.id WHERE m.meter_number = ? AND b.month = ? LIMIT 1");
            psBill.setString(1, meter);
            psBill.setString(2, searchmonthcho.getSelectedItem());
            resultSet = psBill.executeQuery();
            if (resultSet.next()) {
                area.append("\n    Current Month       : " + resultSet.getString("month"));
                area.append("\n   Units Consumed: " + resultSet.getString("units"));
                area.append("\n   Total Charges   : " + resultSet.getString("total_amount"));
                area.append("\n Total Payable: "+resultSet.getString("total_amount"));
            }

            c.closeStatement(psCust);
            c.closeStatement(psMeter);
            c.closeStatement(psTax);
            c.closeStatement(psBill);
            c.closeConnection();


        }catch (Exception E ){
            E.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new generate_bill("");
    }
}

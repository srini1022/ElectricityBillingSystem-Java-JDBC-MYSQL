package electricity.billing.system;

import java.sql.*;

public class database {
    public Connection connection;
    public Statement statement;

    public database(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nebs","root","root");
            statement = connection.createStatement();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public void closeStatement(Statement st){
        try{ if (st!=null) st.close(); }catch (Exception ignored){}
    }

    public void closeConnection(){
        try{ if (connection!=null) connection.close(); }catch (Exception ignored){}
    }
}



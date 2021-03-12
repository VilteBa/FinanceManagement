package DB;

import Model.Category;
import Model.FinanceManagementSystem;
import utils.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SystemControl {

    public static FinanceManagementSystem findSystem(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM system WHERE systemId = ?");
        ps.setInt (1, id);
        ResultSet rs = ps.executeQuery();
        if(!rs.next())
            return null;

        String companyName = rs.getString(2);
        String version = rs.getString(3);
        Date date = rs.getDate(4);
        FinanceManagementSystem financeManagementSystem = new FinanceManagementSystem(id, companyName,version, date);
        connection.close();
        return financeManagementSystem;
    }

    public static List<FinanceManagementSystem> getSystems() throws ClassNotFoundException, SQLException {
        List<FinanceManagementSystem> systems = new ArrayList<>();
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM system");
        ResultSet systemsQuery = ps.executeQuery();

        while(systemsQuery.next()){
            int id = systemsQuery.getInt(1);
            String companyName = systemsQuery.getString(2);
            String version = systemsQuery.getString(3);
            Date date = systemsQuery.getDate(4);
            FinanceManagementSystem financeManagementSystem = new FinanceManagementSystem(id, companyName,version, date);
            systems.add(financeManagementSystem);
        }
        return systems;
    }
}

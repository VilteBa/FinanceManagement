package DB;

import Model.Income;
import utils.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncomeControl {

    public static String getTotalIncome() throws ClassNotFoundException, SQLException {
        String totalIncome = "";
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT SUM(amount) as total FROM income");
        ResultSet incomeQuery = ps.executeQuery();

        while(incomeQuery.next()){
            totalIncome = incomeQuery.getString("total");
        }
        return totalIncome;
    }

    public static List<Income> getIncomes(int categoryId) throws ClassNotFoundException, SQLException {
        List<Income> incomes = new ArrayList<>();
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM income WHERE categoryId = ?");
        ps.setInt (1, categoryId);
        ResultSet incomeQuery = ps.executeQuery();

        while(incomeQuery.next()){
            incomes.add(new Income(incomeQuery.getInt("incomeId"),
                    incomeQuery.getString("name"),
                    incomeQuery.getInt("amount"),
                    incomeQuery.getDate("date"),
                    incomeQuery.getInt("categoryId")));
        }
        return incomes;
    }

    public static  void create(Income income) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        Statement statement = connection.createStatement();
        String incomeInsert = "INSERT INTO income(name, amount, date, categoryId) " +
                "VALUES('"+income.getName()+
                "', '"+income.getAmount()+"', '"+ new Date(income.getDate().getTime()) +"', '"+income.getCategoryId()+"')";
        statement.executeUpdate(incomeInsert);
        connection.close();
    }

    public static boolean incomeExists(String name) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM income WHERE name = ?");
        ps.setString (1, name);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public static Income getIncome(String name) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM income WHERE name = ?");
        ps.setString (1, name);
        ResultSet rs = ps.executeQuery();
       if(!rs.next()){
           return null;
       }
        int id = rs.getInt("incomeId");
        int amount = rs.getInt("amount");
        Date date = rs.getDate("date");
        int categoryId = rs.getInt("categoryId");

        return new Income(id,
                name,
                amount,
                date,
                categoryId);
    }

    public static  void remove(String name) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement("delete from income where name=?");
        ps.setString(1, name);
        ps.execute();
        connection.close();
    }

}

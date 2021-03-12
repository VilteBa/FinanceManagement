package DB;

import Model.Expense;
import utils.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseControl {

    public static String getTotalExpense() throws ClassNotFoundException, SQLException {
        String totalExpense = "";
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT SUM(amount) as total FROM expense");
        ResultSet expenseQuery = ps.executeQuery();

        while(expenseQuery.next()){
            totalExpense = expenseQuery.getString("total");
        }
        return totalExpense;
    }

    public static Expense getExpense(String name) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM expense WHERE name = ?");
        ps.setString (1, name);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()){
            return null;
        }

        return new Expense(rs.getInt("expenseId"),
                rs.getString("name"),
                rs.getInt("amount"),
                rs.getDate("date"),
                rs.getInt("categoryId"));
    }

    public static List<Expense> getExpenses(int categoryId) throws ClassNotFoundException, SQLException {
        List<Expense> expenses = new ArrayList<>();
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM expense WHERE categoryId = ?");
        ps.setInt (1, categoryId);
        ResultSet expenseQuery = ps.executeQuery();

        while(expenseQuery.next()){
            expenses.add(new Expense(expenseQuery.getInt("expenseId"),
                    expenseQuery.getString("name"),
                    expenseQuery.getInt("amount"),
                    expenseQuery.getDate("date"),
                    expenseQuery.getInt("categoryId")));
        }
        return expenses;
    }

    public static  void create(Expense expense) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        Statement statement = connection.createStatement();
        String expenseInsert = "INSERT INTO expense(name, amount, date, categoryId) " +
                "VALUES('"+expense.getName()+
                "', '"+expense.getAmount()+"', '"+ new Date(expense.getDate().getTime()) +"', '"+expense.getCategoryId()+"')";
        statement.executeUpdate(expenseInsert);
        connection.close();
    }

    public static boolean expenseExists(String name) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM expense WHERE name = ?");
        ps.setString (1, name);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public static  void remove(String name) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement("delete from expense where name=?");
        ps.setString(1, name);
        ps.execute();
        connection.close();
    }
}

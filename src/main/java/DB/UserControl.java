package DB;

import Model.User;
import Model.UserType;
import utils.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserControl {

    public static  void create(User user) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        Statement statement = connection.createStatement();
        String userCreate = "INSERT INTO user(login, password, name, surname, phoneNumber, userType, systemId) " +
                "VALUES('"+user.getLogin()+"', '"+user.getPassword()+"', '"+user.getName()+"', '"+user.getSurname()+"', '"+user.getPhoneNumber()+"', '"+user.getType().toString()+"', " +
                "(SELECT systemId from system where systemId='"+user.getFinanceManagementSystemId()+"'))";
        statement.executeUpdate(userCreate);
        connection.close();
    }

    public static boolean userExists(String login) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM user WHERE login = ?");
        ps.setString (1, login);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public static User findUserByLoginAndPassword(String inputLogin, String inputPassword) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM user WHERE login = ? and password = ?");
        ps.setString (1, inputLogin);
        ps.setString (2, inputPassword);
        ResultSet loggedUser = ps.executeQuery();
        if(!loggedUser.next()){
            connection.close();
            return null;
        }
        int id = loggedUser.getInt("userId");
        String login = loggedUser.getString("login");
        String password = loggedUser.getString("password");
        String name = loggedUser.getString("name");
        String surname = loggedUser.getString("surname");
        String phoneNumber = loggedUser.getString("phoneNumber");
        UserType userType = UserType.valueOf(loggedUser.getString("userType"));
        int system = loggedUser.getInt("systemId");
        connection.close();
        return new User(id, name, surname, login, password, phoneNumber, userType, system);
    }

    public static User findUserById(int userId) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM user WHERE userId = ?");
        ps.setInt (1, userId);
        ResultSet loggedUser = ps.executeQuery();
        if(!loggedUser.next()){
            connection.close();
            return null;
        }
        int id = loggedUser.getInt("userId");
        String login = loggedUser.getString("login");
        String password = loggedUser.getString("password");
        String name = loggedUser.getString("name");
        String surname = loggedUser.getString("surname");
        String phoneNumber = loggedUser.getString("phoneNumber");
        UserType userType = UserType.valueOf(loggedUser.getString("userType"));
        int system = loggedUser.getInt("systemId");
        connection.close();
        return new User(id, name, surname, login, password, phoneNumber, userType, system);
    }

    public static User findUserByLogin(String login) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM user WHERE login = ?");
        ps.setString (1, login);
        ResultSet loggedUser = ps.executeQuery();
        if(!loggedUser.next()){
            connection.close();
            return null;
        }
        int id = loggedUser.getInt("userId");
        login = loggedUser.getString("login");
        String password = loggedUser.getString("password");
        String name = loggedUser.getString("name");
        String surname = loggedUser.getString("surname");
        String phoneNumber = loggedUser.getString("phoneNumber");
        UserType userType = UserType.valueOf(loggedUser.getString("userType"));
        int system = loggedUser.getInt("systemId");
        connection.close();
        return new User(id, name, surname, login, password, phoneNumber, userType, system);
    }

    public static  void updateInformation(User user) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("update user set name =?, surname =?, phoneNumber=? where userId=?");
        ps.setString (1, user.getName());
        ps.setString(2, user.getSurname());
        ps.setString(3, user.getPhoneNumber());
        ps.setInt(4, user.getId());
        ps.executeUpdate();
        connection.close();
    }

    public static  void updatePassword(User user) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("update user set password =? where userId=?");
        ps.setString (1, user.getPassword());
        ps.setInt(2, user.getId());
        ps.executeUpdate();
        connection.close();
    }

    public static boolean usersExistInSystem(int systemId) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM user WHERE systemId = ?");
        ps.setInt (1, systemId);
        ResultSet user = ps.executeQuery();
        return user.next();
    }

    public static List<User> getUsers(int categoryId) throws ClassNotFoundException, SQLException {
        List<User> users = new ArrayList<>();
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT userId FROM category_user WHERE categoryId = ?");
        ps.setInt (1, categoryId);
        ResultSet userQuery = ps.executeQuery();

        while(userQuery.next()){
            User user = findUserById(userQuery.getInt("userId"));
            users.add(user);
        }
        return users;
    }

    public static List<User> getUsers() throws ClassNotFoundException, SQLException {
        List<User> users = new ArrayList<>();
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM user");
        ResultSet userQuery = ps.executeQuery();

        while(userQuery.next()){
            int id = userQuery.getInt("userId");
            String login = userQuery.getString("login");
            String password = userQuery.getString("password");
            String name = userQuery.getString("name");
            String surname = userQuery.getString("surname");
            String phoneNumber = userQuery.getString("phoneNumber");
            UserType userType = UserType.valueOf(userQuery.getString("userType"));
            int system = userQuery.getInt("systemId");
            User user = new User(id, name, surname, login, password, phoneNumber, userType, system);
            users.add(user);
        }
        connection.close();
        return users;
    }
}

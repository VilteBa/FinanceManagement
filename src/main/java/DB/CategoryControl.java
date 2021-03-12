package DB;

import Model.Category;
import Model.User;
import utils.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryControl {

    public static boolean categoriesExistInSystem(int systemId) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM category WHERE systemId = ?");
        ps.setInt (1, systemId);
        ResultSet user = ps.executeQuery();
        return user.next();
    }

    public static List<Category> getMainCategories(int systemId) throws ClassNotFoundException, SQLException {
        List<Category> allCategories = new ArrayList<>();
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM category WHERE systemId = ? and parentId is NULL");
        ps.setInt (1, systemId);
        ResultSet categories = ps.executeQuery();

        while(categories.next()){
            allCategories.add(new Category(categories.getInt("categoryId"),
                    categories.getString("name"),
                    categories.getString("description"),
                    categories.getDate("dateCategoryCreated"),
                    0));
        }
        return allCategories;
    }

    public static Category findCategory(int categoryId) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM category WHERE categoryId=?");
        ps.setInt (1, categoryId);
        ResultSet category = ps.executeQuery();
        if(!category.next()){
            connection.close();
            return null;
        }

        Category resultCategory = new Category(category.getInt("categoryId"),
                category.getString("name"),
                category.getString("description"),
                category.getDate("dateCategoryCreated"),
                category.getInt("parentId"));
        connection.close();
        return resultCategory;
    }

    public static Category findCategory(String categoryName) throws SQLException, ClassNotFoundException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM category WHERE name=?");
        ps.setString (1, categoryName);
        ResultSet category = ps.executeQuery();
        if(!category.next()){
            connection.close();
            return null;
        }
        Category resultCategory = new Category(category.getInt("categoryId"),
                category.getString("name"),
                category.getString("description"),
                category.getDate("dateCategoryCreated"),
                category.getInt("parentId"));
        connection.close();
        return resultCategory;
    }

    public static void createMainCategory(Category category) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO category(name, description, dateCategoryCreated, systemId) VALUES( ?,?,?, (SELECT systemId from system where systemId=?))");
        ps.setString(1, category.getName());
        ps.setString(2, category.getDescription());
        ps.setDate(3, new Date(category.getDateCategoryCreated().getTime()));
        ps.setInt(4, category.getFinanceManagementSystemId());
        ps.executeUpdate();
        connection.close();
    }

    public static void createSubcategory(Category category) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO category(name, description, dateCategoryCreated, parentId, systemId) VALUES( ?,?,?,?,(SELECT systemId from system where systemId=?))");
        ps.setString(1, category.getName());
        ps.setString(2, category.getDescription());
        ps.setDate(3, new Date(category.getDateCategoryCreated().getTime()));
        ps.setInt(4, category.getParentCategoryId());
        ps.setInt(5, category.getFinanceManagementSystemId());
        ps.executeUpdate();
        connection.close();
    }
    
    public static boolean categoryExists(String name) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM category WHERE name = ?");
        ps.setString (1, name);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public static boolean subcategoriesExistInCategory(int parentId) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM category WHERE parentId = ?");
        ps.setInt (1, parentId);
        ResultSet user = ps.executeQuery();
        return user.next();
    }

    public static List<Category> getSubcategories(int parentId) throws ClassNotFoundException, SQLException {
        List<Category> allCategories = new ArrayList<>();
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT * FROM category WHERE parentId = ?");
        ps.setInt (1, parentId);
        ResultSet categories = ps.executeQuery();

        while(categories.next()){
            allCategories.add(new Category(categories.getInt("categoryId"),
                    categories.getString("name"),
                    categories.getString("description"),
                    categories.getDate("dateCategoryCreated"),
                    parentId));
        }
        return allCategories;
    }

    public static  void update(Category category) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("update category set name =?, description =? where categoryId=?");
        ps.setString (1, category.getName());
        ps.setString(2, category.getDescription());
        ps.setInt(3, category.getId());
        ps.executeUpdate();
        connection.close();
    }

    public static void addResponsibleUser(int categoryId, int userId) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO category_user(userId, categoryId) VALUES(?,?)");
        ps.setInt(1, userId);
        ps.setInt(2, categoryId);
        ps.executeUpdate();
        connection.close();
    }

    public static void removeResponsibleUser(int categoryId, int userId) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement("delete from category_user where categoryId=? and userId=?");
        ps.setInt(1, categoryId);
        ps.setInt(2, userId);
        ps.execute();
        connection.close();
    }

    public static boolean isResponsibleUser(int categoryId, int userId) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement("Select * from category_user where userId=? and categoryId=?");
        ps.setInt(1, userId);
        ps.setInt(2, categoryId);
        ResultSet user = ps.executeQuery();
        return user.next();
    }

    public static void remove(Category category) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement("delete from category where categoryId=?");
        ps.setInt(1, category.getId());
        ps.execute();
        connection.close();
    }

    public static List<Category> getCategories(int userId) throws ClassNotFoundException, SQLException {
        List<Category> categories = new ArrayList<>();
        Connection connection = DatabaseUtils.connectToDb();
        PreparedStatement ps = connection.prepareStatement
                ("SELECT categoryId FROM category_user WHERE userId = ?");
        ps.setInt (1, userId);
        ResultSet cat = ps.executeQuery();

        while(cat.next()){
            Category category = findCategory(cat.getInt("categoryId"));
            categories.add(category);
        }
        return categories;
    }
}

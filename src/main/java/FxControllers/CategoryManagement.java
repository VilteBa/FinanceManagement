package FxControllers;

import DB.CategoryControl;
import Model.Category;
import Model.FinanceManagementSystem;
import Model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CategoryManagement implements Initializable {

    public ListView<String> categoryList;
    public Button categoryInfoBtn;
    public Button manageSubcategoriesBtn;
    public Button deleteCategoryBtn;
    public Button backBtn;
    public Button backParentBtn;
    public Button addCategoryBtn;
    private Category parentCategory;
    private FinanceManagementSystem financeManagementSystem;
    private User loggedUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setFinanceManagementSystem(FinanceManagementSystem financeManagementSystem) throws SQLException, ClassNotFoundException {
        this.financeManagementSystem = financeManagementSystem;
        fillCategoryListWithData();
        if(parentCategory == null) {
            backParentBtn.setVisible(false);
        }
    }

    private void fillCategoryListWithData() throws SQLException, ClassNotFoundException {
        categoryList.getItems().clear();
        if(parentCategory == null){
            if(CategoryControl.categoriesExistInSystem(financeManagementSystem.getId())){
                List<Category> allCategories = CategoryControl.getMainCategories(financeManagementSystem.getId());
                allCategories.forEach(category -> categoryList.getItems().add(category.getName()+": "+category.getDescription()));
            }
        }
        else{
            if(CategoryControl.subcategoriesExistInCategory(parentCategory.getId())){
                List<Category> allCategories = CategoryControl.getSubcategories(parentCategory.getId());
                allCategories.forEach(category -> categoryList.getItems().add(category.getName()+": "+category.getDescription()));
            }
        }
    }

    public void showCategoryInfo(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        String[] categoryData = categoryList.getSelectionModel().getSelectedItem().toString().split(": ");
        Category category = CategoryControl.findCategory(categoryData[0]);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CategoryInfo.fxml"));
        Parent root = loader.load();
        CategoryInfo categoryInfo = loader.getController();
        categoryInfo.setLoggedUser(loggedUser);
        categoryInfo.setFinanceManagementSystem(financeManagementSystem);
        categoryInfo.setUpdatingCategory(category);
        Stage stage = (Stage)categoryInfoBtn.getScene().getWindow();
        stage.setTitle("Category information");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void manageSubcategories(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        String[] categoryData = categoryList.getSelectionModel().getSelectedItem().toString().split(": ");
        Category parentCategory = CategoryControl.findCategory(categoryData[0]);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CategoriesManagement.fxml"));
        Parent root = loader.load();

        CategoryManagement subcategoryManagement = loader.getController();
        subcategoryManagement.setParentCategory(parentCategory);
        subcategoryManagement.setFinanceManagementSystem(financeManagementSystem);
        subcategoryManagement.setLoggedUser(loggedUser);
        Stage stage = (Stage)backBtn.getScene().getWindow();

        stage.setTitle("Finance Management System");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void backToMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainSystemWindow.fxml"));
        Parent root = loader.load();

        MainSystemWindow mainSystemWindow = loader.getController();
        mainSystemWindow.setLoggedUser(loggedUser);
        mainSystemWindow.setFinanceManagementSystem(financeManagementSystem);

        Stage stage = (Stage)backBtn.getScene().getWindow();

        stage.setTitle("Finance Management System");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void backToParen(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CategoriesManagement.fxml"));
        Parent root = loader.load();

        CategoryManagement categoryManagement = loader.getController();
        categoryManagement.setParentCategory(CategoryControl.findCategory(parentCategory.getParentCategoryId()));
        categoryManagement.setFinanceManagementSystem(financeManagementSystem);
        categoryManagement.setLoggedUser(loggedUser);
        Stage stage = (Stage)backBtn.getScene().getWindow();

        stage.setTitle("Finance Management System");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void deleteCategory(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String[] categoryData = categoryList.getSelectionModel().getSelectedItem().toString().split(": ");
        Category category = CategoryControl.findCategory(categoryData[0]);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to delete this category?");
        alert.setContentText("Deleting a category will delete the subcategories");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            CategoryControl.remove(category);
        }
        fillCategoryListWithData();
    }

    public void addCategory(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Category Dialog");
        dialog.setHeaderText("Add category");
        ButtonType addBtn = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtn, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField name = new TextField();
        name.setPromptText("Category");
        TextField description = new TextField();
        description.setPromptText("Description");
        grid.add(new Label("Category:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(description, 1, 1);
        Node loginButton = dialog.getDialogPane().lookupButton(addBtn);
        loginButton.setDisable(true);
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(name::requestFocus);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addBtn) {
                return new Pair<>(name.getText(), description.getText());
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(category -> {
            try {
                if (CategoryControl.categoryExists(category.getKey()))
                    return;
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            if(parentCategory != null){
                try {
                    CategoryControl.createSubcategory(new Category(category.getKey(), category.getValue(), parentCategory.getId(), financeManagementSystem.getId()));
                    CategoryControl.addResponsibleUser(CategoryControl.findCategory(category.getKey()).getId(), loggedUser.getId());
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    CategoryControl.createMainCategory(new Category(category.getKey(), category.getValue(), 0, financeManagementSystem.getId()));
                    CategoryControl.addResponsibleUser(CategoryControl.findCategory(category.getKey()).getId(), loggedUser.getId());
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        fillCategoryListWithData();
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }


}

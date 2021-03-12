package FxControllers;

import DB.CategoryControl;
import DB.ExpenseControl;
import DB.IncomeControl;
import DB.UserControl;
import Model.*;
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

public class CategoryInfo implements Initializable {
    public TextField categoryName;
    public TextField parentCategory;
    public Button backBtn;
    public Button informationBtn;
    public TextArea description;
    public ListView<String> userList;
    public ListView<String> expenseList;
    public ListView<String> incomeList;
    public Button userInfoBtn;
    public Button userAddBtn;
    public Button removeUserBtn;
    public Button expenseInfoBtn;
    public Button expenseAddBtn;
    public Button expenseRemoveBtn;
    public Button incomeInfoBtn;
    public Button incomeAddBtn;
    public Button incomeRemoveBtn;
    private Category updatingCategory = null;
    private FinanceManagementSystem financeManagementSystem;
    private User loggedUser;

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void setFinanceManagementSystem(FinanceManagementSystem financeManagementSystem) {
        this.financeManagementSystem = financeManagementSystem;
    }

    public void setUpdatingCategory(Category category) throws SQLException, ClassNotFoundException {
        this.updatingCategory = category;
        categoryName.setText(category.getName());
        description.setText(category.getDescription());
        parentCategory.setText(category.getParentCategoryId() !=0 ? CategoryControl.findCategory(category.getParentCategoryId()).getName() : "  —  ");
        fillUserListWithData();
        fillExpenseListWithData();
        fillIncomeListWithData();
    }

    private void fillIncomeListWithData() throws SQLException, ClassNotFoundException {
        incomeList.getItems().clear();
        List<Income> incomes = IncomeControl.getIncomes(updatingCategory.getId());
        if(incomes.size() != 0){
            incomes.forEach(income -> incomeList.getItems().add(income.getName() + " : " + income.getAmount()));
        }
    }

    private void fillExpenseListWithData() throws SQLException, ClassNotFoundException {
        expenseList.getItems().clear();
        List<Expense> expenses = ExpenseControl.getExpenses(updatingCategory.getId());
        if(expenses.size() != 0){
            expenses.forEach(expense -> expenseList.getItems().add(expense.getName() + " : " + expense.getAmount()));
        }
    }

    private void fillUserListWithData() throws SQLException, ClassNotFoundException {
        userList.getItems().clear();
        List<User> users = UserControl.getUsers(updatingCategory.getId());
        if(users.size() != 0){
            users.forEach(user -> userList.getItems().add(user.getName() + " " + user.getSurname() + " (" + user.getLogin() + ")"));
        }
    }

    public void back(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CategoriesManagement.fxml"));
        Parent root = loader.load();
        CategoryManagement categoryManagement = loader.getController();
        categoryManagement.setFinanceManagementSystem(financeManagementSystem);
        categoryManagement.setLoggedUser(loggedUser);
        categoryManagement.setParentCategory(updatingCategory);
        Stage stage = (Stage)backBtn.getScene().getWindow();
        stage.setTitle("Finance Management System");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void addUser(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        List<String> choices = new ArrayList<>();
        UserControl.getUsers().forEach(user -> choices.add(user.getName() + " " + user.getSurname() + " (" + user.getLogin() + ")"));
        ChoiceDialog<String> dialog = new ChoiceDialog<>("", choices);
        dialog.setTitle("Choice Dialog");
        dialog.setHeaderText("User choice Dialog");
        dialog.setContentText("Choose responsible user:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            String user = result.get();
            user = user.substring(user.indexOf("(") + 1);
            user = user.substring(0, user.indexOf(")"));
            User responsibleUser = UserControl.findUserByLogin(user);
            if(CategoryControl.isResponsibleUser(updatingCategory.getId(), responsibleUser.getId())){
                return;
            }
            CategoryControl.addResponsibleUser(updatingCategory.getId(), responsibleUser.getId());
            fillUserListWithData();
        }
    }

    public void removeUser(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(UserControl.getUsers(updatingCategory.getId()).size()==1){
            alertMessage("Cannot remove last responsible user", "If you want to remove user, add another one");
            return;
        }
        String user = userList.getSelectionModel().getSelectedItem();
        user = user.substring(user.indexOf("(") + 1);
        user = user.substring(0, user.indexOf(")"));

        CategoryControl.removeResponsibleUser(updatingCategory.getId(), UserControl.findUserByLogin(user).getId());
        fillUserListWithData();
    }

    public void expenseAdd(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Dialog<Pair<String, String>> dialog = getPairDialog("Add Expense Dialog", "Add Expense");
        ButtonType addBtn = getButtonType(dialog);
        GridPane grid = getGridPane();
        TextField expense = new TextField();
        expense.setPromptText("Value");
        TextField value = new TextField();
        value.setPromptText("Value");
        setGrid(grid, expense, value);
        Node loginButton = dialog.getDialogPane().lookupButton(addBtn);
        loginButton.setDisable(true);
        expense.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(expense::requestFocus);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addBtn) {
                return new Pair<>(expense.getText(), value.getText());
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(e -> {
            try {
                if (ExpenseControl.expenseExists(e.getKey())) return;
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
            try {
                ExpenseControl.create(new Expense(e.getKey(), Integer.parseInt(e.getValue()), updatingCategory.getId()));
            } catch (ClassNotFoundException | SQLException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });
        fillExpenseListWithData();
    }

    public void expenseRemove(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String expense = expenseList.getSelectionModel().getSelectedItem().split(":")[0].trim();
        ExpenseControl.remove(expense);
        fillExpenseListWithData();
    }

    public void addIncome(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Dialog<Pair<String, String>> dialog = getPairDialog("Add Income Dialog", "Add Income");
        ButtonType addBtn = getButtonType(dialog);
        GridPane grid = getGridPane();
        TextField income = new TextField();
        income.setPromptText("Name");
        TextField value = new TextField();
        value.setPromptText("Value");
        setGrid(grid, income, value);
        Node loginButton = dialog.getDialogPane().lookupButton(addBtn);
        loginButton.setDisable(true);
        income.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(income::requestFocus);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addBtn) {
                return new Pair<>(income.getText(), value.getText());
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(i -> {
            try {
                if (IncomeControl.incomeExists(i.getKey())) return;
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
            try {
                IncomeControl.create(new Income(i.getKey(), Integer.parseInt(i.getValue()), updatingCategory.getId()));
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        });
        fillIncomeListWithData();
    }

    public void incomeRemove(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String income = incomeList.getSelectionModel().getSelectedItem().split(":")[0].trim();
        IncomeControl.remove(income);
        fillIncomeListWithData();
    }

    public void changeInformation(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (categoryName.getText().trim().isEmpty()) {
            alertMessage(null, "Category name cannot be empty");
            return;
        }
        if (description.getText().trim().isEmpty()) {
            alertMessage(null, "Description cannot be empty");
        }
        updatingCategory.setName(categoryName.getText().trim());
        updatingCategory.setDescription(description.getText().trim());
        CategoryControl.update(updatingCategory);

    }

    private void setGrid(GridPane grid, TextField income, TextField value) {
        grid.add(new Label("Name:"), 0, 0);
        grid.add(income, 1, 0);
        grid.add(new Label("Value:"), 0, 1);
        grid.add(value, 1, 1);
    }

    private ButtonType getButtonType(Dialog<Pair<String, String>> dialog) {
        ButtonType addBtn = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtn, ButtonType.CANCEL);
        return addBtn;
    }

    private Dialog<Pair<String, String>> getPairDialog(String s, String s2) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle(s);
        dialog.setHeaderText(s2);
        return dialog;
    }
    private GridPane getGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        return grid;
    }

    private void alertMessage(String o, String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(o);
        alert.setContentText(s);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

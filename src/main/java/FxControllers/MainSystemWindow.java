package FxControllers;

import DB.ExpenseControl;
import DB.IncomeControl;
import Model.FinanceManagementSystem;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainSystemWindow implements Initializable {
    @FXML
    public Button categoriesManagementBtn;
    public Button userBtn;
    public Button logOutBtn;
    public Label version;
    public Label income;
    public Label expense;

    private FinanceManagementSystem financeManagementSystem;
    private User loggedUser;


    public void loadCategoriesManagement(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CategoriesManagement.fxml"));
        Parent root = loader.load();

        CategoryManagement categoryManagement = loader.getController();
        categoryManagement.setFinanceManagementSystem(financeManagementSystem);
        categoryManagement.setLoggedUser(loggedUser);
        Stage stage = (Stage)categoriesManagementBtn.getScene().getWindow();

        stage.setTitle("Finance Management System");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setFinanceManagementSystem(FinanceManagementSystem financeManagementSystem) {
        this.financeManagementSystem = financeManagementSystem;
        version.setText(financeManagementSystem.getSystemVersion());
    }

    public void userSettings(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/UserSettings.fxml"));
        Parent root = loader.load();

        UserSettings userSettings = loader.getController();
        userSettings.setFinanceManagementSystem(financeManagementSystem);
        userSettings.setLoggedUser(loggedUser);
        Stage stage = (Stage)userBtn.getScene().getWindow();

        stage.setTitle("User settings");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("Are you ok with this?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LoginPage.fxml"));
            Parent root = loader.load();
            LoginPage loginPage = loader.getController();
            Stage stage = (Stage)logOutBtn.getScene().getWindow();
            stage.setTitle("Finance Management System");
            stage.setScene(new Scene(root));
            stage.show();
        }

    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
        userBtn.setText(loggedUser.getName() + " " + loggedUser.getSurname());
    }

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        expense.setText(ExpenseControl.getTotalExpense());
        income.setText(IncomeControl.getTotalIncome());
    }
}

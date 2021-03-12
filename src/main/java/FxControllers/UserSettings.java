package FxControllers;

import DB.UserControl;
import Model.FinanceManagementSystem;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserSettings implements Initializable {

    public TextField name;
    public TextField surname;
    public TextField phonenumber;
    public PasswordField oldPassword;
    public PasswordField newPassword;
    public Button passwordBtn;
    public Button informationBtn;
    public Button backBtn;
    public TextField login;
    public TextField userType;
    private FinanceManagementSystem financeManagementSystem;
    private User loggedUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setFinanceManagementSystem(FinanceManagementSystem financeManagementSystem) {
        this.financeManagementSystem = financeManagementSystem;
    }

    public void changeInformation(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (name.getText().trim().isEmpty()) {
            alertMessage(Alert.AlertType.ERROR, "Error Dialog", "Name cannot be empty");
            return;
        }
        if (surname.getText().trim().isEmpty()) {
            alertMessage(Alert.AlertType.ERROR, "Error Dialog", "Surname cannot be empty");
            return;
        }
        if (phonenumber.getText().trim().isEmpty()) {
            alertMessage(Alert.AlertType.ERROR, "Error Dialog", "PhoneNumber cannot be empty");
            return;
        }
        loggedUser.setName(name.getText().trim());
        loggedUser.setSurname(surname.getText().trim());
        loggedUser.setPhoneNumber(phonenumber.getText().trim());
        UserControl.updateInformation(loggedUser);
        alertMessage(Alert.AlertType.INFORMATION, "Information Dialog", "Information changed successfully");
    }

    private void alertMessage(Alert.AlertType error, String s, String s2) {
        Alert alert = new Alert(error);
        alert.setTitle(s);
        alert.setHeaderText(null);
        alert.setContentText(s2);
        alert.showAndWait();
    }

    public void changePassword(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(oldPassword.getText().equals(loggedUser.getPassword()) && !newPassword.getText().trim().isEmpty()){
            loggedUser.setPassword(newPassword.getText().trim());
            UserControl.updatePassword(loggedUser);
            alertMessage(Alert.AlertType.INFORMATION, "Information Dialog", "Password changed successfully");
            return;
        }
        alertMessage(Alert.AlertType.ERROR, "Error Dialog", "Wrong password");

    }

    public void backToMain(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainSystemWindow.fxml"));
        Parent root = loader.load();

        MainSystemWindow mainSystemWindow = loader.getController();
        mainSystemWindow.setFinanceManagementSystem(financeManagementSystem);
        mainSystemWindow.setLoggedUser(loggedUser);
        Stage stage = (Stage)backBtn.getScene().getWindow();

        stage.setTitle("Finance Management System");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
        login.setText(loggedUser.getLogin());
        name.setText(loggedUser.getName());
        surname.setText(loggedUser.getSurname());
        phonenumber.setText(loggedUser.getPhoneNumber());
        userType.setText(loggedUser.getType().toString());
    }
}

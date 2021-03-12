package FxControllers;
import DB.UserControl;
import Model.FinanceManagementSystem;
import Model.User;
import Model.UserType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.DatabaseUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginPage implements Initializable{

    @FXML
    public Button signUpBtn;
    @FXML
    public Button signInBtn;
    public TextField signUpLogin;
    public PasswordField signUpPassword;
    public TextField signUpName;
    public TextField signUpSurname;
    public TextField signUpPhoneNumber;
    public TextField signInLogin;
    public PasswordField signInPassword;
    public ToggleGroup userTypeGroup = new ToggleGroup();
    public RadioButton individual;
    public RadioButton legal;

    private FinanceManagementSystem financeManagementSystem = new FinanceManagementSystem();
    private User loggedUser = new User();

    public void setFinanceManagementSystem(FinanceManagementSystem financeManagementSystem){
        this.financeManagementSystem = financeManagementSystem;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            financeManagementSystem = chooseFinanceManagementSystem();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if (financeManagementSystem == null) {
            System.out.println("No information about system");
            Platform.exit();
        }
        individual.setToggleGroup(userTypeGroup);
        legal.setToggleGroup(userTypeGroup);
    }

    private FinanceManagementSystem chooseFinanceManagementSystem() throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseUtils.connectToDb();
        Statement statement = connection.createStatement();
        ResultSet allSystems = statement.executeQuery("SELECT companyName FROM system");
        List<String> choices = new ArrayList<>();
        while (allSystems.next()){
            choices.add(allSystems.getString(1));
        }
            ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
            dialog.setTitle("Library IS");
            dialog.setHeaderText("Choose Your library IS");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String companyName = result.get();
                ResultSet selectedSystem = statement.executeQuery("SELECT * FROM system WHERE system.companyName='"+companyName+"'");
                selectedSystem.next();
                companyName = selectedSystem.getString(2);
                int id = selectedSystem.getInt(1);
                String version = selectedSystem.getString(3);
                Date date = selectedSystem.getDate(4);
                FinanceManagementSystem financeManagementSystem = new FinanceManagementSystem(id, companyName,version, date);
                connection.close();
                return financeManagementSystem;
            }
        connection.close();
        return null;
    }


    public void createUser(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(UserControl.userExists(signUpLogin.getText())){
            alertMessage(Alert.AlertType.ERROR, "Error Dialog", null, "User already exists");
        }
        else{
            UserType userType = UserType.valueOf(((RadioButton)userTypeGroup.getSelectedToggle()).getText());
            User user = new User(signUpName.getText(),
                    signUpSurname.getText(),
                    signUpLogin.getText(),
                    signUpPassword.getText(),
                    signUpPhoneNumber.getText(),
                    userType,
                    financeManagementSystem.getId());
            UserControl.create(user);
            alertMessage(Alert.AlertType.INFORMATION, "Information Dialog", "User created", null);
        }
    }

    public void validateUser(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {

        if (!UserControl.usersExistInSystem(financeManagementSystem.getId())) {
            alertMessage(Alert.AlertType.WARNING, "Warning Dialog", null, "There are no users");
            return;
        }
        if(signInLogin.getText().isEmpty() ||  signInPassword.getText().isEmpty()){
            alertMessage(Alert.AlertType.ERROR, "Error Dialog", null, "You must fill in all of the fields");
            return;
        }
        loggedUser = UserControl.findUserByLoginAndPassword(signInLogin.getText(), signInPassword.getText());
        if(loggedUser != null){
                loadMainWindow();
        }
        else{
            alertMessage(Alert.AlertType.ERROR, "Error Dialog", null, "User doesn't exist");
        }
    }

    private void alertMessage(Alert.AlertType error, String s, String o, String s2) {
        Alert alert = new Alert(error);
        alert.setTitle(s);
        alert.setHeaderText(o);
        alert.setContentText(s2);
        alert.showAndWait();
    }

    private void loadMainWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainSystemWindow.fxml"));
        Parent root = loader.load();

        MainSystemWindow mainSystemWindow = loader.getController();
        mainSystemWindow.setLoggedUser(loggedUser);
        mainSystemWindow.setFinanceManagementSystem(financeManagementSystem);

        Stage stage = (Stage)signInBtn.getScene().getWindow();

        stage.setTitle("Finance Management System");
        stage.setScene(new Scene(root));
        stage.show();
    }
}

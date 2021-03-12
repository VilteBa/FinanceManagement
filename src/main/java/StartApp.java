
import FxControllers.LoginPage;
import Model.FinanceManagementSystem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DataRW;
import utils.DatabaseUtils;

import java.sql.Connection;

public class StartApp extends Application {

    public static FinanceManagementSystem financeManagementSystem = new FinanceManagementSystem();

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LoginPage.fxml"));
        Parent root = loader.load();

        LoginPage loginPage = loader.getController();

        primaryStage.setTitle("Finance Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        //  DatabaseUtils.disconnectFromDb(connection, statement);
    }


    public static void main(String[] args) {
        launch(args);
    }
}

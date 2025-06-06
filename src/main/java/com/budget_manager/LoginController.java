package com.budget_manager;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML TextField usernameField;
    @FXML TextField passwordField;
    @FXML Label errorLabel;
    Boolean Registered = false;
    
    public void onLogin() throws SQLException, IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = UserDAO.login(username, password);
        if (user != null) {
            System.out.println("User logged in: " + user.getUsername());

            Session.setCurrentUser(user);
            // Proceed to the main application view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/budget_manager/MainView.fxml"));
            Parent root = loader.load();
            //MainController mainController = loader.getController();
            //mainController.setCurrentUser(user);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Budget Manager - " + user.getUsername());
            stage.show();

            // Close the login window
            Stage loginStage = (Stage) usernameField.getScene().getWindow();
            loginStage.close();


        } else {
            System.out.println("Login failed.");
            // Show an error message to the user
            errorLabel.setText("Invalid username or password.");
        }
    }

    public void onRegister() throws IOException, SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Registered = UserDAO.registerUser(username, password);
        if (!Registered) {
            errorLabel.setText("Username already exists. Please choose a different username.");
            return;
        }
        else{
            // Open the registration view
        onLogin();
        }
        
    }
}

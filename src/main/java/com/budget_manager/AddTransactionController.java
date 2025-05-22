package com.budget_manager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddTransactionController {

    @FXML
    private TextField amountField;
    @FXML
    private ComboBox<String> typeBox;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField descriptionField;
    @FXML
    private DatePicker datePicker;

    private Runnable onSaveCallback;

    @FXML
    public void initialize() {
        typeBox.getItems().addAll("INCOME", "EXPENSE");
        datePicker.setValue(java.time.LocalDate.now());
    }

    @FXML
    private void onSave(){
        String amountText = amountField.getText();
        String type = typeBox.getValue();
        String category = categoryField.getText();
        String description = descriptionField.getText();
        java.time.LocalDate date = datePicker.getValue();

        if (type == null || category.isEmpty() || description.isEmpty() || amountText == null) {
                showError("Incomplete Fields.");
                return;
            } 
        
        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            showError("Amount must be a valid number.");
            return;
        }

        Transaction transaction = new Transaction(amount, type, category, description, date);
        TransactionDAO.insertTransaction(transaction);
         if (onSaveCallback != null) {
                onSaveCallback.run();
            }

            closeWindow();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) amountField.getScene().getWindow();
        stage.close();
    }
    
    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }
}

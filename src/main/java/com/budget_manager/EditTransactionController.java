package com.budget_manager;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditTransactionController {

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

    private Transaction transaction;
    private Runnable onSaveCallback;

    @FXML
    public void initialize() {
        typeBox.getItems().addAll("INCOME", "EXPENSE");
    }

    public void setTransaction(Transaction t) {
        this.transaction = t;
        amountField.setText(String.valueOf(t.getAmount()));
        typeBox.setValue(t.getType());
        categoryField.setText(t.getCategory());
        descriptionField.setText(t.getDescription());
        datePicker.setValue(t.getDate());
    }

    @FXML
    private void onSave() {
        // Werte aus Feldern holen und Transaction aktualisieren
        transaction.setAmount(Double.parseDouble(amountField.getText()));
        transaction.setType(typeBox.getValue());
        transaction.setCategory(categoryField.getText());
        transaction.setDescription(descriptionField.getText());
        transaction.setDate(datePicker.getValue());

        TransactionDAO.updateTransaction(transaction); // Diese Methode musst du noch ergänzen!
        if (onSaveCallback != null) {
            onSaveCallback.run();
        }
        closeWindow();
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
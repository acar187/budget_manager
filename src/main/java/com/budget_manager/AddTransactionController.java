package com.budget_manager;

import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.util.StringConverter;

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
    @FXML private ComboBox<Category> categoryBox;

    private List<Category> categoryList;

    private Runnable onSaveCallback;

    @FXML
    public void initialize() {
        typeBox.getItems().addAll("INCOME", "EXPENSE");
        datePicker.setValue(java.time.LocalDate.now());
        
        loadCategories();
    
    }
    private void loadCategories() {
        categoryList = CategoryDAO.getAllCategories();
        categoryBox.setItems(FXCollections.observableArrayList(categoryList));
    }

    @FXML
    private void onAddCategory() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Add New Category");
        dialog.setContentText("Name:");
        

        dialog.showAndWait().ifPresent(name -> {
            if (!name.isBlank()) {
                Category c = new Category(name);
                CategoryDAO.insertCategory(c);
                loadCategories(); // Reload categories after adding a new one
                
                categoryBox.setValue(c); // Set the newly added category as selected
                
            }
        });
    }

    @FXML
    private void onSave(){
        String amountText = amountField.getText();
        String type = typeBox.getValue();
        //String category = categoryBox.getValue() != null ? categoryBox.getValue().getName() : categoryField.getText();
        String category = categoryBox.getValue() != null ? categoryBox.getValue().getName() : null;
        System.out.println("categoryBox = " + categoryBox);
        System.out.println("categoryBox.getValue() = " + categoryBox.getValue());
        if (category == null || category.isEmpty()) {
            showError("Category cannot be empty.");
            return;
        }
        if (type == null) {
            showError("Please select a transaction type (INCOME or EXPENSE).");
            return;
        }
        if (amountText == null || amountText.isEmpty()) {
            showError("Amount cannot be empty.");
            return;
        }
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

        Transaction transaction = new Transaction(amount, type, category, description, date, Session.getCurrentUser().getId());
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

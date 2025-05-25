package com.budget_manager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController{

    // FXML-Elemente
    @FXML private TextField searchField;
    @FXML private ComboBox<String> typeFilterBox;
    @FXML private TextField categoryFilterField;
    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;

    @FXML
    private TableView<Transaction> transactionTable;
    @FXML   
    private TableColumn<Transaction, String> typeColumn;
    @FXML
    private TableColumn<Transaction, String> categoryColumn;
    @FXML
    private TableColumn<Transaction, String> descriptionColumn;
    @FXML
    private TableColumn<Transaction, String> dateColumn;
    @FXML   
    private TableColumn<Transaction, String> amountColumn;

    @FXML private Label incomeSumLabel;
    @FXML private Label expenseSumLabel;
    @FXML private Label balanceLabel;
    
    private final ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    public void initialize() {
        dateColumn.setCellValueFactory(data -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> data.getValue().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        typeColumn.setCellValueFactory(data -> 
            javafx.beans.binding.Bindings.createStringBinding(data.getValue()::getType));
        categoryColumn.setCellValueFactory(data -> 
            javafx.beans.binding.Bindings.createStringBinding(data.getValue()::getCategory));
        descriptionColumn.setCellValueFactory(data -> 
            javafx.beans.binding.Bindings.createStringBinding(data.getValue()::getDescription));
        amountColumn.setCellValueFactory(data -> 
            javafx.beans.binding.Bindings.createStringBinding(() -> String.format("%.2f", data.getValue().getAmount())));


        transactionTable.setItems(transactionList);
         // Lade Daten beim Start
        transactionList.setAll(TransactionDAO.getAllTransactions());
        updateSums();
         // Delay focus until after UI is rendered
        javafx.application.Platform.runLater(() -> transactionTable.requestFocus());
    }

    @FXML
    private void onAddTransactionClicked() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/budget_manager/addTransactionDialog.fxml"));
        Parent root = loader.load();

        AddTransactionController controller = loader.getController();
        controller.setOnSaveCallback(() -> {
            transactionList.setAll(TransactionDAO.getAllTransactions());
            updateSums();
        });

        Stage stage = new Stage();
        stage.setTitle("Neue Transaktion");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    @FXML
    private void onDeleteTransactionClicked() {
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            TransactionDAO.deleteTransaction(selectedTransaction.getId());
            transactionList.setAll(TransactionDAO.getAllTransactions());
            updateSums();
        }
    }

    @FXML
    private void onEditTransactionClicked() {
         System.out.println("Edit Transaction Button wurde geklickt!");
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/budget_manager/editTransactionDialog.fxml"));
                Parent root = loader.load();

                EditTransactionController controller = loader.getController();
                controller.setTransaction(selectedTransaction);
                controller.setOnSaveCallback(() -> {
                    transactionList.setAll(TransactionDAO.getAllTransactions());
                    updateSums();
                });

                Stage stage = new Stage();
                stage.setTitle("Transaktion bearbeiten");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Keine Transaktion ausgewählt!");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Click on a Transaction Field", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void onFilterClicked() {
    // Beispiel: Filter-Logik oder einfach ein Testausdruck
    System.out.println("Filter-Button geklickt!");

    String search = searchField.getText().toLowerCase();
    String type = typeFilterBox.getValue();
    String category = categoryFilterField.getText().toLowerCase();
    LocalDate from = fromDatePicker.getValue();
    LocalDate to = toDatePicker.getValue();

    List<Transaction> filtered = TransactionDAO.getAllTransactions().stream()
        .filter(t -> (search.isEmpty() ||
                      t.getDescription().toLowerCase().contains(search) ||
                      String.valueOf(t.getAmount()).contains(search)))
        .filter(t -> (type == null || type.equals("ALL") || t.getType().equals(type)))
        .filter(t -> (category.isEmpty() || t.getCategory().toLowerCase().contains(category)))
        .filter(t -> (from == null || !t.getDate().isBefore(from)))
        .filter(t -> (to == null || !t.getDate().isAfter(to)))
        .collect(Collectors.toList());

    transactionList.setAll(filtered);
    updateSums();
    }
    
    @FXML
    private void onResetFilterClicked() {
        System.out.println("Reset-Button geklickt!");
        searchField.clear();
        typeFilterBox.setValue("ALL");
        categoryFilterField.clear();
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);    
        transactionList.setAll(TransactionDAO.getAllTransactions());
        updateSums();
    }

    private void updateSums() {
        double income = transactionList.stream()
            .filter(t -> "INCOME".equalsIgnoreCase(t.getType()))
            .mapToDouble(Transaction::getAmount)
            .sum();

        double expense = transactionList.stream()
            .filter(t -> "EXPENSE".equalsIgnoreCase(t.getType()))
            .mapToDouble(Transaction::getAmount)
            .sum();

        double balance = income - expense;

        incomeSumLabel.setText(String.format("Income: %.2f €", income));
        expenseSumLabel.setText(String.format("Expense: %.2f €", expense));
        balanceLabel.setText(String.format("Balance: %.2f €", balance));
    }
}
package com.budget_manager;

import java.time.format.DateTimeFormatter;
import java.util.Observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController{

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
    }

    @FXML
private void onAddTransactionClicked() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/budget_manager/addTransactionDialog.fxml"));
        Parent root = loader.load();

        AddTransactionController controller = loader.getController();
        controller.setOnSaveCallback(() -> {
            transactionList.setAll(TransactionDAO.getAllTransactions());
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

    
}
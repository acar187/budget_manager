package com.budget_manager;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.stream.Collectors;

import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
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

    @FXML private PieChart expensePieChart;

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
        updateExpensePieChart();
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
            updateExpensePieChart();
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
            updateExpensePieChart();
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
                    updateExpensePieChart();
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
    updateExpensePieChart();
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

    private void updateExpensePieChart() {
        Map<String, Double> categorySums = transactionList.stream()
            .filter(t -> "EXPENSE".equalsIgnoreCase(t.getType()))
            .collect(Collectors.groupingBy(
            Transaction::getCategory,
            Collectors.summingDouble(Transaction::getAmount)
            ));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Double> entry : categorySums.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        expensePieChart.setData(pieChartData);
}
    @FXML
        private void onExportCsvClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportiere Transaktionen als CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV-Files", "*.csv"));
        java.io.File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
             // Kopfzeile
                writer.write("Datum,Typ,Kategorie,Beschreibung,Betrag\n");
                for (Transaction t : transactionList) {
                    writer.write(String.format("%s,%s,%s,%s,%.2f\n",
                        t.getDate(),
                        t.getType(),
                        t.getCategory(),
                        t.getDescription().replace(",", " "), // Kommas entfernen
                        t.getAmount()
                    ));
                }
                writer.flush();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Export erfolgreich!", ButtonType.OK);
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler beim Export!", ButtonType.OK);
                alert.showAndWait();
            }
     }
    }

    @FXML
    private void onExportPdfClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportiere Transaktionen als PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF-Dateien", "*.pdf"));
        java.io.File file = fileChooser.showSaveDialog(null);
        if (file != null) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new java.io.FileOutputStream(file));
            document.open();

            // Titel
            document.add(new Paragraph("Transaktionen", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            document.add(new Paragraph(" ")); // Leerzeile

            // Tabelle
            PdfPTable table = new PdfPTable(5);
            table.addCell("Datum");
            table.addCell("Typ");
            table.addCell("Kategorie");
            table.addCell("Beschreibung");
            table.addCell("Betrag");

            for (Transaction t : transactionList) {
                table.addCell(t.getDate().toString());
                table.addCell(t.getType());
                table.addCell(t.getCategory());
                table.addCell(t.getDescription());
                table.addCell(String.format("%.2f", t.getAmount()));
            }

            document.add(table);
            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Export als PDF erfolgreich!", ButtonType.OK);
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler beim PDF-Export!", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
}
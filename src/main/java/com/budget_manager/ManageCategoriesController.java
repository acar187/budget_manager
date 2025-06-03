package com.budget_manager;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;



public class ManageCategoriesController {

   
    @FXML private TableColumn<Category, String> nameColumn;
    @FXML private TableView<Category> categoryTable;
    @FXML private ComboBox<Category> categoryBox;
    private Runnable onCategoryChangedCallback;
    private final ObservableList<Category> categoryList = FXCollections.observableArrayList();
    private Category category;
    @FXML
    private TextField categoryField;
    private TextInputControl dialogControl;

    @FXML   
    public void initialize() {
        // Set up the category table
        nameColumn.setCellValueFactory(data -> 
            javafx.beans.binding.Bindings.createStringBinding(data.getValue()::getName));
        categoryTable.setItems(categoryList);
        //  // Lade Daten beim Start
        categoryList.setAll(CategoryDAO.getAllCategories());
       
    }

    @FXML
    private void onAddCategoryClicked() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Add New Category");
        dialog.setContentText("Name:");
        dialog.showAndWait().ifPresent(name -> {
            if (!name.isBlank()) {
                Category c = new Category(name);
                CategoryDAO.insertCategory(c);
                if (onCategoryChangedCallback != null) {
                onCategoryChangedCallback.run();
            }
                //categoryList.add(c); // Add the new category to the list
                categoryTable.getItems().add(c); // Add the new category to the table    
            }
        });
    }

    @FXML void onDeleteClicked() {
        if (getSelectedCategory() != null) {
            Category selectedCategory = getSelectedCategory();
            CategoryDAO.deleteCategory(selectedCategory);
            categoryList.remove(selectedCategory);
        } else {
            System.out.println("No category selected for deletion.");
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a category to delete.");
            alert.setTitle("No Category Selected");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }
    @FXML void onEditClicked() {

        if (getSelectedCategory() == null) {
            Alert alert = new Alert(null);
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("No Category Selected");
            alert.setHeaderText("Please select a category to edit.");
            alert.showAndWait();

        }
        TextInputDialog dialog = new TextInputDialog(getSelectedCategory().getName());
        dialog.setHeaderText("Edit Category");
        dialog.setContentText("New Name:");
        dialog.showAndWait().ifPresent(newName -> {
            if (!newName.isBlank()) {
                Category selectedCategory = getSelectedCategory(); 
                selectedCategory.setName(newName);
                CategoryDAO.updateCategory(selectedCategory);
                categoryList.set(categoryList.indexOf(selectedCategory), selectedCategory); // Update the list 
            }
        });
    }
    
    private Category getSelectedCategory() {
        // Implement logic to get the currently selected category
        // This could be from a ComboBox or ListView in your UI
        if (categoryTable.getSelectionModel().getSelectedItem() != null) {
            return categoryTable.getSelectionModel().getSelectedItem();
        }
        return null;
    }
    @FXML void onCloseClicked() {
        // Close the manage categories window
        // This could be done by getting the current stage and closing it
        Stage stage = (Stage) categoryTable.getScene().getWindow();
        stage.close();
    }
    
    public void setOnCategoryChangedCallback(Runnable callback) {
        // For example, you can call this after adding, editing, or deleting a category
        this.onCategoryChangedCallback = callback;
    }   
}

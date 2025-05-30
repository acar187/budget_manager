package com.budget_manager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/budget_manager/MainView.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Budget Manager");
        primaryStage.show();

        Category category = new Category(1, "Food");
        System.out.println("Category ID: " + category.getId());
        System.out.println("Category Name: " + category.getName());
        
        // System.out.println("All Categories: " + CategoryDAO.getAllCategories());
        // CategoryDAO.insertCategory(new Category("Transport"));
        // System.out.println("After inserting new category: " + CategoryDAO.getAllCategories());
        
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}

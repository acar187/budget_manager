module com.budget_manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;
    requires javafx.graphics;

    opens com.budget_manager to javafx.fxml;
    exports com.budget_manager;
}

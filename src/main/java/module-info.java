module com.budget_manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;

    opens com.budget_manager to javafx.fxml;
    exports com.budget_manager;
}

module com.cars_management {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires javafx.base;

    requires transitive javafx.graphics; // Ensure Stage is accessible

    opens com.cars_management.Controller to javafx.fxml;
    opens com.cars_management.Controller.Cars to javafx.fxml, javafx.base;
    opens com.cars_management.Controller.DashBord to javafx.fxml;
    opens com.cars_management.Controller.Reservations to javafx.fxml, javafx.base;
    opens com.cars_management.Controller.Contracts to javafx.fxml, javafx.base;
    opens com.cars_management.Controller.Auth to javafx.fxml, javafx.base;

    exports com.cars_management;
    opens com.cars_management.Repository to javafx.base, javafx.fxml;
}

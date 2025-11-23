module com.cars_management {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.base;

    opens com.cars_management.Controller to javafx.fxml;
    opens com.cars_management.Controller.Cars to javafx.fxml, javafx.base;
    opens com.cars_management.Controller.DashBord to javafx.fxml;
    opens com.cars_management.Controller.Reservations to javafx.fxml, javafx.base;

    exports com.cars_management;
}

module myjfx {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    opens ServerApplication to javafx.graphics, javafx.fxml;
    opens ClientApplications to javafx.graphics, javafx.fxml;
    opens NetworkUtilities to javafx.fxml, javafx.graphics;
    opens RestaurantUtilities to javafx.fxml, javafx.graphics, javafx.base, javafx.controls;
}
module dining {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.logging;

    opens no.os.DiningPhilsopher.gui.controller to javafx.graphics, javafx.fxml;
    opens no.os.DiningPhilsopher.gui.window to javafx.fxml, javafx.graphics;
    opens no.os.DiningPhilsopher.model to javafx.graphics, javafx.fxml;
    opens no.os.DiningPhilsopher.gui to javafx.fxml, javafx.graphics;

    exports no.os.DiningPhilsopher.gui.controller;
    exports no.os.DiningPhilsopher.gui.window;
    exports no.os.DiningPhilsopher.model;
    exports no.os.DiningPhilsopher.gui;

}
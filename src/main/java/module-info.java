module dining {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.logging;

    opens no.stonedstonar.DiningPhilsopher.gui.controller to javafx.fxml, javafx.graphics;
    opens no.stonedstonar.DiningPhilsopher.gui.window to javafx.graphics, javafx.fxml;
    opens no.stonedstonar.DiningPhilsopher.gui to javafx.fxml, javafx.graphics;
    opens no.stonedstonar.DiningPhilsopher.model to javafx.graphics, javafx.fxml;

    exports no.stonedstonar.DiningPhilsopher.gui.controller;
    exports no.stonedstonar.DiningPhilsopher.gui.window;
    exports no.stonedstonar.DiningPhilsopher.model;
    exports no.stonedstonar.DiningPhilsopher.gui;

}
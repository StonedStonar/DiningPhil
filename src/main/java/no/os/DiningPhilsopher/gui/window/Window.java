package no.os.DiningPhilsopher.gui.window;

import javafx.scene.Scene;
import no.os.DiningPhilsopher.gui.controller.Controller;

/**
 * Represents the basic methods a window should have.
 * @version 0.1
 * @author Group 13
 */
public interface Window {

    /**
     * Gets the controller of the window.
     */
    Controller getController();

    /**
     * Gets the scene that the window has.
     * @return a scene that this window is loading and making GUI elements for.
     */
    Scene getScene();

    /**
     * Gets the name of the FXML file so the main app can load it.
     * @return a FXML file name that can be used to load the scene.
     */
    String getFXMLName();

    /**
     * Gets the title of the window so it can be displayed in the far left corner.
     * @return the title you want displayed in the far left of the window.
     */
    String getTitleName();

    /**
     * Sets the scene of the window.
     * @param scene the new scene you want to set the window to.
     */
    void setScene(Scene scene);
}
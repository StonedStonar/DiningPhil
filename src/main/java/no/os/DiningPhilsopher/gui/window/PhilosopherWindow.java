package no.os.DiningPhilsopher.gui.window;

import javafx.scene.Scene;
import no.os.DiningPhilsopher.gui.controller.Controller;
import no.os.DiningPhilsopher.gui.controller.PhilosopherController;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class PhilosopherWindow implements Window{

    private final String title;

    private final PhilosopherController controller;

    private Scene scene;

    private final String fxmlName;

    private static PhilosopherWindow philosopherWindow;

    /**
     * Makes an instance of the PhilosopherWindow class.
     */
    public PhilosopherWindow() {
        this.title = "Philosopher simulation";
        this.controller = new PhilosopherController();
        this.fxmlName = "diningWindow";
    }

    /**
     * Gets the philosopher window.
     * @return the philosopher window to get.
     */
    public static PhilosopherWindow getPhilosopherWindow(){
        if (philosopherWindow == null){
            synchronized (PhilosopherWindow.class){
                philosopherWindow = new PhilosopherWindow();
            }
        }
        return philosopherWindow;
    }

    /**
     * Checks if a string is of a valid format or not.
     *
     * @param stringToCheck the string you want to check.
     * @param errorPrefix   the error the exception should have if the string is invalid.
     */
    private void checkString(String stringToCheck, String errorPrefix) {
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()) {
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }

    /**
     * Checks if an object is null.
     *
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }

    @Override
    public Controller getController() {
        return controller;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public String getFXMLName() {
        return fxmlName;
    }

    @Override
    public String getTitleName() {
        return title;
    }

    @Override
    public void setScene(Scene scene) {
        checkIfObjectIsNull(scene, "scene");
        this.scene = scene;
    }
}

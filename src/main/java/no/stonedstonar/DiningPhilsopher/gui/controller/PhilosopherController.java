package no.stonedstonar.DiningPhilsopher.gui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import no.stonedstonar.DiningPhilsopher.model.Philosopher;
import no.stonedstonar.DiningPhilsopher.model.PhilosopherObserver;
import no.stonedstonar.DiningPhilsopher.model.State;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class PhilosopherController implements Controller, PhilosopherObserver {

    @FXML
    private HBox battleArena;

    @FXML
    private TextField amountField;

    @FXML
    private Button startButton;

    @FXML
    private Button abortButton;

    @FXML
    private VBox philosopherText;

    /**
     * Makes an instance of the PhilosopherController class.
     */
    public PhilosopherController() {

    }

    private void setButtonActions(){
        this.startButton.setOnAction(event -> {
            try{
                int amount = getAmount();

            }catch (NumberFormatException numberFormatException){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Amount");
                alert.setHeaderText("Amount cannot be a string");
                alert.setContentText("The amount cannot be a string and must only be a number.");
                alert.show();
            }
        });

        this.abortButton.setDisable(true);

        this.abortButton.setOnAction(event -> {

        });
    }

    /**
     * Gets the amount that is put into the textfield.
     * @return a number.
     * @throws NumberFormatException gets thrown if the textfield contains letters.
     */
    private int getAmount() throws NumberFormatException{
        int amount = 0;
        String text = amountField.textProperty().get();
        if (!text.isEmpty()){
            amount = Integer.parseInt(text);
        }
        return amount;
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
    public void updateContent() {

    }

    @Override
    public void emptyContent() {

    }

    @Override
    public void notifyObserver(Philosopher philosopher) {

    }

    @Override
    public void notifyObserverAboutStateChange(long id, State state) {
        Platform.runLater(() -> {
            //Todo: Make edit method and use it here.
        });
    }
}

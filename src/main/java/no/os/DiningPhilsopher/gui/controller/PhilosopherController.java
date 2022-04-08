package no.os.DiningPhilsopher.gui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import no.os.DiningPhilsopher.model.Philosopher;
import no.os.DiningPhilsopher.model.PhilosopherObserver;
import no.os.DiningPhilsopher.model.State;
import no.os.DiningPhilsopher.model.Table;


import java.util.ArrayList;
import java.util.List;

/**
 * Represents the controller for the philosophers.
 * @author Group 13
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

    private Table table;

    private List<VBox> philList;

    /**
     * Makes an instance of the PhilosopherController class.
     */
    public PhilosopherController() {
        philList = new ArrayList<>();
    }

    /**
     * Set all the buttons actions.
     */
    private void setButtonActions(){
        this.startButton.setOnAction(event -> {
            try{
                System.out.flush();
                int amount = getAmount();
                table = new Table(amount, 750);
                Philosopher.setConsole();
                Table.setConsole();
                displayTable();
                startButton.setDisable(true);
                abortButton.setDisable(false);
            }catch (IllegalArgumentException numberFormatException){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Amount");
                alert.setHeaderText("Amount cannot be a string");
                alert.setContentText("The amount cannot be a string and must only be a number.");
                alert.show();
            }
        });

        this.abortButton.setDisable(true);

        this.abortButton.setOnAction(event -> {
            table.stopSimulation();
            abortButton.setDisable(true);
            startButton.setDisable(false);
        });
    }

    /**
     * Makes the simulation table elements where philosophers are.
     */
    private void displayTable(){
        battleArena.getChildren().clear();
        List<Philosopher> philosopherList = table.getPhilosophers();
        for (Philosopher phil : philosopherList) {
            phil.addObserver(this);
            VBox vBox = new VBox();
            vBox.setId(Long.toString(phil.getPhilID()));
            Label label = new Label(phil.getName());
            Text text = new Text(phil.getState().toString());
            text.setStyle("-fx-background-color: yellow;");
            vBox.getChildren().add(label);
            vBox.getChildren().add(text);
            vBox.setPadding(new Insets(10, 10, 10, 10));
            philList.add(vBox);
            battleArena.getChildren().add(vBox);
        }
        table.startSimulation();
    }

    private void updatePhil(long id, State state){
        VBox vBox = (VBox) battleArena.lookup("#" + id);
        Text textState = (Text) vBox.getChildren().get(1);
        String status = "";
        switch (state){
            case DEAD -> status = "DEAD";
            case EATING -> status = "EATING";
            case THINKING -> status = "THINKING";
            case HUNGRY -> status = "HUNGRY";
        }
        textState.setText(status);
        if (State.EATING == state){
            textState.setStyle("-fx-background-color: yellow;");
        }else {
            textState.setStyle("-fx-background-color: white;");
        }
    }

    /**
     * Gets the amount that is put into the textfield.
     * @return a number.
     * @throws NumberFormatException gets thrown if the textfield contains letters.
     */
    private int getAmount() throws NumberFormatException{
        int amount = 0;
        String text = amountField.textProperty().get();
        checkString(text, "text");
        if (!text.isEmpty()){
            amount = Integer.parseInt(text);
        }
        return amount;
    }

    /**
     * Checks if a string is of a valid format or not.
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
        setButtonActions();
    }

    @Override
    public void emptyContent() {

    }

    @Override
    public void notifyObserver(Philosopher philosopher) {

    }

    @Override
    public void notifyObserverAboutStateChange(long id, State state) {
        Platform.runLater(() -> updatePhil(id, state));
    }
}

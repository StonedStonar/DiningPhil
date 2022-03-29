package no.stonedstonar.DiningPhilsopher.model;

import java.time.LocalTime;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class Message {

    private String message;

    private LocalTime localTime;

    private boolean error;

    /**
     * Makes an instance of the Message class.
     * @param message the message that should be displayed.
     * @param localTime the local time this message was sent.
     */
    public Message(String message, LocalTime localTime, boolean error) {
        checkString(message, "message");
        checkIfObjectIsNull(localTime, "local time");
        this.message = message;
        this.localTime = localTime;
        this.error = error;
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
}

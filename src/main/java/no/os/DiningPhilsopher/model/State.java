package no.os.DiningPhilsopher.model;

/**
 * Represents an enum that shows the hunger state of a person.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public enum State {

    /**
     * Is done when one is hungry.
     */
    HUNGRY,

    /**
     * Is done when one is in the act of thinking.
     */
    THINKING,

    /**
     * Is done when one is in the act of eating.
     */
    EATING,

    /**
     * You know what this state indicates
     */
    DEAD
}

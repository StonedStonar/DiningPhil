package no.stonedstonar.DiningPhilsopher.model;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public interface PhilosopherObserver {

    /**
     * Notifies the observer about the new state.
     * @param philosopher the philosopher object
     */
    void  notifyObserver(Philosopher philosopher);

    /**
     * Notifies an observer about a state change.
     * @param id the id of the philosopher.
     * @param state the new state.
     */
    void notifyObserverAboutStateChange(long id, State state);
}

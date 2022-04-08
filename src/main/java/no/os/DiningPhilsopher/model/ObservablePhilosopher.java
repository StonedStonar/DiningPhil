package no.os.DiningPhilsopher.model;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public interface ObservablePhilosopher {

    /**
     * Adds a new object as an observer.
     * @param philosopherObserver the new observer.
     */
    void addObserver(PhilosopherObserver philosopherObserver);

    /**
     * Removes a observer.
     * @param philosopherObserver the object to remove as a observer.
     */
    void removeObserver(PhilosopherObserver philosopherObserver);

    /**
     * Alerts all the observers.
     */
    void alertObservers();

    /**
     * Alerts the observers about a new state change.
     */
    void alertObserverAboutStateChange();
}

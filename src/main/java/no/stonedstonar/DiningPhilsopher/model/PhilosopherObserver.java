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
}

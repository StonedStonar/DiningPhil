package no.stonedstonar.DiningPhilsopher.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class Philosopher implements Runnable, ObservablePhilosopher{

    private long philID;

    private String name;

    private final int finalHunger;

    private int hunger;

    private State state;

    private Food food;

    private int amountOfTimesEating;

    private List<PhilosopherObserver> observers;

    private final Random random;

    private int delay;

    private Logger logger;

    /**
     * Makes an instance of the Philosopher class.
     * @param name the name of the philosopher.
     * @param amountOfFood the state of the hunger right now.
     * @param isRandom <code>true</code> if the starting state should be random.
     *                 <code>false</code> if the starting state should be set.
     * @param delay the amount of time in milliseconds that the delay should be. 1000 is one second.
     */
    public Philosopher(long philID,String name, int amountOfFood, boolean isRandom, int delay) {
        checkString(name, "name");
        this.observers = new LinkedList<>();
        this.philID = philID;
        this.name = name;
        this.random = new Random();
        if (isRandom){
            this.hunger = random.nextInt(amountOfFood/2, amountOfFood);
        }else {
            this.hunger = amountOfFood;
        }
        this.finalHunger = amountOfFood;
        setState(State.THINKING);
        this.amountOfTimesEating = 0;
        this.delay = delay;
        this.logger = Logger.getLogger(getClass().getName());
    }

    /**
     * Used to make the logger messages in the console visible.
     */
    public static void setConsole(){
        Logger logger = Logger.getLogger(Philosopher.class.getName());
        logger.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
    }

    /**
     * Receives food from a source.
     * @param food the food to eat.
     */
    public void receiveFood(Food food){
        checkIfObjectIsNull(food, "food");
        setState(State.EATING);
        this.food = food;
        logger.log(Level.FINE, "{0} got {1} {2}", new String[]{name, food.getFoodName(), LocalTime.now().toString()});
    }

    /**
     * Sets the state to a new value.
     * @param state the new state.
     */
    private void setState(State state){
        if (state != this.state && this.state != State.DEAD){
            this.state = state;
            alertObserverAboutStateChange();
        }

    }

    /**
     * Gets the philosopher's id.
     * @return the id.
     */
    public long getPhilID(){
        return philID;
    }

    /**
     * Represents a method that starts the philosopher. Switches between eating, thinking and hungry.
     */
    public void startPhilosopher(){
        while(!Thread.interrupted() && hunger > 0){
            switch (state){
                case HUNGRY -> hungry();
                case THINKING -> think();
                case EATING -> eat();
            }
            sleepAndLive();
        }
        if (Thread.interrupted()){
            logger.log(Level.INFO, "{0} aborted execution.", name);
        }else {
            dieOfHunger();
        }
    }

    /**
     * Makes the thread "sleep" and loose hunger. If the state is not set to "eating"
     */
    public void sleepAndLive(){
        if (state != State.EATING && !Thread.interrupted()){
            try {
                sleep();
                hunger -= 1;
                if (hunger <= finalHunger/2){
                    setState(State.HUNGRY);
                }
            } catch (InterruptedException e) {
                System.out.println("PEPE sleep failed.");
            }

        }
    }

    /**
     * Just makes the thread sleep.
     * @throws InterruptedException if the thread was interrupted.
     */
    private void sleep() throws InterruptedException{
        Thread.sleep(delay);

    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * Gets the hunger state of the philosopher.
     * @return the hunger state of the philosopher.
     */
    public State getState(){
        return state;
    }

    /**
     * The physical act of eating.
     */
    public void eat(){
        int amountOfFoodToEat = finalHunger - hunger;
        int remainingFood = food.getAmountOfFood();
        if (amountOfFoodToEat > remainingFood){
            amountOfFoodToEat = remainingFood;
        }
        food.removeAmountOfFood(amountOfFoodToEat);
        this.hunger += amountOfFoodToEat;
        logger.log(Level.FINE, "{0} is eating {1} amount {2} {3}", new String[]{name, food.getFoodName(), Integer.toString(amountOfFoodToEat),getTimeAsString() });
        try {
            sleep();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setState(State.THINKING);
        this.amountOfTimesEating += 1;
        food.setTaken(false);
        this.food = null;
    }

    /**
     * The physical act of thinking.
     */
    private void think(){
        logger.log(Level.FINE, "{0} is thinking about life and space. {1}", new String[]{name, getTimeAsString()});
    }

    /**
     * The physical act of asking for food.
     */
    private void hungry(){
        int priority = Thread.MIN_PRIORITY;
        if (hunger < finalHunger/2){
            priority = Thread.MAX_PRIORITY;
        }
        Thread.currentThread().setPriority(priority);
        alertObservers();
    }

    /**
     * The physical act of dying of hunger.
     */
    public void dieOfHunger(){
        logger.log(Level.INFO, "{0} has died of starvation. But ate {1} times. {2}", new String[]{name, Integer.toString(amountOfTimesEating), getTimeAsString()});
        setState(State.DEAD);
        observers.clear();
    }

    /**
     * Gets the current time as a string.
     * @return the time right now.
     */
    private String getTimeAsString(){
        return LocalTime.now().toString();
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
    public void run() {
        startPhilosopher();
    }

    @Override
    public void addObserver(PhilosopherObserver philosopherObserver) {
        checkIfObjectIsNull(philosopherObserver, "philosopher observer");
        this.observers.add(philosopherObserver);
    }

    @Override
    public void removeObserver(PhilosopherObserver philosopherObserver) {
        checkIfObjectIsNull(philosopherObserver, "philosopher observer");
        this.observers.remove(philosopherObserver);
    }

    @Override
    public void alertObservers() {
        synchronized (Philosopher.class){
            observers.forEach(obs -> {
                if (obs instanceof Table){
                    obs.notifyObserver(this);
                }
            });
            if (food == null){
                logger.log(Level.INFO, "{0} panicks since there is no food available. {1}", new String[]{name, getTimeAsString()});
            }
        }
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public void alertObserverAboutStateChange() {
        this.observers.forEach(observer -> {
            if (!(observer instanceof Table)){
                observer.notifyObserverAboutStateChange(philID, state);
            }
        });
    }
}

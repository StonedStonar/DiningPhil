package no.stonedstonar.DiningPhilsopher.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class Philosopher implements Runnable, ObservablePhilosopher{

    private static List<String> messageLog = new ArrayList<>();

    private long philID;

    private String name;

    private final int finalHunger;

    private int hunger;

    private State state;

    private Food food;

    private int amountOfTimesEating;

    private List<PhilosopherObserver> observers;

    private final Random random;

    private AtomicBoolean atomicBoolean;

    private int delay;

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
        observers = new LinkedList<>();
        this.philID = philID;
        this.name = name;
        random = new Random();
        if (isRandom){
            this.hunger = random.nextInt(amountOfFood/2, amountOfFood);
        }else {
            this.hunger = amountOfFood;
        }
        this.finalHunger = amountOfFood;
        setState(State.THINKING);
        amountOfTimesEating = 0;
        atomicBoolean = new AtomicBoolean();
        atomicBoolean.set(false);
        this.delay = delay;
    }

    /**
     * Adds a new message to the message list.
     * @param message the new message.
     */
    private static void addMessage(String message){
        synchronized (Philosopher.class){
            synchronized (messageLog){
                messageLog.add(message);
            }
        }
    }

    /**
     *
     * @return
     */
    public static List<String> getMessageLog(){
        return messageLog;
    }

    /**
     * Receives food from a source.
     * @param food the food to eat.
     */
    public void receiveFood(Food food){
        checkIfObjectIsNull(food, "food");
        setState(State.EATING);
        this.food = food;
        addMessage(name + " got " + food.getFoodName() + " " + LocalTime.now());
    }

    public void stop(){
        this.atomicBoolean.set(true);
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
        while(!atomicBoolean.get()){
            switch (state){
                case HUNGRY -> hungry();
                case THINKING -> think();
                case EATING -> eat();
                default -> sleepAndLive();
            }
        }
        if (atomicBoolean.get()){
            addMessage(name + " aborted.");
        }
    }

    /**
     * Makes the thread "sleep" and loose hunger. If the state is not set to "eating"
     */
    public void sleepAndLive(){
        if (state != State.EATING && !atomicBoolean.get()){
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
        addMessage(name + " is eating " + food.getFoodName() + " amount " + amountOfFoodToEat + " " + LocalTime.now());
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
        addMessage(name + " is thinking about life and space." + " " + LocalTime.now());
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
        System.err.println(name + " has died of starvation. But ate " + amountOfTimesEating + " times." + " " + LocalTime.now());
        setState(State.DEAD);
        observers.clear();
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
                addMessage(name + " panicks since there is no food available." + " " + LocalTime.now());
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

package no.stonedstonar.DiningPhilsopher.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class Philosopher implements Runnable, ObservablePhilosopher {

    private String name;

    private final int finalHunger;

    private int hunger;

    private State state;

    private int amountOfTimesEating;

    private List<PhilosopherObserver> observers;

    private Food food;

    private final Random random;

    /**
     * Makes an instance of the Philosopher class.
     * @param name the name of the philosopher.
     * @param amountOfFood the state of the hunger right now.
     * @param isRandom <code>true</code> if the starting state should be random.
     *                 <code>false</code> if the starting state should be set.
     */
    public Philosopher(String name, int amountOfFood, boolean isRandom) {
        checkString(name, "name");
        this.name = name;
        random = new Random();
        if (isRandom){
            this.hunger = random.nextInt(amountOfFood/2, amountOfFood);
        }else {
            this.hunger = 5;
        }
        this.finalHunger = amountOfFood;
        state = State.THINKING;
        amountOfTimesEating = 0;
        observers = new LinkedList<>();
    }

    /**
     * Receives food from the table.
     * @param food the food to receive.
     */
    public void receiveFood(Food food){
        checkIfObjectIsNull(food, "food");
        System.out.println(name + " got " + food.getFoodName() + " " + LocalTime.now());
        this.food = food;
        this.state = State.EATING;
    }

    /**
     * Represents a method that starts the philosopher. Switches between eating, thinking and hungry.
     */
    public void startPhilosopher(){
        while(hunger > 0){
            switch (state){
                case HUNGRY -> hungry();
                case THINKING -> think();
                case EATING -> eat();
            }
            sleepAndLive();
        }
        dieOfHunger();
    }

    /**
     * Makes the thread "sleep" and loose hunger. If the state is not set to "eating"
     */
    public void sleepAndLive(){
        if (state != State.EATING){
            try {
                Thread.sleep(500);
                hunger -= 1;
                if (hunger <= finalHunger/2){
                    state = State.HUNGRY;
                }
            } catch (InterruptedException e) {
                System.out.println("PEPE sleep failed.");
            }
        }
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
        System.out.println(name + " is eating " + food.getFoodName() + " amount " + amountOfFoodToEat + " " + LocalTime.now());
        this.state = State.THINKING;
        this.amountOfTimesEating += 1;
        food.setTaken(false);
        this.food = null;
    }

    /**
     * The physical act of thinking.
     */
    private void think(){
        System.out.println(name + " is thinking about life and space." + " " + LocalTime.now());
    }

    /**
     * The physical act of asking for food.
     */
    private void hungry(){
        alertObservers();
    }

    /**
     * The physical act of dying of hunger.
     */
    private void dieOfHunger(){
        System.err.println(name + " has died of starvation. \nBut ate " + amountOfTimesEating + " times." + " " + LocalTime.now());
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
        observers.forEach(obs -> obs.notifyObserver(this));
        if (food == null){
            System.out.println(name + " panicks since there is no food available." + " " + LocalTime.now());
        }
    }
}

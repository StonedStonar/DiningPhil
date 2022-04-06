package no.stonedstonar.DiningPhilsopher.model;

import javafx.scene.control.Tab;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class Table implements PhilosopherObserver {

    private List<Philosopher> philosophers;

    private List<Food> foods;

    private Map<LocalTime, Philosopher> deadPhilosopher;

    private ExecutorService executorService;

    private final Logger logger;

    public static void main(String[] args) {
        //Table.setConsole();
        Philosopher.setConsole();
        Table table = new Table(3, 50);
        table.startSimulation();
    }

    /**
     * Used to make the logger messages in the console visible.
     */
    public static void setConsole(){
        Logger logger = Logger.getLogger(Table.class.getName());
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
    }

    /**
     * Makes an empty table ready for GUI use.
     * @param amount the amount of extra philosophers than 3.
     * @param delay the amount of delay in milli seconds.
     */
    public Table(int amount, int delay){
        addNDummyPhilosophers(amount, delay);
        deadPhilosopher = new HashMap<>();
        this.logger = Logger.getLogger(getClass().getName());
    }

    /**
     * Gets a list of all the philosophers.
     * @return a list with the philosophers.
     */
    public List<Philosopher> getPhilosophers(){
        return philosophers;
    }

    /**
     * Adds N amount of dummy philosophers.
     * @param amountOfN the amount of extra Toms we need.
     * @param delay the amount of delay in milliseconds.
     */
    private void addNDummyPhilosophers(int amountOfN, int delay){
        int foodAmount = amountOfN * 15;
        philosophers = new LinkedList<>();
        foods = new LinkedList<>();
        foods.add(new Food(2000, "Rice"));
        foods.add(new Food(1000, "Apple"));
        this.philosophers.add(new Philosopher(1, "Bjarne", foodAmount, false, delay));
        this.philosophers.add(new Philosopher(2, "Terje", foodAmount, false, delay));
        this.philosophers.add(new Philosopher(3, "Burt", foodAmount, false, delay));
        long size = this.philosophers.size();
        for (int i = 1; i <= amountOfN; i++){
            philosophers.add(new Philosopher( size + i,"Tom " + i, foodAmount, false, delay));
        }
        executorService = Executors.newFixedThreadPool(this.philosophers.size());
    }

    /**
     * Starts the simulation.
     */
    public void startSimulation(){
        philosophers.forEach(philosopher -> {
            philosopher.addObserver(this);
            executorService.submit(philosopher);
        });
    }

    /**
     * Stops the simulation.
     */
    public void stopSimulation(){
        executorService.shutdown();
    }


    /**
     * Handles the philosophers asking for food.
     * @param philosopher the philosopher to handle.
     */
    public void handlePhilosopherAskingForFood(Philosopher philosopher){
        if (checkIfTableHasFood()){
            List<Philosopher> philosopherList = getPhilosophersOnTheSide(philosopher);
            if (!checkIfPhilosophersBesideIsEating(philosopherList)){
                serveFood(philosopher);
            }else {
                StringBuilder stringBuilder = new StringBuilder();
                philosopherList.stream().filter(philosopher1 -> philosopher1.getState() == State.EATING).forEach(philosopher1 -> {
                    if (!stringBuilder.isEmpty()){
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(philosopher1.getName());
                });
                stringBuilder.append(" beside ");
                stringBuilder.append(philosopher.getName());
                stringBuilder.append(" is eating.");
                String warning = stringBuilder.toString();
                logger.log(Level.WARNING, warning);
            }
        }else {
            if (philosophers.stream().allMatch(philosopher1 -> philosopher1.getState() == State.DEAD)){
                stopSimulation();
            }
        }
    }

    /**
     * Gets food that is available and serves it to the person.
     * @param philosopher the philosopher to feed.
     */
    private void serveFood(Philosopher philosopher){
        Optional<Food> opFood = foods.stream().filter(food -> !food.isTaken() && food.getAmountOfFood() > 0).findFirst();
        if (opFood.isPresent()){
            Food food = opFood.get();
            food.setTaken(true);
            philosopher.receiveFood(food);
        }
    }

    /**
     * Checks if one of the philosophers on the side is eating.
     * @param philosophers the list with the philosophers.
     * @return <code>true</code> if one of the philosophers beside them are eating.
     *         <code>false</code> if none of the two of the philosophers are eating.
     */
    public boolean checkIfPhilosophersBesideIsEating(List<Philosopher> philosophers){
        checkIfObjectIsNull(philosophers, "philosophers");
        Philosopher philosopher1 = philosophers.get(0);
        Philosopher philosopher2 = philosophers.get(1);
        return (philosopher1.getState() == State.EATING) || (philosopher2.getState() == State.EATING);
    }

    /**
     * Gets the philosophers that are beside a philosopher.
     * @param philosopher the philosopher that is in the middle.
     * @return a list with two people beside the input philosopher.
     */
    private List<Philosopher> getPhilosophersOnTheSide(Philosopher philosopher){
        int index = philosophers.indexOf(philosopher);
        int amountOfPhilosophers = philosophers.size();
        int nextPosition = (index + 1) % amountOfPhilosophers;
        int beforePosition = (index + amountOfPhilosophers - 1) % philosophers.size();
        List<Philosopher> philosophers2 = new LinkedList<>();
        philosophers2.add(philosophers.get(beforePosition));
        philosophers2.add(philosophers.get(nextPosition));

        return philosophers2;
    }

    /**
     * Checks if the table has food.
     * @return <code>true</code> if there is still food available.
     *         <code>false</code> if there is no food available.
     */
    private boolean checkIfTableHasFood(){
        return !foods.stream().allMatch(food -> food.getAmountOfFood() == 0);
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
    public void notifyObserver(Philosopher philosopher) {
        handlePhilosopherAskingForFood(philosopher);
    }

    @Override
    public void notifyObserverAboutStateChange(long id, State state) {
        if (state == State.DEAD){
            LocalTime localTime = LocalTime.now();
            int place = (int) id - 1;
            Philosopher philosopher = philosophers.get(place);
            deadPhilosopher.put(localTime, philosopher);
        }
    }
}

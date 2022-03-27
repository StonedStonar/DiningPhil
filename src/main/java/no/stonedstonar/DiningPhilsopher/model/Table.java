package no.stonedstonar.DiningPhilsopher.model;

import javafx.application.Platform;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class Table implements PhilosopherObserver {

    private List<Philosopher> philosophers;

    private List<Food> foods;

    private ExecutorService executorService;

    public static void main(String[] args) {
        List<Philosopher> philosophers = new LinkedList<>();
        List<Food> foods = new LinkedList<>();
        foods.add(new Food(400, "Rice"));
        foods.add(new Food(50, "Apple"));
        philosophers.add(new Philosopher(1, "Bjarne", 10, false));
        philosophers.add(new Philosopher(1, "Terje", 10, false));
        philosophers.add(new Philosopher(1, "Burt", 10, false));
        Table table = new Table(foods, philosophers);
        table.addNDummyPhilosophers(4);
        table.startSimulation();
    }

    /**
     * Makes an instance of the Table class.
     * @param foods the list of foods.
     * @param philosophers the list of philosophers.
     */
    public Table(List<Food> foods, List<Philosopher> philosophers) {
        checkIfObjectIsNull(foods, "foods");
        checkIfObjectIsNull(philosophers, "philosophers");
        this.foods = foods;
        this.philosophers = philosophers;
        executorService = Executors.newFixedThreadPool(philosophers.size());
    }

    /**
     * Adds N amount of dummy philosophers.
     * @param amountOfN the amount of extra Toms we need.
     */
    public void addNDummyPhilosophers(int amountOfN){
        int size = philosophers.size();
        for (int i = 0; i < 4; i++){
            philosophers.add(new Philosopher( size + i,"Tom " + i, 10, false));
        }
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
        this.executorService.shutdown();
    }


    /**
     * Handles the philosophers asking for food.
     * @param philosopher the philosopher to handle.
     */
    public synchronized void handlePhilosopherAskingForFood(Philosopher philosopher){
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
            System.err.println(stringBuilder.toString());
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
        synchronized (Table.class){
            handlePhilosopherAskingForFood(philosopher);
        }
    }

    @Override
    public void notifyObserverAboutStateChange(long id, State state) {

    }

}

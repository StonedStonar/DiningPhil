package no.stonedstonar.DiningPhilsopher.model;

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
        foods.add(new Food(100, "Rice"));
        foods.add(new Food(100, "Apple"));
        philosophers.add(new Philosopher("Bjarne", 10, false));
        philosophers.add(new Philosopher("Terje", 10, false));
        philosophers.add(new Philosopher("Burt", 10, false));
        Table table = new Table(foods, philosophers);
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
     * Stars the simulation.
     */
    public void startSimulation(){
        philosophers.forEach(philosopher -> {
            philosopher.addObserver(this);
            executorService.submit(philosopher);
        });
    }


    /**
     * Handles the philosophers asking for food.
     * @param philosopher the philosopher to handle.
     */
    public synchronized void handlePhilosopherAskingForFood(Philosopher philosopher){
        if (!checkIfPhilosophersBesideIsEating(philosopher)){
            serveFood(philosopher);
        }else {
            System.err.println("People beside " + philosopher.getName() + " is eating.");
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
     * @param philosopher the philosopher to check for.
     * @return <code>true</code> if one of the philosophers beside them are eating.
     *         <code>false</code> if none of the two of the philosophers are eating.
     */
    public boolean checkIfPhilosophersBesideIsEating(Philosopher philosopher){
        checkIfObjectIsNull(philosopher, "philosopher");
        int index = philosophers.indexOf(philosopher);
        int amountOfPhilosophers = philosophers.size();
        int nextPosition = (index + 1) % amountOfPhilosophers;
        int beforePosition = (index + amountOfPhilosophers - 1) % philosophers.size();
        Philosopher philosopher1 = philosophers.get(nextPosition);
        Philosopher philosopher2 = philosophers.get(beforePosition);
        return (philosopher1.getState() == State.EATING) || (philosopher2.getState() == State.EATING);
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
    public synchronized void notifyObserver(Philosopher philosopher) {
        handlePhilosopherAskingForFood(philosopher);
    }

}

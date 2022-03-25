package no.stonedstonar.DiningPhilsopher.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class TableTests {

    private Table table;

    private List<Food> foods;

    private Philosopher philosopher;

    private Philosopher philosopher2;

    @BeforeEach
    private void makeTable(){
        List<Philosopher> philosophers = new LinkedList<>();
        foods = new LinkedList<>();
        foods.add(new Food(100, "Rice"));
        foods.add(new Food(100, "Apple"));
        philosopher2 = new Philosopher("Bjarne", 10, false);
        this.philosopher = new Philosopher("Terje", 10, false);
        philosophers.add(philosopher);
        philosophers.add(this.philosopher2);
        philosophers.add(new Philosopher("Burt", 10, false));
        table = new Table(foods, philosophers);
    }

    /**
     * Tests if checkIfPhilosophersBesideIsEating works with invalid input.
     */
    @Test
    @DisplayName("Tests if checkIfPhilosophersBesideIsEating works with invalid input.")
    public void testIfCheckIfPhilosophersBesideIsEatingWorksWithInvalidInput(){
        try {
            table.checkIfPhilosophersBesideIsEating(null);
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if checkIfPhilosophersBesideIsEating works with valid input.
     */
    @Test
    @DisplayName("Tests if checkIfPhilosophersBesideIsEating works with valid input.")
    public void testIfCheckIfPhilosophersBesideIsEatingWorksWithValidInput(){
        try {
            philosopher2.receiveFood(foods.get(0));
            boolean valid = table.checkIfPhilosophersBesideIsEating(this.philosopher);
            if (valid){
                assertTrue(true);
            }else {
                fail("Expected to get true since there is someone eating.");
            }
        }catch (IllegalArgumentException exception){
            fail("Executed to get a true or false value since the input format is valid.");
        }
    }



}

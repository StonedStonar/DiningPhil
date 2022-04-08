package no.os.DiningPhilsopher.model;

/**
 * @author Group 15
 * @version 0.1
 */
public class Food {

    private int amountOfFood;

    private String foodName;

    private boolean taken;

    /**
     * Makes an instance of the Food class.
     */
    public Food(int amountOfFood, String foodName) {
        ifNumberIsAboveZero(amountOfFood, "amount of food");
        checkString(foodName, "food name");
        this.foodName = foodName;
        this.amountOfFood = amountOfFood;
        taken = false;
    }

    /**
     * Removes a certain amount of food from the bowl.
     * @param amountOfFood the amount of food in int.
     */
    public void removeAmountOfFood(int amountOfFood){
        ifNumberIsAboveZero(amountOfFood, "amount of food");
        this.amountOfFood = this.amountOfFood - amountOfFood;
    }

    /**
     * Gets the amount of food from the food object.
     * @return gets the amount of food.
     */
    public int getAmountOfFood(){
        return amountOfFood;
    }

    /**
     * Gets if the food is taken by another.
     * @return <code>true</code> if the food is being used by someone else.
     *         <code>false</code> if the food is not being used by someone else.
     */
    public boolean isTaken(){
        return taken;
    }

    /**
     * Sets if the food is in use of another
     * @param taken <code>true</code> if the food is getting eaten by another.
     *              <code>false</code> if the food is not getting eaten by another.
     */
    public void setTaken(boolean taken){
        this.taken = taken;
    }

    /**
     * Gets the name of the food.
     * @return the name of the food.
     */
    public String getFoodName(){
        return foodName;
    }

    /**
     * Checks if the input number is above zero.
     * @param number the number to check against.
     * @param prefix the prefix of the error.
     * @throws IllegalArgumentException when the number is not above zero.
     */
    private void ifNumberIsAboveZero(long number, String prefix){
        if(number <= 0){
            throw new IllegalArgumentException("The " + prefix + " must be larger than 0.");
        }
    }

    /**
     * Checks if a string is of a valid format or not.
     *
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
}

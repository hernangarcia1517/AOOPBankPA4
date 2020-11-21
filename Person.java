/**
 * @author Hernan Garcia
 * @version 1.0.0
 * @since September 28, 2020
 */
public abstract class Person {
    private String firstName;
    private String lastName;
    private String customerID;
    private boolean hasChecking;
    private boolean hasSavings;

    Person() {
        // Default
    }

    /**
     * This is the constructor of Person
     * 
     * @param firstName   this is the first name
     * @param lastName    this is the last name
     * @param customerID  this is the customer ID
     * @param hasChecking this is to check if they have a checking account
     * @param hasSavings  this is to check if they have a savings account
     */
    Person(String firstName, String lastName, String customerID, boolean hasChecking, boolean hasSavings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.customerID = customerID;
        this.hasChecking = hasChecking;
        this.hasSavings = hasSavings;
    }

    /**
     * This method is to get the full name of the person
     * 
     * @return the full name
     */
    public String getName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * This method is to get th customer ID of the person
     * 
     * @return the customer ID
     */
    public String getCustomerID() {
        return customerID;
    }

    /**
     * This is to get whether the person has a checking account
     * 
     * @return either true if they have a checking account or false if they don't
     */
    public boolean getHasChecking() {
        return hasChecking;
    }

    /**
     * This method is to get whether the person has a savings account
     * 
     * @return either true if they have a savings account or false if they don't
     */
    public boolean getHasSavings() {
        return hasSavings;
    }
}

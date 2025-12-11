import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import default_proj.*;
import default_proj.ui.Menu;

public class MenuTest {
    
    /*
     * Testing Menu.java (and JMenu.java as well) are particularly difficult
     *  because the file uses a lot of static, final values for in its own
     *  private methods for creating and displaying its GUI. The test methods
     *  in this class, for example, will primarily be focused on Menu.java's
     *  interaction with the classes in the default_proj.processor package.
     * 
     * This is what the menu options array of String arrays looks like in
     *   Menu.java:
     * 
     *  private static final String[][] MENU_OPTIONS =
     *  {
     *      {"Total Population", "none"},
     *      {"Parking Fines per Capita", "none"},
     *      {"Average Residence Market Value for Zip Code", "zip code"},
     *      {"Average Residence Livable Area for Zip Code", "zip code"},
     *      {"Residential Market Value per Capita for Zip Code", "zip code"},
     *      {"Average Residence Livable Area per Capita for Zip Code", "zip code"},
     *      {"Average Residence Value per Square Foot for Zip Code", "zip code"},
     *      {"Exit", "none"}
     *  };
     *  
     * To "instantiate" the only Menu object, call Menu.getMenu(boolean init)
     *  to get a returned Menu object with its GUI uninitialized
     * 
     * ***We'll use Main.getMenuForTesting(), since that initializes the
     *  DataReader object that the Processor class interacts with***
     */

    // Getting Menu.java's MENU_OPTIONS array of String arrays
    private static final String[][] MENU_OPTIONS = Menu.getMenuOptions();
    // Set the directory to get the data files for Menu testing
    private static final String DATA_DIR = "datafiles/";

    // Testing Menu.getResultsFromSearch():
    // Testing null input for the String option 
    @Test
    public void testNullOption() {
        // Create option and random input Strings
        String zipcode_input = "23467";
        String invalid_input = "-1";

        // Get Menu object
        Menu menu = Main.getMenuForTesting(DATA_DIR);

        // Get the results
        String first_result = menu.getResultsFromSearch(null, zipcode_input);
        String second_result = menu.getResultsFromSearch(null, invalid_input);

        // Make sure both results are null
        assertEquals(first_result, null);
        assertEquals(second_result, null);
    }

    // Testing Menu.getResultsFromSearch():
    // Testing empty input for the String option 
    @Test
    public void testEmptyOption() {
        // Create option and random input Strings
        String option = "";
        String zipcode_input = "00000";
        String invalid_input = "qAFrdgrnh";

        // Get Menu object
        Menu menu = Main.getMenuForTesting(DATA_DIR);

        // Get the results
        String first_result = menu.getResultsFromSearch(option, zipcode_input);
        String second_result = menu.getResultsFromSearch(option, invalid_input);

        // Make sure both results are not null
        assertNotEquals(first_result, null);
        assertNotEquals(second_result, null);

        // Make sure both results are empty Strings
        assertEquals(first_result.length(), 0);
        assertEquals(second_result.length(), 0);
    }

    // Testing Menu.getResultsFromSearch():
    // Testing null user input for the first two options, which don't require
    //  user input, so we should get String representations of integers back
    @Test
    public void testFirstTwoOptionsNullUserInput() {
        // Create option and random input Strings
        String option_one = MENU_OPTIONS[0][0];
        String option_two = MENU_OPTIONS[1][0];

        // Get Menu object
        Menu menu = Main.getMenuForTesting(DATA_DIR);

        // Get the results
        String first_result = menu.getResultsFromSearch(option_one, null);
        String second_result = menu.getResultsFromSearch(option_two, null);

        // Make sure both results are not null
        assertNotEquals(first_result, null);
        assertNotEquals(second_result, null);

        // Make sure both results are Strings of length greater than 1
        assertTrue(first_result.length() > 0);
        assertTrue(second_result.length() > 0);
    }

    // Testing Menu.getResultsFromSearch():
    // Testing null user input for the other options should return an empty
    //  results String
    @Test
    public void testOtherOptionsNullUserInput() {
        // Create option and random input Strings
        String option_three = MENU_OPTIONS[2][0];
        String option_four = MENU_OPTIONS[3][0];
        String option_five = MENU_OPTIONS[4][0];
        String option_six = MENU_OPTIONS[5][0];
        String option_seven = MENU_OPTIONS[6][0];

        // Get Menu object
        Menu menu = Main.getMenuForTesting(DATA_DIR);

        // Get the results
        String third_result = menu.getResultsFromSearch(option_three, null);
        String fourth_result = menu.getResultsFromSearch(option_four, null);
        String fifth_result = menu.getResultsFromSearch(option_five, null);
        String sixth_result = menu.getResultsFromSearch(option_six, null);
        String seventh_result = menu.getResultsFromSearch(option_seven, null);

        // Make sure all results are not null
        assertNotEquals(third_result, null);
        assertNotEquals(fourth_result, null);
        assertNotEquals(fifth_result, null);
        assertNotEquals(sixth_result, null);
        assertNotEquals(seventh_result, null);

        // Make sure all result Strings are empty
        assertEquals(third_result.length(), 0);
        assertEquals(fourth_result.length(), 0);
        assertEquals(fifth_result.length(), 0);
        assertEquals(sixth_result.length(), 0);
        assertEquals(seventh_result.length(), 0);
    }

    // Testing Menu.getResultsFromSearch():
    // Testing invalid integer input (a number < 0) for the other options
    //  should return an empty results String
    @Test
    public void testOtherOptionsInvalidIntegerInput() {
        // Create option and random input Strings
        String option_three = MENU_OPTIONS[2][0];
        String option_four = MENU_OPTIONS[3][0];
        String option_five = MENU_OPTIONS[4][0];
        String option_six = MENU_OPTIONS[5][0];
        String option_seven = MENU_OPTIONS[6][0];
        // Set user input to String representing an invalid integer
        String user_input = "-1"; 

        // Get Menu object
        Menu menu = Main.getMenuForTesting(DATA_DIR);

        // Get the results
        String third_result = menu.getResultsFromSearch(
                        option_three, user_input);
        String fourth_result = menu.getResultsFromSearch(
                        option_four, user_input);
        String fifth_result = menu.getResultsFromSearch(
                        option_five, user_input);
        String sixth_result = menu.getResultsFromSearch(
                        option_six, user_input);
        String seventh_result = menu.getResultsFromSearch(
                        option_seven, user_input);

        // Make sure all results are not null
        assertNotEquals(third_result, null);
        assertNotEquals(fourth_result, null);
        assertNotEquals(fifth_result, null);
        assertNotEquals(sixth_result, null);
        assertNotEquals(seventh_result, null);

        // Make sure all result Strings are empty
        assertEquals(third_result.length(), 0);
        assertEquals(fourth_result.length(), 0);
        assertEquals(fifth_result.length(), 0);
        assertEquals(sixth_result.length(), 0);
        assertEquals(seventh_result.length(), 0);
    }

    // Testing Menu.getResultsFromSearch():
    // Testing invalid zip code as user input for the other options
    //  should return an empty results String
    @Test
    public void testOtherOptionsInvalidZipCode() {
        // Create option and random input Strings
        String option_three = MENU_OPTIONS[2][0];
        String option_four = MENU_OPTIONS[3][0];
        String option_five = MENU_OPTIONS[4][0];
        String option_six = MENU_OPTIONS[5][0];
        String option_seven = MENU_OPTIONS[6][0];
        // Set user input to String representing an invalid zip code input
        String char_input = "greyjt";

        // Get Menu object
        Menu menu = Main.getMenuForTesting(DATA_DIR);

        // Get the results
        String third_result = menu.getResultsFromSearch(
                        option_three, char_input);
        String fourth_result = menu.getResultsFromSearch(
                        option_four, char_input);
        String fifth_result = menu.getResultsFromSearch(
                        option_five, char_input);
        String sixth_result = menu.getResultsFromSearch(
                        option_six, char_input);
        String seventh_result = menu.getResultsFromSearch(
                        option_seven, char_input);

        // Make sure all results are not null
        assertNotEquals(third_result, null);
        assertNotEquals(fourth_result, null);
        assertNotEquals(fifth_result, null);
        assertNotEquals(sixth_result, null);
        assertNotEquals(seventh_result, null);

        // Make sure all result Strings are empty
        assertEquals(third_result.length(), 0);
        assertEquals(fourth_result.length(), 0);
        assertEquals(fifth_result.length(), 0);
        assertEquals(sixth_result.length(), 0);
        assertEquals(seventh_result.length(), 0);
    }

    // Testing Menu.getResultsFromSearch():
    // Testing integers that are not technically in zip code format
    //  (such as a String with 4 numbers) as user input for the other
    //  options should return an empty results String
    @Test
    public void testOtherOptionsNotZipCode() {
        // Create option and random input Strings
        String option_three = MENU_OPTIONS[2][0];
        String option_four = MENU_OPTIONS[3][0];
        String option_five = MENU_OPTIONS[4][0];
        String option_six = MENU_OPTIONS[5][0];
        String option_seven = MENU_OPTIONS[6][0];
        // Set user input to String representing an non zip code int as input
        String user_input = "3006";

        // Get Menu object
        Menu menu = Main.getMenuForTesting(DATA_DIR);

        // Get the results
        String third_result = menu.getResultsFromSearch(
                        option_three, user_input);
        String fourth_result = menu.getResultsFromSearch(
                        option_four, user_input);
        String fifth_result = menu.getResultsFromSearch(
                        option_five, user_input);
        String sixth_result = menu.getResultsFromSearch(
                        option_six, user_input);
        String seventh_result = menu.getResultsFromSearch(
                        option_seven, user_input);

        // Make sure all results are not null
        assertNotEquals(third_result, null);
        assertNotEquals(fourth_result, null);
        assertNotEquals(fifth_result, null);
        assertNotEquals(sixth_result, null);
        assertNotEquals(seventh_result, null);

        // Make sure all result Strings are empty
        assertEquals(third_result.length(), 0);
        assertEquals(fourth_result.length(), 0);
        assertEquals(fifth_result.length(), 0);
        assertEquals(sixth_result.length(), 0);
        assertEquals(seventh_result.length(), 0);
    }
}

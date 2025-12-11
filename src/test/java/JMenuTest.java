import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.*;
import javax.swing.*;
import default_proj.*;
import default_proj.ui.JMenu;
import default_proj.ui.Menu;

public class JMenuTest {

    /*
     * The tests for JMenu.java will focus on testing the following method:
     *  
     *  getComponent(Component comp, String ... parameters)
     *      -> this returns a Component
     * 
     *  This method covers the majority of Menu.java's interaction with
     *   JMenu.java
     * 
     *  These are the valid attribute categories that are checked for
     *   during getComponent():
     * 
     *  private static final String[] COMPONENT_ATTRIBUTE_CATEGORIES =
     *  {
     *      "x", "y", "width", "height",
     *      "original x", "original y", "original width", "original height",
     *      "layout", "foreground color", "background color",
     *      "frame title", "label text", "button text", "area text",
     *      "text font", "text style", "text size",
     *      "edit", "label image", "button image",
     *      "opaque", "wrap", "visible", "resizable", "close op"
     *  };
     */

    // Testing JMenu.getComponent()
    // Testing that a null input for Component returns null
    @Test
    public void testNullComp() {
        // Get the results using random attributes set in
        Component result = JMenu.getComponent(null, "x=100", "y=100");

        // Make sure the result is null
        assertEquals(result, null);
    }

    // Testing JMenu.getComponent()
    // Testing that no provided String attributes with a provided Component
    //  returns the same Component
    //  We'll use a JLabel (a subclass of Component) to compare text values
    @Test
    public void testNoAttributesSameComponent() {
        // Set the test text
        String test_text = "test text";
        JLabel test_label = new JLabel(test_text);

        // Get the results using no attributes provided
        Component result = JMenu.getComponent(test_label);

        // Make sure the result is not null
        assertNotEquals(result, null);

        // Make sure the resulting JLabel (Component) text is the same as our
        //  test text
        assertTrue(test_text.equals(((JLabel) result).getText()));
    }

    // Testing JMenu.getComponent()
    // Testing that null as an input String attribute with a provided
    // Component returns the same Component
    //  We'll use a JLabel (a subclass of Component) to compare text values
    @Test
    public void testNullAttribute() {
        // Set the test text
        String test_text = "test text";
        JLabel test_label = new JLabel(test_text);

        // Get the results using a null attribute
        Component result = JMenu.getComponent(test_label, null);

        // Make sure the result is not null
        assertNotEquals(result, null);

        // Make sure the resulting JLabel (Component) text is the same as our
        //  test text
        assertTrue(test_text.equals(((JLabel) result).getText()));
    }

    // Testing JMenu.getComponent()
    // Testing that valid String attributes as input with a provided
    // Component returns the same Component
    //  We'll use a JLabel (a subclass of Component) to compare text values
    @Test
    public void testValidAttributesSameComponent() {
        // Set the test text
        String test_text = "test text";
        JLabel test_label = new JLabel(test_text);

        // Get the results using random attributes set in
        Component result = JMenu.getComponent(test_label, "x=100", "y=100",
                        "width=20");

        // Make sure the result is not null
        assertNotEquals(result, null);

        // Make sure the resulting JLabel (Component) text is the same as our
        //  test text
        assertTrue(test_text.equals(((JLabel) result).getText()));
    }

    // Testing JMenu.getComponent()
    // Testing that valid String attributes as input with a provided
    // Component returns an updated Component
    //  We'll use a JLabel (a subclass of Component) to compare text values
    @Test
    public void testValidAttributesUpdatedComponent() {
        // Set the test text
        String test_text = "test text";
        // Set a new text to test the updated JLabel (Component)
        String new_text = "new text";
        JLabel test_label = new JLabel(test_text);

        // Get the results using random attributes set in
        Component result = JMenu.getComponent(test_label, "x=100", "y=100",
                        "label text="+new_text);

        // Make sure the result is not null
        assertNotEquals(result, null);

        // Make sure the resulting JLabel (Component) text is not the same as
        //  our test text
        assertFalse(test_text.equals(((JLabel) result).getText()));
        // And then make sure the resulting text is the same as the new text
        assertTrue(new_text.equals(((JLabel) result).getText()));
    }

    // Testing JMenu.getComponent()
    // Testing that valid String attributes representing invalid integers
    // for repositioning or resizing a Component returns the same Component
    //  We'll use a JLabel (a subclass of Component) to compare text values
    @Test
    public void testInvalidAttributesSameComponent() {
        // Set the test text
        String test_text = "test text";
        JLabel test_label = new JLabel(test_text);

        // Get the results using random attributes set in
        Component result = JMenu.getComponent(test_label, "x=-1", "y=-1",
                        "width=-500");

        // Make sure the result is not null
        assertNotEquals(result, null);

        // Make sure the resulting JLabel (Component) text is the same as
        //  our test text
        assertTrue(test_text.equals(((JLabel) result).getText()));
        // Also check that the position and dimensions of the JLabel (Component)
        //  are valid (>= 0)
        assertTrue(result.getX() >= 0);
        assertTrue(result.getY() >= 0);
        assertTrue(result.getWidth() >= 0);
        assertTrue(result.getHeight() >= 0);
    }
}

package default_proj.ui;

import default_proj.processor.*;

import java.util.*;
import java.awt.*;
// For MouseListener
import java.awt.event.*;
import javax.swing.*;

public class Menu implements MouseListener {
    
    private static final String DATA_DIR = "data/";
    private static final String HEADER_IMAGE =
                    DATA_DIR + "openphillydata_title.jpg";
    private static final String BUTTON_PANEL_IMAGE =
                    DATA_DIR + "button_panel.jpg";
    private static final String DISPLAY_PANEL_IMAGE =
                    DATA_DIR + "paper_pen.jpg";
    private static final String BUTTON_IMAGE = DATA_DIR + "button.jpg";
    private static final String BUTTON_PRESSED_IMAGE =
                    DATA_DIR + "button_pressed.jpg";
    private static final String STICKY_NOTE_IMAGE =
                    DATA_DIR + "sticky_note.png";
    // Messages
    // Welcome screen
    private static final String WELCOME_MESSAGE =
                "Welcome to OpenPhillyData!\nSelect a button to begin " +
                "searching through parking violations data for Philadelphia!";
    // Prompt
    private static final String ZIPCODE_PROMPT = "\n\nInput a zip code:";
    // In case there is an invalid response
    private static final String INVALID_RESPONSE =
                    "\n\n(Invalid response, try again!)";

    // The menu selections
    private static final String[][] MENU_OPTIONS =
    {
        {"Total Population", "none"},
        {"Parking Fines per Capita", "none"},
        {"Average Residence Market Value for Zip Code", "zip code"},
        {"Average Residence Livable Area for Zip Code", "zip code"},
        {"Residential Market Value per Capita for Zip Code", "zip code"},
        {"Average Residence Livable Area per Capita for Zip Code", "zip code"},
        {"Average Residence Value per Square Foot for Zip Code", "zip code"},
        {"Exit", "none"}
    };
    // The variable names
    private static final String TITLE = "OpenPhillyData";
    private static final String WINDOW_NAME = "window";
    private static final String HEADER_NAME = "header";
    private static final String BUTTON_PANEL_NAME = "button panel";
    private static final String DISPLAY_PANEL_NAME = "display panel";
    private static final String DISPLAY_TEXT_PANEL_NAME = "display text panel";
    private static final String DISPLAY_TEXT_NAME = "display text";
    private static final String DISPLAY_TEXT_SCROLL_NAME = "display text scroll";
    private static final String BUTTON_NAME = "button";
    private static final String STICKY_NOTE_NAME = "sticky_note";
    // For differentiating between JLabel and JTextArea later on
    private static final String JLABEL_COLOR = "yellow";
    private static final String JTEXTAREA_COLOR = "red";

    // The Menu (great movie btw)
    private static Menu menu;
    // The map of Component names to the Components themselves
    private Map<String, Component> names_comps;
    // The map of menu option Strings to expected input types
    private Map<String, String> option_inputs;
    // The map of all search results --> primarily for memoization
    private Map<String, String> search_results;

    private Menu(boolean perform_init) {
        names_comps = new HashMap<>();
        option_inputs = new HashMap<>();
        search_results = new HashMap<>();
        if (perform_init)
            init();
    }

    public static Menu getMenu(boolean perform_init) {
        // First check if the menu wasn't instantiated yet
        if (menu == null)
            menu = new Menu(perform_init);
        return menu;
    }

    // =============== Methods to help with testing this class ================
    
    public static String[][] getMenuOptions() {
        return MENU_OPTIONS;
    }

    public String getResultsFromSearch(String option, String input) {
        return getSearchResults(option, input);
    }
    // ========================================================================

    private void init() {
        // The main window
        JFrame window = (JFrame) JMenu.getComponent(new JFrame(),
                        "x=0", "y=0", "width=900", "height=900",
                        "layout=null", "background color=light gray",
                        "text font=dialog", "text style=plain",
                        "text size=24", "frame title="+TITLE,
                        "resizable=false", "close op=default");
        JLabel header_label = (JLabel) JMenu.getComponent(new JLabel(),
                        "x=0", "y=0", "width=900", "height=150",
                        "background color=green",
                        "label image="+HEADER_IMAGE);
        JLabel button_label = (JLabel) JMenu.getComponent(new JLabel(),
                        "x=0", "y=150", "width=300", "height=750",
                        "layout=grid bag",
                        "label image="+BUTTON_PANEL_IMAGE);
        JLabel display_label = (JLabel) JMenu.getComponent(new JLabel(),
                        "x=300", "y=150", "width=600", "height=750",
                        "layout=null",
                        "label image="+DISPLAY_PANEL_IMAGE);
        JPanel display_text_panel = (JPanel) JMenu.getComponent(
                        new JPanel(),
                        "x=100", "y=185",
                        "width=350", "height=375",
                        "layout=null",
                        "opaque=false");
        JTextArea display_text_title = (JTextArea) JMenu.getComponent(
                        new JTextArea(),
                        "x=0", "y=0",
                        "width=350", "height=150",
                        "text font=serif", "text style=bold",
                        "text size=22", "area text="+WELCOME_MESSAGE,
                        "opaque=false", "wrap=true", "edit=false");
        JTextArea display_text = (JTextArea) JMenu.getComponent(
                        new JTextArea(),
                        "x=25", "y=25",
                        "width=300", "height=225",
                        "text font=serif", "text style=plain",
                        "text size=18",
                        "opaque=false", "wrap=true", "edit=false");
        JScrollPane display_text_scroll = (JScrollPane) JMenu.getComponent(
                        new JScrollPane(display_text),
                        "x=0", "y=100",
                        "width=350", "height=275",
                        "opaque=false", "visible=false");
        // Attach the header, button panel, and display panel to the window
        JMenu.addComponent(window, header_label, null);
        JMenu.addComponent(window, button_label, null);
        JMenu.addComponent(window, display_label, null);
        // Attach the display text panel to the display panel
        JMenu.addComponent(display_label, display_text_panel, null);
        // Attach the display text title and scroll pane to the display text panel
        JMenu.addComponent(display_text_panel, display_text_title, null);
        JMenu.addComponent(display_text_panel, display_text_scroll, null);
        names_comps.put(WINDOW_NAME, window);
        names_comps.put(HEADER_NAME, header_label);
        names_comps.put(BUTTON_PANEL_NAME, button_label);
        names_comps.put(DISPLAY_PANEL_NAME, display_label);
        names_comps.put(DISPLAY_TEXT_PANEL_NAME, display_text_panel);
        names_comps.put(DISPLAY_TEXT_NAME, display_text);
        names_comps.put(DISPLAY_TEXT_SCROLL_NAME, display_text_scroll);
        // Lastly, set the options
        setOptions();
        update();
    }

    protected void update() {
        if (names_comps.containsKey(WINDOW_NAME))
            ((JFrame) names_comps.get(WINDOW_NAME)).setVisible(true);
    }

    private void setOptions() {
        String option_text;
        String input_type;
        JLabel button_label = (JLabel) names_comps.get(BUTTON_PANEL_NAME);
        // Get button and text sizes and gridbag constraints
        int button_width = button_label.getWidth();
        int button_height = button_label.getHeight() / MENU_OPTIONS.length;
        int text_width = (int) (.6 * button_width);
        int text_height = (int) (.6 * button_height);
        // Set the grid bag constraints
        GridBagConstraints panel_button_gbc = new GridBagConstraints();
        panel_button_gbc.weightx = 1;
        // Iterate through the menu options
        for (int i = 0; i < MENU_OPTIONS.length; i++) {
            // System.out.println("\nPrinting option #" + ++option_count + "!");
            option_text = MENU_OPTIONS[i][0].strip();
            input_type = MENU_OPTIONS[i][1].strip().toLowerCase();
            // Make the button (JButton) and button text (JTextArea)
            JLabel button = (JLabel) JMenu.getComponent(new JLabel(),
                            "width="+String.valueOf((int) (.8 * button_width)),
                            "height="+String.valueOf((int) (.8 * button_height)),
                            "layout=grid bag", // Use grid bag layout here
                            "label image="+BUTTON_IMAGE,
                            "opaque=false",
                            "background color="+JLABEL_COLOR);
            JTextArea button_text = (JTextArea) JMenu.getComponent(
                            new JTextArea(),
                            "width="+String.valueOf(text_width),
                            "height="+String.valueOf(text_height),
                            "text font=serif", "text style=bold",
                            "text size=14", "area text="+option_text,
                            "opaque=false", "wrap=true", "edit=false",
                            "background color="+JTEXTAREA_COLOR);
            // Add the button text to the button label
            JMenu.addComponent(button, button_text, null);
            button.addMouseListener(this);
            button_text.addMouseListener(this);
            // Add the button label to the button panel
            JMenu.addComponent(button_label, button, panel_button_gbc,
                // button label width, button label height, button x, button y
                            "0", String.valueOf(i), "inset",
                // container width and height
                            String.valueOf(button_width),
                            String.valueOf(button_height));
            // Update the maps
            names_comps.put(BUTTON_NAME+String.valueOf(i), button);
            names_comps.put(option_text, button_text);
            option_inputs.put(option_text, input_type);
        }
    }

    private void displaySearchResults(String option, String input) {
        String results_name = option;
        if (input.length() > 0)
            results_name += ", " + input;
        int searches_size = search_results.size();
        boolean added_sticky_note = false;
        // First check if the search results haven't been set yet
        if (!search_results.containsKey(results_name)) {
            search_results.put(results_name, getSearchResults(option, input));
            searches_size++;
            // Only 6 sticky notes are displayed at a time
            if (searches_size < 7) {
                // Create a new sticky note
                //  ***NOTE*** The display panel is 600 x 750
                //      and each sticky note is 152 x 152
                int x = 25 + 177 * ((searches_size - 1) % 3);
                int y = 25 + 548 * ((searches_size - 1) / 3);
                JLabel sticky_note = (JLabel) JMenu.getComponent(new JLabel(),
                            "x="+String.valueOf(x), "y="+String.valueOf(y),
                            "width=152", "height=152",
                            "layout=grid bag", // Use grid bag layout here
                            "label image="+STICKY_NOTE_IMAGE,
                            "opaque=true",
                            "background color="+JLABEL_COLOR);
                JTextArea note_text = (JTextArea) JMenu.getComponent(
                            new JTextArea(),
                            "width=121", "height=121",
                            "text font=serif", "text style=bold",
                            "text size=11", "area text="+results_name,
                            "opaque=false", "wrap=true", "edit=false",
                            "background color="+JTEXTAREA_COLOR);
                // Add the button text to the button label
                JMenu.addComponent(sticky_note, note_text, null);
                sticky_note.addMouseListener(this);
                note_text.addMouseListener(this);
                // Add the button label to the button panel
                JMenu.addComponent(
                    (Container) names_comps.get(DISPLAY_PANEL_NAME),
                                sticky_note, null);
                // Update the maps
                names_comps.put(
                    STICKY_NOTE_NAME+String.valueOf(searches_size),
                    sticky_note);
                names_comps.put(results_name, note_text);
                added_sticky_note = true;
            }
        }
        // Update a sticky note if we didn't add one already and we have gone
        //  through at least 7 unique search results
        if (!added_sticky_note && search_results.size() > 6) {
            // Rotate sticky notes if the current search is not on them
            boolean shift_sticky_notes = true;
            // The sticky note numbers are 1-indexed
            for (int i = 1; i < 7; i++) {
                if (results_name.equals(((JTextArea) ((JLabel) names_comps.get(
                                STICKY_NOTE_NAME+String.valueOf(i)))
                                .getComponents()[0]).getText()))
                    shift_sticky_notes = false;
            }
            // Shift stick notes if applicable
            if (shift_sticky_notes) {
                for (int i = 5; i > 0; i--) {
                    // Shift the sticky note texts downward
                    ((JTextArea) ((JLabel) names_comps.get(
                        STICKY_NOTE_NAME+String.valueOf(i + 1)))
                        .getComponents()[0]).setText(
                                ((JTextArea) ((JLabel) names_comps.get(
                                    STICKY_NOTE_NAME+String.valueOf(i)))
                                    .getComponents()[0]).getText());
                }
                // Finally add the new text for the first sticky note
                ((JTextArea) ((JLabel) names_comps.get(
                        STICKY_NOTE_NAME+String.valueOf(1)))
                        .getComponents()[0]).setText(results_name);
            }
        }
        // Display the search results
        displaySearchResults(results_name);
        // Update the frame's display
        update();
    }

    protected String getSearchResults(String option, String input) {
        String results_text = "";
        // If the specified option is null, return null
        if (option == null)
            return null;
        // Else, if the specified option is empty, return the empty results
        if (option.length() == 0)
            return results_text;
        // Go through the options and get the relevant search results
        // For total population
        if (option.equals(MENU_OPTIONS[0][0]))
            results_text = String.valueOf(Processor.getTotalPopulation());
        // For fines per capita
        else if (option.equals(MENU_OPTIONS[1][0]))
            results_text = String.valueOf(Processor.getFinesPerCapita());
        
        // Else, check if the user input is 5 chars long (representing a
        //  correct zip code)
        else if (input != null && input.length() == 5) {
            // Make sure parsing of user input is successful
            try {
                int zip_code = Integer.parseInt(input);
                // For average residence market value
                if (option.equals(MENU_OPTIONS[2][0]))
                    results_text = String.valueOf(Processor
                            .getAverageMarketValue(zip_code));
                // For average residence liveable area
                if (option.equals(MENU_OPTIONS[3][0]))
                    results_text = String.valueOf(Processor
                            .getAverageLivableArea(zip_code));
                // For residential market value per capita
                if (option.equals(MENU_OPTIONS[4][0]))
                    results_text = String.valueOf(Processor
                            .getMarketValuePerCapita(zip_code));
                // For residential livable area per capita
                if (option.equals(MENU_OPTIONS[5][0]))
                    results_text = String.valueOf(Processor
                            .getLivableAreaPerCapita(zip_code));
                // For residential average value per square foot
                if (option.equals(MENU_OPTIONS[6][0]))
                    results_text = String.valueOf(Processor
                            .getAverageValuePerSqFt(zip_code));
                // For residential average value per square foot
                if (option.equals(MENU_OPTIONS[7][0]))
                    results_text = String.valueOf(Processor
                            .getAverageValuePerSqFt(zip_code));
            } catch (NumberFormatException e) {
                // Just return the empty results
            }
        }
        return results_text;
    }

    private void promptSelection(String option) {
        JFrame frame = (JFrame) names_comps.get(WINDOW_NAME);
        // Check if Exit was selected
        if (option.equals("Exit")) {
            //  Dispose the frame and close the program
            frame.dispose();
            System.exit(0);
        }
        // First get prompt message
        String prompt = "";
        // First check that the option exists
        if (option_inputs.containsKey(option)) {
            // For zip code prompt
            if (option_inputs.get(option).equals("zip code"))
                prompt = ZIPCODE_PROMPT;
            // Check if the input type isn't none
            if (!option_inputs.get(option).equals("none")) {
                // Then loop looking for valid input
                String response = "";
                String invalid_response = "";
                while (!checkResponse(option, response)) {
                    // Use a statically created dialog box here
                    response = (String) JOptionPane.showInputDialog(
                        frame, option +
                        invalid_response + prompt, "User Input",
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (response == null)
                        break;
                    // Set invalid response
                    invalid_response = INVALID_RESPONSE;
                }
                // Finally, get and display the search results
                if (response != null)
                    displaySearchResults(option, response);
            // Else, the there is no input required
            } else
                displaySearchResults(option, "");
        }
    }

    private boolean checkResponse(String option, String input) {
        // Make sure the prompt wasn't cancelled
        if (input == null)
            return false;
        // If there was not input, return false
        if (input.length() == 0)
            return false;
        // First check that the option exists
        if (option_inputs.containsKey(option)) {
            // Check if the option input type is "zip code"
            if (option_inputs.get(option).equals("zip code")) {
                // Zip codes should have 5 String digits
                if (input.length() != 5)
                    return false;
                // Else, try to parse the zip code and check if within range
                try {
                    int num = Integer.parseInt(input);
                    if (num < 0 || num > 99999)
                        return false;
                // Return false if the integer couldn't be parsed
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        // The option's input type doesn't exist or is "none"
        return true;
    }

    private void displaySearchResults(String search) {
        // Get the display text panel, display text title, and display text
        JPanel display_text_panel = (JPanel) names_comps.get(
                        DISPLAY_TEXT_PANEL_NAME);
        JTextArea display_text_title = (JTextArea) display_text_panel
                        .getComponents()[0];
        // Make sure the display text title is the right size
        //  and that the scroll pane is visible
        names_comps.get(DISPLAY_TEXT_SCROLL_NAME).setVisible(true);
        JMenu.resize(display_text_title, "350", "100");
        // The display text is attached to the display text scroll panel
        JTextArea display_text = (JTextArea) (names_comps.get(
                        DISPLAY_TEXT_NAME));
        // Update the display text to show the search results
        String title = "Results for \"" + search + "\":";
        String results = search_results.get(search);
        JMenu.setAreaText(display_text_title, title);
        JMenu.setAreaText(display_text, results);
        // Lastly, update the window display
        update();
    }

    // ============= Methods for MouseListener implementation =============

    public void mousePressed(MouseEvent e) {
        // We want the left mouse button to be pressed
        if (e.getButton() == MouseEvent.BUTTON1) {
            JLabel label;
            String text = "";
            Component comp = e.getComponent();
            // Check to see if the component is a JLabel
            if (((JComponent) comp).getBackground() ==
                            JMenu.getColor(JLABEL_COLOR)) {
                label = (JLabel) comp;
                text = ((JTextArea) label.getComponents()[0]).getText();
            } else {
                label = (JLabel) comp.getParent();
                text = ((JTextArea) comp).getText();
            }
            // System.out.println("\nPressed \"" + text + "\"!");
            // If width != 152, it was one of the button's pressed
            if (label.getWidth() != 152) {
                // Set label image to look like button press
                JMenu.setImage(label, BUTTON_PRESSED_IMAGE);
                update();
                // Prompt menu selection
                promptSelection(text);
                // Afterwards, restore the button's original image
                JMenu.setImage(label, BUTTON_IMAGE);
                // Finally, update the window
                update();
            // Else, it was a sticky note interacted with
            } else {
                displaySearchResults(text);
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}
}
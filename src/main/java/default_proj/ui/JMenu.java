package default_proj.ui;

import java.util.*;
import java.awt.*;

import javax.swing.*;

public class JMenu {

    private static final String NFE_ERROR_MSG = "Invalid integer provided!";
    private static final String[] COMPONENT_ATTRIBUTE_CATEGORIES =
    {
        "x", "y", "width", "height",
        "original x", "original y", "original width", "original height",
        "layout", "foreground color", "background color",
        "frame title", "label text", "button text", "area text",
        "text font", "text style", "text size",
        "edit", "label image", "button image",
        "opaque", "wrap", "visible", "resizable", "close op"
    };

    private static String[] COLORS = {"black", "blue", "cyan", "dark gray",
                    "gray", "green", "light gray", "magenta", "orange",
                    "pink", "red", "white"
    };

    // Made public for testing
    public static Component getComponent(Component comp,
                    String ... parameters) {
        // Create the attributes map
        setAttributes(comp, parameters);
        return comp;
    }

    protected static Color getRandomColor() {
        return getColor(COLORS[new Random().nextInt(COLORS.length)]);
    }

    // Made public for testing
    public static Map<String, String> getAttrMap(String[] parameters) {
        Map<String, String> attributes_map = new HashMap<>();
        for (String p : parameters) {
            // Make sure the parameter is not null
            if (p != null) {
                // System.out.println("\nAttempting to set " + p + "...");
                String[] tokens = p.split("=");
                String key = "";
                if (tokens.length == 2) {
                    key = tokens[0].strip().toLowerCase();
                    if (isValidAtrCat(key)) {
                        String value = tokens[1].strip();
                        if (key != null && value != null) {
                            attributes_map.put(key, value);
                        }
                    }
                }
            }
        }
        // Return the map of attributes
        return attributes_map;
    }

    // Take a String pair and insert the relevant entry in the attribute map
    protected static void setAttributes(Component component,
                    String[] parameters) {
        
        // Skip if the component or the parameters are null,
        //  or the parameters length = 0
        if (component != null && parameters != null &&
                        parameters.length > 0) {
            // First get the map of attributes
            Map<String, String> attributes_map = getAttrMap(parameters);
            // Then set the relevant attributes

            // For setting the position
            if (attributes_map.containsKey("x") &&
                            attributes_map.containsKey("y"))
                reposition(component, attributes_map.get("x"),
                                attributes_map.get("y"));

            // For setting the size
            if (attributes_map.containsKey("width") &&
                            attributes_map.containsKey("height"))
                resize(component, attributes_map.get("width"),
                            attributes_map.get("height"));

            // For setting the LayoutManager
            if (attributes_map.containsKey("layout"))
                setLayout((Container) component,
                                attributes_map.get("layout"));

            // For the foreground color
            if (attributes_map.containsKey("foreground color"))
                setColorFg(component, attributes_map.get("foreground color"));

            // For the background color
            if (attributes_map.containsKey("background color"))
                setColorBg(component, attributes_map.get("background color"));

            // For setting the frame title
            if (attributes_map.containsKey("frame title"))
                setFrameTitle((JFrame) component,
                                attributes_map.get("frame title"));

            // For setting the label text
            if (attributes_map.containsKey("label text"))
                setLabelText((JLabel) component, attributes_map.get("label text"));

            // For setting the button text
            if (attributes_map.containsKey("button text"))
                setButtonText((JButton) component,
                                attributes_map.get("button text"));

            // For setting the area text
            if (attributes_map.containsKey("area text"))
                setAreaText((JTextArea) component,
                                attributes_map.get("area text"));

            // For setting the font
            if (attributes_map.containsKey("text font") &&
                            attributes_map.containsKey("text style") &&
                            attributes_map.containsKey("text size"))
                setFont(component, attributes_map.get("text font"),
                                attributes_map.get("text style"),
                                attributes_map.get("text size"));

            // For setting editability
            if (attributes_map.containsKey("edit"))
                setEditable((JTextArea) component, attributes_map.get("edit"));

            // For setting the image
            if (attributes_map.containsKey("label image"))
                setImage((JLabel) component, attributes_map.get("label image"));

            // For setting the image
            if (attributes_map.containsKey("button image"))
                setImage((JButton) component, attributes_map.get("button image"));

            // For setting opacity
            if (attributes_map.containsKey("opaque"))
                setOpaque((JComponent) component, attributes_map.get("opaque"));

            // For setting text wrapping
            if (attributes_map.containsKey("wrap"))
                setTextWrapping((JTextArea) component, attributes_map.get("wrap"));

            // For setting visibility
            if (attributes_map.containsKey("visible"))
                setVisible(component, attributes_map.get("visible"));

            // For setting resizability
            if (attributes_map.containsKey("resizable"))
                setResizable((JFrame) component, attributes_map.get("resizable"));

            // For setting close operations
            if (attributes_map.containsKey("close op"))
                setCloseOp((JFrame) component, attributes_map.get("close op"));
        }
    }

    // This is the cleaner way to add Components to Containers since we always
    //  perform checks on the Containers here
    protected static void addComponent(Container container, Component component,
                    GridBagConstraints gbc, String ... parameters) {
        // First make sure that the Container and Component are not null
        if (container != null && component != null) {
            // Check for Grid Bag Constraints
            if (gbc != null && parameters.length > 2) {
                gbc.gridx = Integer.parseInt(parameters[0]);
                gbc.gridy = Integer.parseInt(parameters[1]);
                if (parameters[2].equals("inset")) {
                    int width_spacing = (Integer.parseInt(parameters[3]) -
                                    component.getWidth()) / 4;
                    int height_spacing = (Integer.parseInt(parameters[4]) -
                                    component.getHeight()) / 4;
                    // Use container and component sizes to set Inset
                    gbc.insets = new Insets(
                        height_spacing, // top
                        width_spacing, // left
                        height_spacing, // bottom
                        width_spacing); // right
                } else
                    gbc.fill = getConstraint(parameters[2]);
                // Finally add the component
                container.add(component, gbc);
            // There's another layout
            } else if (parameters.length > 0) {
                if (parameters[0].equals("border")) {
                    // By default set the position to center
                    String position = getBorderPos("center");
                    if (parameters.length > 1)
                        position = getBorderPos(parameters[1]);
                    // Finally add the component
                    container.add(component, position);
                }
            } else
                container.add(component);
        }
    }

    // Reposition a Component
    protected static void reposition(Component component,
                    String x_str, String y_str) {
        try {
            int x = Integer.parseInt(x_str);
            int y = Integer.parseInt(y_str);
            if (x >= 0 && y >= 0)
                component.setBounds(x, y, component.getWidth(), component.getHeight());
        } catch (NumberFormatException e) {
            // System.out.println("\nReposition error: " + NFE_ERROR_MSG);
        }
    }

    // Resize a Component
    protected static void resize(Component component,
                    String w_str, String h_str) {
        
        try {
            int width = Integer.parseInt(w_str);
            int height = Integer.parseInt(h_str);
            if (width >= 0 && height >= 0)
                component.setBounds(component.getX(), component.getY(), width, height);
            // component.setPreferredSize(new Dimension(width, height));
        } catch (NumberFormatException e) {
            // System.out.println("\nResize error: " + NFE_ERROR_MSG);
        }
    }

    // Set a Component foreground color
    protected static void setColorFg(Component component, String color) {
        component.setForeground(getColor(color));
    }

    // Set a Component background color
    protected static void setColorBg(Component component, String color) {
        component.setBackground(getColor(color));
    }

    // Set a JFrame title
    protected static void setFrameTitle(JFrame fr, String title) {
        fr.setTitle(title);
    }

    // Set a JLabel text
    protected static void setLabelText(JLabel jl, String text) {
        jl.setHorizontalTextPosition(SwingConstants.CENTER);
        jl.setText(text);
    }

    // Set a JButton text
    protected static void setButtonText(JButton jb, String text) {
        jb.setHorizontalTextPosition(SwingConstants.CENTER);
        jb.setText(text);
    }

    // Set a JTextArea text
    protected static void setAreaText(JTextArea jta, String text) {
        jta.setText(text);
    }

    // Set a Component font
    protected static void setFont(Component component,
                    String fn, String fs, String sz) {
        component.setFont(getFont(fn, fs, Integer.parseInt(sz)));
    }

    // Set a JTextArea text editability
    protected static void setEditable(JTextArea jta, String edit) {
        edit = edit.toLowerCase();
        if (edit.equals("true")) {
            jta.setEditable(true);
            jta.setFocusable(true);
        // Else, make the component transparent
        } else {
            jta.setEditable(false);
            jta.setFocusable(false);
        }
    }

    // Set a JLabel image
    protected static void setImage(JLabel jl, String image_filename) {
        if (image_filename.equals("none"))
            image_filename = null;
        jl.setIcon(new ImageIcon(image_filename));
    }

    // Set a JLabel image
    protected static void setImage(JButton jb, String image_filename) {
        if (image_filename.equals("none"))
            image_filename = null;
        jb.setIcon(new ImageIcon(image_filename));
    }

    // Set a Container layout
    protected static void setLayout(Container container, String layout) {
        container.setLayout(getLayout(layout));
    }

    // Set a Component wrapping
    protected static void setTextWrapping(JTextArea jta, String wrap) {
        wrap = wrap.toLowerCase();
        if (wrap.equals("true")) {
            jta.setLineWrap(true);
            jta.setWrapStyleWord(true);
        // Else, disable text wrapping
        } else {
            jta.setLineWrap(false);
            jta.setWrapStyleWord(false);
        }
    }

    // Set a Component opacity
    protected static void setOpaque(JComponent jc, String opaque) {
        opaque = opaque.toLowerCase();
        if (opaque.equals("true"))
            jc.setOpaque(true);
        // Else, make the component transparent
        jc.setOpaque(false);
    }

    // Set a Component visibility
    protected static void setVisible(Component component, String visible) {
        visible = visible.toLowerCase();
        if (visible.equals("true"))
            component.setVisible(true);
        // Else, make the component invisible
        component.setVisible(false);
    }

    // Set a Component visibility
    protected static void setResizable(Frame frame, String resizable) {
        resizable = resizable.toLowerCase();
        if (resizable.equals("true"))
            frame.setResizable(true);
        // Else, make the component statically sized (not resizable)
        frame.setResizable(false);
    }

    // Set a close operation
    protected static void setCloseOp(JFrame frame, String close_op) {
        frame.setDefaultCloseOperation(getCloseOp(close_op));
    }

    // Check valid component attribute categories
    protected static boolean isValidAtrCat(String query_cat_name) {
        // First check if the query category name String is null
        if (query_cat_name == null)
            return false;
        for (String cat_name : COMPONENT_ATTRIBUTE_CATEGORIES) {
            if (query_cat_name.equals(cat_name))
                return true; // The query category name is valid
        }
        // Else, the query category name is not valid
        return false;
    }

    protected static int getConstraint(String c) {
        c = c.toLowerCase();
        if (c.equals("horizontal"))
            return GridBagConstraints.HORIZONTAL;
        if (c.equals("vertical"))
            return GridBagConstraints.VERTICAL;
        if (c.equals("both"))
            return GridBagConstraints.BOTH;
        // Else, return none
        return GridBagConstraints.NONE;
    }

    // Return a Color based on specified String color
    protected static Color getColor(String c) {
        c = c.toLowerCase();
        if (c.equals("black"))
            return Color.black; // Black
        if (c.equals("blue"))
            return Color.blue; // Blue
        if (c.equals("cyan"))
            return Color.cyan; // Cyan
        if (c.equals("dark gray"))
            return Color.darkGray; // Dark gray
        if (c.equals("gray"))
            return Color.gray; // Gray
        if (c.equals("green"))
            return Color.green; // Green
        if (c.equals("light gray"))
            return Color.lightGray; // Light gray
        if (c.equals("magenta"))
            return Color.magenta; // Magenta
        if (c.equals("orange"))
            return Color.orange; // Orange
        if (c.equals("pink"))
            return Color.pink; // Pink
        if (c.equals("red"))
            return Color.red; // Red
        if (c.equals("white"))
            return Color.white;
        // Else, return yellow
        return Color.yellow;
    }
    
    // Return a Font based on specified String font style and int size
    // Possible String constants for the Font's "name" are:
    //  1) DIALOG, 
    protected static Font getFont(String name, String fs, int size) {
        // First set font name based on String name
        name = name.toLowerCase();
        if (name.equals("dialog"))
            name = Font.DIALOG;
        if (name.equals("dialog input"))
            name = Font.DIALOG_INPUT;
        if (name.equals("monospaced"))
            name = Font.MONOSPACED;
        if (name.equals("sans serif"))
            name = Font.SANS_SERIF;
        if (name.equals("serif"))
            name = Font.SERIF;
        
        // Then set font style base on String font style (fs)
        fs = fs.toLowerCase();
        // Set font style to plain (Font.PLAIN) by default
        int style = Font.PLAIN;
        // Set style based on String f
        if (fs.equals("bold"))
            style = Font.BOLD; // Bold
        if (fs.equals("italic"))
            style = Font.ITALIC; // Italic

        // Finally, return a new Font based on the specifications
        return new Font(name, style, size);
    }

    // Return a Layout based on specified String layout
    protected static LayoutManager getLayout(String layout) {
        layout = layout.toLowerCase();
        if (layout.equals("border"))
            return new BorderLayout(); // Border layout
        if (layout.equals("flow"))
            return new FlowLayout(); // Flow layout
        if (layout.equals("grid"))
            return new GridLayout(); // Grid layout
        if (layout.equals("grid bag"))
            return new GridBagLayout(); // Grid layout
        // Else return null layout
        return null;
    }

    protected static String getBorderPos(String pos) {
        pos = pos.toLowerCase();
        if (pos.equals("north"))
            return BorderLayout.NORTH;
        if (pos.equals("south"))
            return BorderLayout.SOUTH;
        if (pos.equals("east"))
            return BorderLayout.EAST;
        if (pos.equals("west"))
            return BorderLayout.WEST;
        // Else return center
        return BorderLayout.CENTER;
    }

    protected static int getFlowPos(String pos) {
        pos = pos.toLowerCase();
        if (pos.equals("leading"))
            return FlowLayout.LEADING;
        if (pos.equals("trailing"))
            return FlowLayout.TRAILING;
        if (pos.equals("right"))
            return FlowLayout.RIGHT;
        if (pos.equals("left"))
            return FlowLayout.LEFT;
        // Else return center
        return FlowLayout.CENTER;
    }

    protected static int getCloseOp(String co) {
        co = co.toLowerCase();
        // By default, return exit on close
        return JFrame.EXIT_ON_CLOSE;
    }

    protected static void printCompAttr(String comp_name, String comp_type,
                        Component component) {
        System.out.println("\n==============================================");
        System.out.println("\nPrinting the attributes for " + comp_name + ":");
        // Print the component's attributes
        System.out.print("\nIts top-left coordinate is (" + component.getX() +
                        ", " + component.getY() + ")");
        System.out.print("\nIts dimensions are " + component.getWidth() +
                        " by " + component.getHeight());
        // Check if it has text
        String text = null;
        if (comp_type.equals("jbutton"))
            text = ((JButton) component).getText();
        if (comp_type.equals("jlabel"))
            text = ((JLabel) component).getText();
        if (comp_type.equals("jtextarea"))
            text = ((JTextArea) component).getText();
        if (text != null)
            System.out.print("\nIts text is:\n" + text + "\n");
        System.out.println("\n==============================================");
    }
}
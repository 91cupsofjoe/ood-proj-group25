package default_proj;

import default_proj.data.DataReader;
// import default_proj.processor.Processor; Processor is a static class
import default_proj.ui.Menu;


public class Main {
    public static void main(String[] args) {

        if (args.length != 4) {
            System.err.println("Invalid number of arguments");
            return;
        }
        if (!args[0].equals("csv") && !args[0].equals("json")) {
            System.err.println("Parking format must be 'csv' or 'json'");
            return;
        }
        
        DataReader.init(args[0], args[1], args[2], args[3]);
        DataReader dr = DataReader.getInstance();
        if (dr == null) { return; }
        
        // This will initialize the menu and its interactive GUI
        Menu menu = Menu.getMenu(true);
    }

    public static Menu getMenuForTesting(String data_dir) {
        String[] args = new String[]{
            "json",
            data_dir + "parking.json",
            data_dir + "properties.csv",
            data_dir + "population.txt"
        };

        DataReader.init(args[0], args[1], args[2], args[3]);
        DataReader dr = DataReader.getInstance();
        if (dr == null) { return null; }
        
        // This will not initialize the menu and its interactive GUI
        return Menu.getMenu(false);
    }
}
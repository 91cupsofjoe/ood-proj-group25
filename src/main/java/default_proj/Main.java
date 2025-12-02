package default_proj;

import default_proj.data.DataReader;
import default_proj.processor.Processor;
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


        int selection = -1;
        while (selection != 0) {

            selection = Menu.promptSelection();

            if (selection == 1) {
                System.out.println(Processor.getTotalPopulation());
            } else if (selection == 2) {
                System.out.print(Processor.getFinesPerCapita());
            } else if (selection == 3) {
                int zip = Menu.promptZipCode();
                System.out.println(Processor.getAverageMarketValue(zip));
            }

        }


    }

}

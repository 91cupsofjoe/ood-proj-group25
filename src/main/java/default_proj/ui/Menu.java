package default_proj.ui;

import java.util.*;

public class Menu {

    private static final String MENU_OPTIONS = """
        1 : Total Population
        2 : Parking Fines per Capita
        3 : Average Residence Market Value for Zip Code
        4 : Average Residence Livable Area for Zip Code
        5 : Residential Market Value per Capita for Zip Code
        6 : (Data Sanity Check) Parking File Entries
        7 : (Data Sanity Check) Property File Entries
        0 : Exit""";

    public static int promptSelection() {
        Scanner sc = new Scanner(System.in);
        int selection;
        while (true) {
            System.out.println(MENU_OPTIONS);
            System.out.print("Enter menu selection: ");
            String ln = sc.nextLine();
            try {
                selection = Integer.parseInt(ln);
                if (selection < 0 || selection > 7) {
                    throw new NumberFormatException();
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid menu selection\n");
            }
        }
        return selection;
    }

    public static int promptZipCode() {
        Scanner sc = new Scanner(System.in);
        int zip_code;
        System.out.print("Enter zip code: ");
        String ln = sc.nextLine();
        try {
            zip_code = Integer.parseInt(ln);
            return zip_code;
        } catch (NumberFormatException e) {
            System.out.println("Invalid zip code\n");
        }
        return 0;
    }

    public static int promptNumEntries(int max) {
        Scanner sc = new Scanner(System.in);
        int entries = 0;
        System.out.print("How many entries: ");
        String ln = sc.nextLine();
        try {
            entries = Integer.parseInt(ln);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number of entries\n");
        }
        if (entries > max)
            entries = max;
        if (entries < 0)
            entries = 0;
        return entries;
    }

    public static void printParkingEntries(ArrayList<String[]> parking_data) {
        int num_entries = Menu.promptNumEntries(parking_data.size());
        String[] entry, categories = {"date", "fine", "violation",
                        "plate id", "state", "ticket number", "zip code"};
        System.out.println("\n================ Parking File Entries ================\n");
        for (int i = 0; i < num_entries; i++) {
            entry = parking_data.get(i);
            System.out.println("Entry # " + (i + 1) + ": {");
            for (int j = 0; j < categories.length; j++) {
                System.out.println("\t" + categories[j] + ": " + entry[j]);
            }
            System.out.println("}\n");
        }
        System.out.println("=================================================\n");
    }

    public static void printPropertyEntries(ArrayList<String[]> property_data) {
        int num_entries = promptNumEntries(property_data.size() - 1);
        String[] entry, categories = property_data.get(0);
        System.out.println("\n================ Property File Entries ================\n");
        for (int i = 1; i < num_entries + 1; i++) {
            entry = property_data.get(i);
            System.out.println("Entry # " + i + ": {");
            for (int j = 0; j < categories.length; j++) {
                System.out.println("\t" + categories[j] + ": " + entry[j]);
            }
            System.out.println("}\n");
        }
        System.out.println("==================================================\n");
    }
}

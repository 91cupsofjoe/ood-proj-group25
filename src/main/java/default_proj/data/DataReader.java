package default_proj.data;

import default_proj.common.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DataReader {

    private static DataReader instance;
    private final ArrayList<Parking> parking_data;
    private final ArrayList<Property> property_data;
    private final ArrayList<Population> population_data;

    private DataReader(String parking_format, String parking_file,
                       String property_file, String population_file) throws IOException, ParseException {
        if (parking_format.equals("json")) {
            parking_data = parseParkingJSON(new FileReader(parking_file));
        } else {
            parking_data = parseParkingCSV(new FileReader(parking_file));
        }
        property_data = parsePropertiesCSV(new FileReader(property_file));
        population_data = parsePopulationTXT(new FileReader(population_file));
    }

    public static void init(String parking_format, String parking_file,
                            String property_file, String population_file) {
        if (instance == null) {
            try {
                instance = new DataReader(parking_format, parking_file, property_file, population_file);
            } catch (IOException | ParseException e) {
                System.err.println("Error reading file: " + e.getMessage());
                instance = null;
            }
        }
    }

    public static DataReader getInstance() { return instance; }

    public ArrayList<Parking> getParkingData() { return parking_data; }

    public ArrayList<Property> getPropertyData() { return property_data; }

    public ArrayList<Population> getPopulationData() { return population_data; }


    private static ArrayList<Parking> parseParkingJSON(FileReader fp) throws IOException, ParseException {
        ArrayList<Parking> data = new ArrayList<>();
        JSONArray json_arr = (JSONArray) new JSONParser().parse(fp);
        for (Object obj : json_arr) {
            JSONObject json_obj = (JSONObject) obj;
            String zip_code = json_obj.get("zip_code").toString();
            String fine = json_obj.get("fine").toString();
            if (!zip_code.isEmpty() && !fine.isEmpty()) {
                data.add(new Parking(zip_code, fine));
            }
        }
        return data;
    }


    private static ArrayList<Parking> parseParkingCSV(FileReader fp) throws IOException {
        // The order of this array matches the order of fields in the csv file
        // String[] fields = {"date", "fine", "violation", "plate_id", "state", "ticket_number", "zip_code"};
        ArrayList<Parking> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(fp);
            String ln;
            while ((ln = br.readLine()) != null) {
                String[] fields = ln.split(",");
                if (fields.length == 7) {
                    String zip_code = fields[6];
                    String fine = fields[1];
                    if (!zip_code.isEmpty() && !fine.isEmpty()) {
                        data.add(new Parking(zip_code, fine));
                    }
                }
            }
        return data;
    }


    private static ArrayList<Property> parsePropertiesCSV(FileReader fp) throws IOException {

        // This array sets the output fields
        String[] fields = {"zip_code", "market_value", "total_livable_area"};

        ArrayList<Property> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(fp);
        String ln = br.readLine();
        List<String> first_line = Arrays.asList(ln.split(","));
        int[] index_list = new int[fields.length];
        for (int i = 0; i < fields.length; i++) {
            index_list[i] = first_line.indexOf(fields[i]); // Finding field name in first line
        }

        while ((ln = br.readLine()) != null) {
            String[] ln_arr = ln.split(",");
            String zip_code = ln_arr[index_list[0]].strip(); // Indexes correspond to fields array
            String market_value = ln_arr[index_list[1]].strip();
            String livable_area = ln_arr[index_list[2]].strip();

            if (zip_code.length() >= 5) {
                zip_code = zip_code.substring(0, 5).trim();
                if (zip_code.length() == 5) {
                    data.add(new Property(zip_code, market_value, livable_area));
                }
            }
        }
        return data;
    }


    private static ArrayList<Population> parsePopulationTXT(FileReader fp) throws IOException {
        ArrayList<Population> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(fp);
            String ln;
            while ((ln = br.readLine()) != null) {
                String[] ln_arr = ln.split(" ");
                data.add(new Population(ln_arr[0], ln_arr[1]));
            }
        return data;
    }

}

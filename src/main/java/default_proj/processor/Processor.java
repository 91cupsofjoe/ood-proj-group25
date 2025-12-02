package default_proj.processor;

import default_proj.common.*;
import default_proj.data.DataReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Processor {

    public static int getTotalPopulation() {
        ArrayList<Population> populationData = DataReader.getInstance().getPopulationData();
        int total = 0;
        for (Population p : populationData) {
            total += p.value();
        }
        return total;
    }

    public static String getFinesPerCapita() {
        ArrayList<Parking> parkingData = DataReader.getInstance().getParkingData();
        ArrayList<Population> populationData = DataReader.getInstance().getPopulationData();

        Map<Integer, Integer> populationMap = new TreeMap<>();
        for (Population p : populationData) {
            populationMap.put(p.zip_code(), p.value());
        }

        Map<Integer, Integer> finesMap = new TreeMap<>();
        for (Parking p : parkingData) {
            if (!p.state().equals("PA")) {
                continue;
            }
            int zip = p.zip_code();
            finesMap.put(zip, finesMap.getOrDefault(zip, 0) + p.fine());
        }

        StringBuilder result = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : finesMap.entrySet()) {
            int zip = entry.getKey();
            int totalFines = entry.getValue();
            if (totalFines == 0) {
                continue;
            }
            Integer population = populationMap.get(zip);
            if (population == null || population == 0) {
                continue;
            }
            double perCapita = (double) totalFines / population;
            result.append(String.format("%d %.4f%n", zip, perCapita));
        }
        return result.toString();
    }

}


package default_proj.processor;

import default_proj.common.*;
import default_proj.data.DataReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Processor {

    private static final Map<Integer, Integer> marketValueCache = new HashMap<>();

    public static void clearCacheForTesting() {
        marketValueCache.clear();
    }

    public static int getTotalPopulation() {
        return DataReader.getInstance().getPopulationData()
                .stream()
                .mapToInt(Population::value)
                .sum();
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

    public static int getAverageMarketValue(int zipCode) {
        if (marketValueCache.containsKey(zipCode)) {
            return marketValueCache.get(zipCode);
        }
        double totalValue = 0;
        int count = 0;
        for (Property p : new ZipCodeProperties(zipCode)) {
            if (p.market_value() <= 0) {
                continue;
            }
            totalValue += p.market_value();
            count++;
        }
        int result = 0;
        if (count > 0) {
            result = (int) Math.round(totalValue / count);
        }
        marketValueCache.put(zipCode, result);
        return result;
    }

    public static int getAverageLivableArea(int zipCode) {
        double totalArea = 0;
        int count = 0;
        for (Property p : new ZipCodeProperties(zipCode)) {
            if (p.livable_area() <= 0) {
                continue;
            }
            totalArea += p.livable_area();
            count++;
        }
        if (count == 0) {
            return 0;
        }
        return (int) Math.round(totalArea / count);
    }

    public static int getMarketValuePerCapita(int zipCode) {
        double totalValue = 0;
        for (Property p : new ZipCodeProperties(zipCode)) {
            if (p.market_value() <= 0) {
                continue;
            }
            totalValue += p.market_value();
        }

        if (totalValue == 0) {
            return 0;
        }

        int population = getPopulationForZip(zipCode);
        if (population == 0) {
            return 0;
        }

        return (int) Math.round(totalValue / population);
    }

    private static int getPopulationForZip(int zipCode) {
        return DataReader.getInstance().getPopulationData()
                .stream()
                .filter(p -> p.zip_code() == zipCode)
                .mapToInt(Population::value)
                .findFirst()
                .orElse(0);
    }

    public static int getLivableAreaPerCapita(int zipCode) {
        double totalArea = 0;
        for (Property p : new ZipCodeProperties(zipCode)) {
            if (p.livable_area() <= 0) {
                continue;
            }
            totalArea += p.livable_area();
        }

        if (totalArea == 0) {
            return 0;
        }

        int population = getPopulationForZip(zipCode);
        if (population == 0) {
            return 0;
        }

        return (int) Math.round(totalArea / population);
    }

    public static int getAverageValuePerSqFt(int zipCode) {
        double totalValuePerSqFt = 0;
        int count = 0;
        for (Property p : new ZipCodeProperties(zipCode)) {
            if (p.market_value() <= 0 || p.livable_area() <= 0) {
                continue;
            }
            totalValuePerSqFt += p.market_value() / p.livable_area();
            count++;
        }
        if (count == 0) {
            return 0;
        }
        return (int) Math.round(totalValuePerSqFt / count);
    }

}


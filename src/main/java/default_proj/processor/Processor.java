package default_proj.processor;

import default_proj.common.*;
import default_proj.data.DataReader;
import java.util.ArrayList;

public class Processor {

    public static int getTotalPopulation() {
        ArrayList<Population> populationData = DataReader.getInstance().getPopulationData();
        int total = 0;
        for (Population p : populationData) {
            total += p.value();
        }
        return total;
    }

}


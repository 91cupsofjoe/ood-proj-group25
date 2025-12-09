import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import default_proj.processor.Processor;
import default_proj.data.DataReader;
import default_proj.common.Population;

class GetTotalPopulationTest {

    @AfterEach
    void resetDataReader() {
        DataReader.setInstanceForTesting(null);
    }

    @Test
    void testProcessorInstantiation() {
        Processor processor = new Processor();
        assertNotNull(processor);
    }

    @Test
    void testEmptyPopulationData() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Population> getPopulationData() {
                return new ArrayList<>();
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(0, Processor.getTotalPopulation());
    }

    @Test
    void testSingleZipCode() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Population> getPopulationData() {
                ArrayList<Population> data = new ArrayList<>();
                data.add(new Population(19104, 50000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(50000, Processor.getTotalPopulation());
    }

    @Test
    void testMultipleZipCodes() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Population> getPopulationData() {
                ArrayList<Population> data = new ArrayList<>();
                data.add(new Population(19104, 50000));
                data.add(new Population(19103, 30000));
                data.add(new Population(19102, 20000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(100000, Processor.getTotalPopulation());
    }

}


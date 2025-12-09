import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import default_proj.processor.Processor;
import default_proj.data.DataReader;
import default_proj.common.Property;
import default_proj.common.Population;

class GetMarketValuePerCapitaTest {

    @AfterEach
    void resetDataReader() {
        DataReader.setInstanceForTesting(null);
        Processor.clearCacheForTesting();
    }

    @Test
    void testNoPropertiesForZipCode() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19103, 100000, 1000));
                return data;
            }

            @Override
            public ArrayList<Population> getPopulationData() {
                ArrayList<Population> data = new ArrayList<>();
                data.add(new Population(19104, 50000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(0, Processor.getMarketValuePerCapita(19104));
    }

    @Test
    void testZipCodeNotInPopulationData() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19104, 100000, 1000));
                data.add(new Property(19104, 200000, 2000));
                return data;
            }

            @Override
            public ArrayList<Population> getPopulationData() {
                ArrayList<Population> data = new ArrayList<>();
                data.add(new Population(19103, 50000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(0, Processor.getMarketValuePerCapita(19104));
    }

    @Test
    void testValidPropertiesAndPopulation() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19104, 100000, 1000));
                data.add(new Property(19104, 200000, 2000));
                data.add(new Property(19104, 300000, 3000));
                return data;
            }

            @Override
            public ArrayList<Population> getPopulationData() {
                ArrayList<Population> data = new ArrayList<>();
                data.add(new Population(19104, 30000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(20, Processor.getMarketValuePerCapita(19104));
    }

    @Test
    void testIgnoresZeroAndNegativeMarketValues() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19104, 100000, 1000));
                data.add(new Property(19104, 0, 2000));
                data.add(new Property(19104, -50000, 3000));
                data.add(new Property(19104, 100000, 4000));
                return data;
            }

            @Override
            public ArrayList<Population> getPopulationData() {
                ArrayList<Population> data = new ArrayList<>();
                data.add(new Population(19104, 20000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(10, Processor.getMarketValuePerCapita(19104));
    }

    @Test
    void testProperRounding() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19104, 100001, 1000));
                data.add(new Property(19104, 100002, 2000));
                data.add(new Property(19104, 100003, 3000));
                return data;
            }

            @Override
            public ArrayList<Population> getPopulationData() {
                ArrayList<Population> data = new ArrayList<>();
                data.add(new Population(19104, 30000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(10, Processor.getMarketValuePerCapita(19104));
    }

}


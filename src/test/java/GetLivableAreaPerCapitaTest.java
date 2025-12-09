import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import default_proj.processor.Processor;
import default_proj.data.DataReader;
import default_proj.common.Property;
import default_proj.common.Population;

class GetLivableAreaPerCapitaTest {

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
        assertEquals(0, Processor.getLivableAreaPerCapita(19104));
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
        assertEquals(0, Processor.getLivableAreaPerCapita(19104));
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
                data.add(new Population(19104, 3000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(2, Processor.getLivableAreaPerCapita(19104));
    }

    @Test
    void testIgnoresZeroAndNegativeLivableAreas() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19104, 100000, 1000));
                data.add(new Property(19104, 200000, 0));
                data.add(new Property(19104, 300000, -500));
                data.add(new Property(19104, 400000, 3000));
                return data;
            }

            @Override
            public ArrayList<Population> getPopulationData() {
                ArrayList<Population> data = new ArrayList<>();
                data.add(new Population(19104, 2000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(2, Processor.getLivableAreaPerCapita(19104));
    }

    @Test
    void testProperRounding() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19104, 100000, 10001));
                data.add(new Property(19104, 200000, 10002));
                data.add(new Property(19104, 300000, 10003));
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
        assertEquals(1, Processor.getLivableAreaPerCapita(19104));
    }

}


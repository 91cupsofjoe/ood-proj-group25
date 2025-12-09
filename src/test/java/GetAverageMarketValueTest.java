import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import default_proj.processor.Processor;
import default_proj.data.DataReader;
import default_proj.common.Property;

class GetAverageMarketValueTest {

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
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(0, Processor.getAverageMarketValue(19104));
    }

    @Test
    void testMultiplePropertiesWithValidMarketValues() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19104, 400000, 1000));
                data.add(new Property(19104, 200000, 2000));
                data.add(new Property(19104, 300000, 3000));
                data.add(new Property(19103, 500000, 5000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(300000, Processor.getAverageMarketValue(19104));
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
                data.add(new Property(19104, 200000, 4000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(150000, Processor.getAverageMarketValue(19104));
    }

    @Test
    void testProperRounding() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19104, 100000, 1000));
                data.add(new Property(19104, 100000, 2000));
                data.add(new Property(19104, 100001, 3000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(100000, Processor.getAverageMarketValue(19104));
    }

    @Test
    void testMemoization() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19104, 150000, 1500));
                data.add(new Property(19104, 250000, 2500));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        int firstCall = Processor.getAverageMarketValue(19104);
        assertEquals(200000, firstCall);
        
        DataReader mockReader2 = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19104, 999999, 9999));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader2);
        int secondCall = Processor.getAverageMarketValue(19104);
        assertEquals(200000, secondCall);
    }

}


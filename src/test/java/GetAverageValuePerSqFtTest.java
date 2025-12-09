import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import default_proj.processor.Processor;
import default_proj.data.DataReader;
import default_proj.common.Property;

class GetAverageValuePerSqFtTest {

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
        assertEquals(0, Processor.getAverageValuePerSqFt(19104));
    }

    @Test
    void testMultiplePropertiesWithValidValues() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19104, 100000, 1000));
                data.add(new Property(19104, 200000, 2000));
                data.add(new Property(19104, 300000, 3000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(100, Processor.getAverageValuePerSqFt(19104));
    }

    @Test
    void testIgnoresPropertiesWithZeroOrNegativeMarketValue() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19104, 100000, 1000));
                data.add(new Property(19104, 0, 2000));
                data.add(new Property(19104, -50000, 1000));
                data.add(new Property(19104, 200000, 2000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(100, Processor.getAverageValuePerSqFt(19104));
    }

    @Test
    void testIgnoresPropertiesWithZeroOrNegativeLivableArea() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19104, 100000, 1000));
                data.add(new Property(19104, 200000, 0));
                data.add(new Property(19104, 300000, -500));
                data.add(new Property(19104, 400000, 2000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(150, Processor.getAverageValuePerSqFt(19104));
    }

    @Test
    void testProperRounding() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19104, 150000, 1000));
                data.add(new Property(19104, 150001, 1000));
                data.add(new Property(19104, 150002, 1000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(150, Processor.getAverageValuePerSqFt(19104));
    }

}


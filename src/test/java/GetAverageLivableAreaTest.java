import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import default_proj.processor.Processor;
import default_proj.data.DataReader;
import default_proj.common.Property;

class GetAverageLivableAreaTest {

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
        assertEquals(0, Processor.getAverageLivableArea(19104));
    }

    @Test
    void testMultiplePropertiesWithValidLivableAreas() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19104, 100000, 1000));
                data.add(new Property(19104, 200000, 2000));
                data.add(new Property(19104, 300000, 3000));
                data.add(new Property(19103, 500000, 5000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(2000, Processor.getAverageLivableArea(19104));
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
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(2000, Processor.getAverageLivableArea(19104));
    }

    @Test
    void testProperRounding() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Property> getPropertyData() {
                ArrayList<Property> data = new ArrayList<>();
                data.add(new Property(19104, 100000, 1000));
                data.add(new Property(19104, 200000, 1000));
                data.add(new Property(19104, 300000, 1001));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals(1000, Processor.getAverageLivableArea(19104));
    }

}


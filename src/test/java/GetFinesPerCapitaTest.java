import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import default_proj.processor.Processor;
import default_proj.data.DataReader;
import default_proj.common.Population;
import default_proj.common.Parking;

class GetFinesPerCapitaTest {

    @AfterEach
    void resetDataReader() {
        DataReader.setInstanceForTesting(null);
    }

    @Test
    void testEmptyParkingData() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Parking> getParkingData() {
                return new ArrayList<>();
            }

            @Override
            public ArrayList<Population> getPopulationData() {
                ArrayList<Population> data = new ArrayList<>();
                data.add(new Population(19104, 50000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        assertEquals("", Processor.getFinesPerCapita());
    }

    @Test
    void testMultipleZipCodesWithProperFormatting() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Parking> getParkingData() {
                ArrayList<Parking> data = new ArrayList<>();
                data.add(new Parking(19104, 100, "PA"));
                data.add(new Parking(19104, 50, "PA"));
                data.add(new Parking(19103, 200, "PA"));
                data.add(new Parking(19102, 300, "PA"));
                return data;
            }

            @Override
            public ArrayList<Population> getPopulationData() {
                ArrayList<Population> data = new ArrayList<>();
                data.add(new Population(19104, 10000));
                data.add(new Population(19103, 20000));
                data.add(new Population(19102, 30000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        String expected = "19102 0.0100\n19103 0.0100\n19104 0.0150\n";
        assertEquals(expected, Processor.getFinesPerCapita());
    }

    @Test
    void testIgnoresNonPAStates() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Parking> getParkingData() {
                ArrayList<Parking> data = new ArrayList<>();
                data.add(new Parking(19104, 100, "PA"));
                data.add(new Parking(19104, 200, "NJ"));
                data.add(new Parking(19103, 300, "NY"));
                data.add(new Parking(19102, 400, "PA"));
                return data;
            }

            @Override
            public ArrayList<Population> getPopulationData() {
                ArrayList<Population> data = new ArrayList<>();
                data.add(new Population(19104, 10000));
                data.add(new Population(19103, 20000));
                data.add(new Population(19102, 40000));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        String expected = "19102 0.0100\n19104 0.0100\n";
        assertEquals(expected, Processor.getFinesPerCapita());
    }

    @Test
    void testIgnoresZeroPopulationAndZeroFines() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Parking> getParkingData() {
                ArrayList<Parking> data = new ArrayList<>();
                data.add(new Parking(19104, 100, "PA"));
                data.add(new Parking(19103, 0, "PA"));
                data.add(new Parking(19102, 200, "PA"));
                data.add(new Parking(19105, 300, "PA"));
                return data;
            }

            @Override
            public ArrayList<Population> getPopulationData() {
                ArrayList<Population> data = new ArrayList<>();
                data.add(new Population(19104, 10000));
                data.add(new Population(19103, 20000));
                data.add(new Population(19102, 0));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        String expected = "19104 0.0100\n";
        assertEquals(expected, Processor.getFinesPerCapita());
    }

    @Test
    void testProperRoundingAndTrailingZeros() {
        DataReader mockReader = new DataReader() {
            @Override
            public ArrayList<Parking> getParkingData() {
                ArrayList<Parking> data = new ArrayList<>();
                data.add(new Parking(19104, 123, "PA"));
                data.add(new Parking(19103, 456, "PA"));
                return data;
            }

            @Override
            public ArrayList<Population> getPopulationData() {
                ArrayList<Population> data = new ArrayList<>();
                data.add(new Population(19104, 9999));
                data.add(new Population(19103, 12345));
                return data;
            }
        };
        DataReader.setInstanceForTesting(mockReader);
        String expected = "19103 0.0369\n19104 0.0123\n";
        assertEquals(expected, Processor.getFinesPerCapita());
    }

}


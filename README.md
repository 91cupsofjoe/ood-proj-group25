# Philadelphia Data Analysis Project

## How to Run

### Compile
```bash
mvn compile
```

### Run with CSV format
```bash
java -cp "target/classes:json-simple-1.1.1.jar" default_proj.Main csv parking.csv properties.csv population.txt
```

### Run with JSON format
```bash
java -cp "target/classes:json-simple-1.1.1.jar" default_proj.Main json parking.json properties.csv population.txt
```

## Menu Options

1. Total Population - Shows total population for all ZIP codes
2. Parking Fines per Capita - Shows fines per capita for each ZIP code
3. Average Residence Market Value - Enter ZIP code to see average market value
4. Average Residence Livable Area - Enter ZIP code to see average livable area
5. Residential Market Value per Capita - Enter ZIP code to see market value per capita
6. Total Livable Area per Capita - Enter ZIP code to see total livable area per capita
7. Average Value per Square Foot - Enter ZIP code to see average value per square foot
0. Exit

## Test ZIP Codes

- 19104 (large population)
- 19103
- 19147
- 19102


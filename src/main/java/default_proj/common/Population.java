package default_proj.common;

public class Population {
    private final int zip_code, value;

    public Population(int zip_code, int value) {
        this.zip_code = zip_code;
        this.value = value;
    }

    public Population(String zip_code, String value) {
        this.zip_code = Integer.parseInt(zip_code);
        this.value = Integer.parseInt(value);
    }

    public int zip_code() { return zip_code; }
    public int value() { return value; }

    @Override
    public String toString() {
        return "Population: {zip_code="+zip_code+", value="+value+"}";
    }

}

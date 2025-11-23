package default_proj.common;

public class Property {
    private final int zip_code;
    private double market_value, livable_area;

    public Property(int zip_code, double market_value, double livable_area) {
        this.zip_code = zip_code;
        this.market_value = market_value;
        this.livable_area = livable_area;
    }

    public Property(String zip_code, String market_value, String livable_area) {
        this.zip_code = Integer.parseInt(zip_code);
        try {
            this.market_value = Double.parseDouble(market_value);
        } catch (NumberFormatException e) {
            this.market_value = 0;
        }
        try {
            this.livable_area = Double.parseDouble(livable_area);
        } catch (NumberFormatException e) {
            this.livable_area = 0;
        }
    }

    public int zip_code() { return zip_code; }
    public double market_value() { return market_value; }
    public double livable_area() { return livable_area; }

    @Override
    public String toString() {
        return "Property: {zip_code="+zip_code+", market_value="+market_value+", livable_area="+livable_area+"}";
    }
}

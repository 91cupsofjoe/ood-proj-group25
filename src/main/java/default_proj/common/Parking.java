package default_proj.common;

public class Parking {
    private final int zip_code, fine;
    private final String state;

    public Parking(int zip_code, int fine, String state) {
        this.zip_code = zip_code;
        this.fine = fine;
        this.state = state;
    }

    public Parking(String zip_code, String fine, String state) {
        this.zip_code = Integer.parseInt(zip_code);
        this.fine = Integer.parseInt(fine);
        this.state = state;
    }

    public int zip_code() { return zip_code; }
    public int fine() { return fine; }
    public String state() { return state; }

    @Override
    public String toString() {
        return "Parking: {zip_code="+zip_code+", fine="+fine+", state="+state+"}";
    }

}

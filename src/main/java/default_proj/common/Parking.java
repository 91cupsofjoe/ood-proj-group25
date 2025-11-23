package default_proj.common;

public class Parking {
    public int zip_code, fine;

    public Parking(int zip_code, int fine) {
        this.zip_code = zip_code;
        this.fine = fine;
    }

    public Parking(String zip_code, String fine) {
        this.zip_code = Integer.parseInt(zip_code);
        this.fine = Integer.parseInt(fine);
    }

    @Override
    public String toString() {
        return "Parking: {zip_code="+zip_code+", fine="+fine+"}";
    }

}

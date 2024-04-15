public class DistanceAltimetry extends Activity {
    private int distance;
    private int altimetry;

    public DistanceAltimetry() {
        super();
        this.distance = 0;
        this.altimetry = 0;
    }

    public DistanceAltimetry(int duration, int intensity, int distance, int altimetry) {
        super(duration, intensity);
        this.distance = distance;
        this.altimetry = altimetry;
    }

    public DistanceAltimetry(DistanceAltimetry distanceAltimetry) {
        super(distanceAltimetry);
        this.distance = distanceAltimetry.getDistance();
        this.altimetry = distanceAltimetry.getAltimetry();
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getAltimetry() {
        return this.altimetry;
    }

    public void setAltimetry(int altimetry) {
        this.altimetry = altimetry;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Acivity {\n");
        sb.append("  Duration: " + this.duration + " minutes,\n");
        sb.append("  Intensity: " + this.intensity + ",\n");
        sb.append("  Distance: " + this.distance + " meters,\n");
        sb.append("  Altimetry: " + this.altimetry + " meters\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        DistanceAltimetry distanceAltimetry = (DistanceAltimetry) o;
        return (
            this.name.equals(distanceAltimetry.getName()) &&
            this.duration == distanceAltimetry.getDuration() &&
            this.intensity == distanceAltimetry.getIntensity() &&
            this.distance == distanceAltimetry.getDistance() &&
            this.altimetry == distanceAltimetry.getAltimetry()
        );
    }

    @Override
    public DistanceAltimetry clone() {
        return new DistanceAltimetry(this);
    }

    @Override
    public int caloriesBurned(User u) {
        // TODO Calculate calories burned based on user, distance, altimetry, duration, and intensity
        return 0;
    }
}
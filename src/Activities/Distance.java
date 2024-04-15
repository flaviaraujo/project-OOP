public class Distance extends Activity {
    private int distance;

    public Distance() {
        super();
        this.distance = 0;
    }

    public Distance(int duration, int intensity, int distance) {
        super(duration, intensity);
        this.distance = distance;
    }

    public Distance(Distance distance) {
        super(distance);
        this.distance = distance.getDistance();
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Acivity {\n");
        sb.append("  Duration: " + this.duration + " minutes,\n");
        sb.append("  Intensity: " + this.intensity + ",\n");
        sb.append("  Distance: " + this.distance + " meters\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        Distance distance = (Distance) o;
        return (
            this.name.equals(distance.getName()) &&
            this.duration == distance.getDuration() &&
            this.intensity == distance.getIntensity() &&
            this.distance == distance.getDistance()
        );
    }

    @Override
    public Distance clone() {
        return new Distance(this);
    }

    @Override
    public int caloriesBurned(User u) {
        // TODO Calculate calories burned based on user, distance, duration, and intensity
        return 0;
    }
}
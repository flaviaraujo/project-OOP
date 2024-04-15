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

    @Override
    public int caloriesBurned(User u) {
        // TODO Calculate calories burned based on user, distance, altimetry, duration, and intensity
        return 0;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
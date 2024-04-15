/**
 * Activity
 */
public abstract class Activity {

    private int duration; // in minutes
    private int intensity;

    public Activity() {
        this.duration = 0;
        this.type = null;
    }

    public Activity(int duration, int intensity) {
        this.duration = duration;
        this.intensity = intensity;
    }

    public Activity(Activity activity) {
        this.duration = activity.getDuration();
        this.type = activity.getType();
    }

    public int getDuration() {
        return this.duration;
    }

    public Type getIntensity() {
        return this.intensity;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }
}

public class Distance extends Activity {}

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

    @Override
    public int caloriesBurned(User u) {
        // TODO Calculate calories burned based on user, distance, altimetry, duration, and intensity
        return 0;
    }
}

public class Repetitions extends Activity {}

public class RepetitionsWeight extends Activity {}

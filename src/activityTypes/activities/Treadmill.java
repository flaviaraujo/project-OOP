import src.activityTypes.Distance;
import src.User;

import java.io.Serializable;

public class Treadmill extends Distance implements Serializable {

    private final double MET_VALUE = 11.5;

    /* Constructors */
    public Treadmill() {
        super();
    }

    public Treadmill(String name, int duration, int intensity, boolean hard, int calories) {
        super(name, duration, intensity, hard, calories);
    }

    public Treadmill(String name, int duration, int intensity, boolean hard, int calories, int distance) {
        super(name, duration, intensity, hard, calories, distance);
    }

    public Treadmill(Treadmill treadmill) {
        super(treadmill);
    }

    /* Calculate calories */
    @Override
    public int calculateCalories(User u) {

        int intensity = this.getIntensity();
        int distance = this.getDistance();
        int weight = u.getWeight();
        int height = u.getHeight();
        int nutritionMultiplier = u.getCaloriesMultiplier();

        double weightFactor = Math.min(weight / 200.0, 2);
        weightFactor = Math.max(weightFactor, 1);

        // return (int)
        //     (weightFactor * (height / 100.0) * (nutritionMultiplier / 100.0) *
        //     met * (intensity / 100.0) * duration *
        //     (distance / 1000.0));

        return (int)
            (weight * (nutritionMultiplier / 100.0) *
            this.MET_VALUE * (intensity / 100.0) * (distance / 1000.0));
    }
}

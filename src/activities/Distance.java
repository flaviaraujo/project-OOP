package src.activities;

import src.Activity;
import src.User;

// TODO remove this
import src.Controller;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public class Distance extends Activity implements Serializable {

    private static final int ACTIVITY_TYPE = 1;
    private static final double MET_VALUE = 11.5;


    private int distance;

    public Distance() {
        super();
        this.distance = 0;
    }

    public Distance(
        String name, int duration, int intensity,
        boolean hard, int calories
    ) {
        super(name, duration, intensity, hard, calories);
        this.distance = 0;
    }

    public Distance(
        String name, int duration, int intensity,
        boolean hard, int calories, int distance
    ) {
        super(name, duration, intensity, hard, calories);
        this.distance = distance;
    }

    public Distance(Distance distance) {
        super(distance);
        this.distance = distance.getDistance();
    }

    @Override
    public final int getACTIVITY_TYPE() {
        return ACTIVITY_TYPE;
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
        sb.append("Activity {\n");
        sb.append("  Name: " + this.name + ",\n");
        sb.append("  Duration: " + this.duration + " minutes,\n");
        sb.append("  Intensity: " + this.intensity + ",\n");
        sb.append("  Distance: " + this.distance + " meters\n");
        sb.append("  Hard: " + this.hard + ",\n");
        if (this.calories != 0) {
            sb.append("  Calories: " + this.calories + ",\n");
        }
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
            this.distance == distance.getDistance() &&
            this.hard == distance.getHard()
            // this.calories == distance.getCalories()
        );
    }

    @Override
    public Distance clone() {
        return new Distance(this);
    }

    @Override
    public int calculateCalories(User u) {

        int intensity = this.getIntensity();
        int distance = this.getDistance();
        int weight = u.getWeight();
        int height = u.getHeight();
        int nutritionMultiplier = u.getType().getNutritionMultiplier();
        double met = MET_VALUE;

        double weightFactor = Math.min(weight / 200.0, 2);
        weightFactor = Math.max(weightFactor, 1);

        // return (int)
        //     (weightFactor * (height / 100.0) * (nutritionMultiplier / 100.0) *
        //     met * (intensity / 100.0) * duration *
        //     (distance / 1000.0));

        return (int)
            (weight * (nutritionMultiplier / 100.0) *
            met * (intensity / 100.0) * (distance / 1000.0));
    }

    @Override
    public Activity create(Scanner sc, ArrayList<Activity> userActivities) {

        Distance activity = (Distance) Activity.createAux(sc, userActivities, ACTIVITY_TYPE);
        if (activity == null) {
            return null;
        }

        // TODO remove this
        Controller c = new Controller();

        int distance = 0;

        while (true) {
            System.out.print("Enter the distance of the activity in meters: ");
            distance = c.readInt(sc);

            // check if distance is between 1 and 50000 meters
            if (distance < 1 || distance > 50000) {
                System.out.println("Distance must be between 1 and 50000 meters.");
                continue;
            }
            break;
        }

        activity.setDistance(distance);
        return activity.clone();
    }
}

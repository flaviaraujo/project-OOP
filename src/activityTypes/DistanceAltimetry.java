package src.activityTypes;

import src.Activity;
import src.User;

// TODO remove this
import src.Controller;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public abstract class DistanceAltimetry extends Activity implements Serializable {

    private static final double MET_VALUE = 16.0;
    private static final int ALTIMETRY_FACTOR = 4;

    private int distance;
    private int altimetry;

    public DistanceAltimetry() {
        super();
        this.setDistance(0);
        this.setAltimetry(0);
    }

    public DistanceAltimetry(
        String name, int duration, int intensity,
        boolean hard, int calories
    ) {
        super(name, duration, intensity, hard, calories);
        this.setDistance(0);
        this.setAltimetry(0);
    }

    public DistanceAltimetry(
        String name, int duration, int intensity,
        boolean hard, int calories, int distance, int altimetry
    ) {
        super(name, duration, intensity, hard, calories);
        this.setDistance(distance);
        this.setAltimetry(altimetry);
    }

    public DistanceAltimetry(DistanceAltimetry distanceAltimetry) {
        super(distanceAltimetry);
        this.setDistance(distanceAltimetry.getDistance());
        this.setAltimetry(distanceAltimetry.getAltimetry());
    }

    public int getDistance() {
        return this.distance;
    }

    public int getAltimetry() {
        return this.altimetry;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setAltimetry(int altimetry) {
        this.altimetry = altimetry;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Activity {\n");
        sb.append("  Name: " + this.getName() + ",\n");
        sb.append("  Duration: " + this.getDuration() + " minutes,\n");
        sb.append("  Intensity: " + this.getIntensity() + ",\n");
        sb.append("  Distance: " + this.getDistance() + " meters,\n");
        sb.append("  Altimetry: " + this.getAltimetry() + " meters\n");
        sb.append("  Hard: " + this.getHard() + ",\n");
        if (this.getCalories() != 0) {
            sb.append("  Calories: " + this.getCalories() + ",\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        DistanceAltimetry distanceAltimetry = (DistanceAltimetry) o;
        return (
            this.getName().equals(distanceAltimetry.getName()) &&
            this.getDuration() == distanceAltimetry.getDuration() &&
            this.getIntensity() == distanceAltimetry.getIntensity() &&
            this.getDistance() == distanceAltimetry.getDistance() &&
            this.getAltimetry() == distanceAltimetry.getAltimetry() &&
            this.getHard() == distanceAltimetry.getHard()
            // The calories field is not considered in the equals method
            // since it varies depending on the user parameters
        );
    }

    @Override
    public DistanceAltimetry clone() {
        return new DistanceAltimetry(this);
    }

    @Override
    public int calculateCalories(User u) {

        int altimetry = this.getAltimetry();
        int intensity = this.getIntensity();
        int distance = this.getDistance();
        int weight = u.getWeight();
        int height = u.getHeight();
        int nutritionMultiplier = u.getCaloriesMultiplier();
        double met = MET_VALUE;

        double weightFactor = Math.min(weight / 200.0, 2);
        weightFactor = Math.max(weightFactor, 1);

        // return (int)
        //     (weightFactor * (height / 100.0) * (nutritionMultiplier / 100.0) *
        //     met * (intensity / 100.0) * duration *
        //     ((distance + altimetry * ALTIMETRY_FACTOR) / 1000.0));

        return (int)
            (weight * (nutritionMultiplier / 100.0) *
            met * (intensity / 100.0) * ((distance + (altimetry * ALTIMETRY_FACTOR)) / 1000.0));
    }

    @Override
    public Activity create(Scanner sc, ArrayList<Activity> userActivities) {

        DistanceAltimetry activity = (DistanceAltimetry) super.createAux(sc, userActivities, ACTIVITY_TYPE);
        if (activity == null) {
            return null;
        }

        // TODO remove this
        Controller c = new Controller();

        int distance = 0;
        int altimetry = 0;

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

        while (true) {
            System.out.print("Enter the altimetry of the activity in meters: ");
            altimetry = c.readInt(sc);

            // check if altimetry is between 1 and 10000 meters
            if (altimetry < 1 || altimetry > 10000) {
                System.out.println("Altimetry must be between 1 and 10000 meters.");
                continue;
            }
            break;
        }

        activity.setDistance(distance);
        activity.setAltimetry(altimetry);
        return activity.clone();
    }
}

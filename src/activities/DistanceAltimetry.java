package src.activities;

import src.Activity;
import src.User;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public class DistanceAltimetry extends Activity implements Serializable {

    private static final int ACTIVITY_TYPE = 2;
    private static final double MET_VALUE = 16.0;

    private int distance;
    private int altimetry;

    public DistanceAltimetry() {
        super();
        this.distance = 0;
        this.altimetry = 0;
    }

    public DistanceAltimetry(String name, int duration, int intensity, boolean hard) {
        super(name, duration, intensity, hard);
        this.distance = 0;
        this.altimetry = 0;
    }

    public DistanceAltimetry(String name, int duration, int intensity, boolean hard, int distance, int altimetry) {
        super(name, duration, intensity, hard);
        this.distance = distance;
        this.altimetry = altimetry;
    }

    public DistanceAltimetry(DistanceAltimetry distanceAltimetry) {
        super(distanceAltimetry);
        this.distance = distanceAltimetry.getDistance();
        this.altimetry = distanceAltimetry.getAltimetry();
    }

    @Override
    public final int getACTIVITY_TYPE() {
        return ACTIVITY_TYPE;
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
        sb.append("  Name: " + this.name + ",\n");
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
        int weight = u.getWeight();
        int duration = this.getDuration();
        int intensity = this.getIntensity();
        int nutritionMultiplier = u.getType().getNutritionMultiplier();
        double met = MET_VALUE; // MET value for this activity

        double caloriesBurned = met * weight * (duration / 60.0);
        caloriesBurned *= ((intensity/100) * (nutritionMultiplier/100));
        return (int) caloriesBurned;
    }

    @Override
    public Activity create(Scanner sc, ArrayList<Activity> userActivities) {

        DistanceAltimetry activity = (DistanceAltimetry) super.createAux(sc, userActivities, ACTIVITY_TYPE);
        if (activity == null) {
            return null;
        }

        int distance = 0;
        int altimetry = 0;

        while (true) {
            System.out.print("Enter the distance of the activity in meters: ");
            try {
                distance = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Distance must be an integer.");
                continue;
            }

            // check if distance is between 1 and 50000 meters
            if (distance < 1 || distance > 50000) {
                System.out.println("Distance must be between 1 and 50000 meters.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter the altimetry of the activity in meters: ");
            try {
                altimetry = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Altimetry must be an integer.");
                continue;
            }

            // check if altimetry is between 1 and 10000 meters
            if (altimetry < 1 || altimetry > 10000) {
                System.out.println("Altimetry must be between 1 and 10000 meters.");
                continue;
            }
            break;
        }

        activity.setDistance(distance);
        activity.setAltimetry(altimetry);
        return activity; // TODO clone
    }
}

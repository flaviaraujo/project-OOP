package src.activities;

import src.Activity;
import src.User;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public class Distance extends Activity implements Serializable {

    private static final int ACTIVITY_TYPE = 1;

    private int distance;

    public Distance() {
        super();
        this.distance = 0;
    }

    public Distance(String name, int duration, int intensity) {
        super(name, duration, intensity);
        this.distance = 0;
    }

    public Distance(String name, int duration, int intensity, int distance) {
        super(name, duration, intensity);
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
        sb.append("  Name: " + this.name + ",\n");
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

    @Override
    public Activity create(Scanner sc, ArrayList<Activity> userActivities) {

        Distance activity = (Distance) Activity.createAux(sc, userActivities, ACTIVITY_TYPE);
        if (activity == null) {
            return null;
        }

        int distance = 0;

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

        activity.setDistance(distance);
        return activity; // TODO clone
    }
}

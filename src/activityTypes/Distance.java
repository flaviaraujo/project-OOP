package src.activityTypes;

import src.Activity;
import src.User;

// TODO remove this
import src.Controller;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public abstract class Distance extends Activity implements Serializable {

    private int distance;

    public Distance() {
        super();
        this.setDistance(0);
    }

    public Distance(
        String name, int duration, int intensity,
        boolean hard, int calories
    ) {
        super(name, duration, intensity, hard, calories);
        this.setDistance(0);
    }

    public Distance(
        String name, int duration, int intensity,
        boolean hard, int calories, int distance
    ) {
        super(name, duration, intensity, hard, calories);
        this.setDistance(distance);
    }

    public Distance(Distance distance) {
        super(distance);
        this.setDistance(distance.getDistance());
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
        sb.append("  Name: " + this.getName() + ",\n");
        sb.append("  Duration: " + this.getDuration() + " minutes,\n");
        sb.append("  Intensity: " + this.getIntensity() + ",\n");
        sb.append("  Distance: " + this.getDistance() + " meters\n");
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

        Distance distance = (Distance) o;
        return (
            this.getName().equals(distance.getName()) &&
            this.getDuration() == distance.getDuration() &&
            this.getIntensity() == distance.getIntensity() &&
            this.getDistance() == distance.getDistance() &&
            this.getHard() == distance.getHard()
            // The calories field is not considered in the equals method
            // since it varies depending on the user parameters
        );
    }

    @Override
    public Distance clone() {
        return new Distance(this);
    }

    public abstract int calculateCalories(User u);

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

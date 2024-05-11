
package src.activities;

import src.Activity;
import src.User;

import java.io.Serializable;
import java.util.ArrayList;

public class Swimming extends Activity implements Serializable {

    private static final double MET_VALUE = 12.5;

    private int distance;

    public Swimming() {
        super();
        this.setDistance(0);
    }

    public Swimming(
        String name, int duration, int intensity,
        boolean hard, int calories
    ) {
        super(name, duration, intensity, hard, calories);
        this.setDistance(0);
    }

    public Swimming(
        String name, int duration, int intensity,
        boolean hard, int calories, int distance
    ) {
        super(name, duration, intensity, hard, calories);
        this.setDistance(distance);
    }

    public Swimming(
        String name, int duration, int intensity,
        boolean hard, int calories, ArrayList<Integer> attributes
    ) {
        this(name, duration, intensity, hard, calories, attributes.get(0));
    }

    public Swimming(Swimming distance) {
        super(distance);
        this.setDistance(distance.getDistance());
    }

    @Override
    public int getDistance() {
        return this.distance;
    }

    @Override
    public int getAltimetry() {
        return 0;
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

        Swimming a = (Swimming) o;
        return (
            this.getName().equals(a.getName()) &&
            this.getDuration() == a.getDuration() &&
            this.getIntensity() == a.getIntensity() &&
            this.getDistance() == a.getDistance() &&
            this.getHard() == a.getHard()
            // The calories field is not considered in the equals method
            // since it varies depending on the user parameters
        );
    }

    @Override
    public Activity clone() {
        return new Swimming(this);
    }

    @Override
    public int calculateCalories(User u) {

        // User parameters
        double nutritionFactor = u.getCaloriesMultiplier() / 100.0;

        double weightFactor = 1 + ((double) u.getWeight() / User.MAX_WEIGHT);
        double heightFactor = 1 + ((double) u.getHeight() / User.MAX_HEIGHT);
        double weightHeightFactor = weightFactor + heightFactor;
        double bpmFactor = 1 + ((double) u.getHeartRate() / User.MAX_HEART_RATE);
        double met = MET_VALUE * (weightHeightFactor + bpmFactor);

        // Activity parameters
        double intensity = 1 + (this.getIntensity() / 100.0);
        int distance = this.getDistance();

        return (int) (
            met * nutritionFactor *
            intensity *
            (distance / 1000.0)
        );
    }

    @Override
    public boolean isDistanceBased() {
        return true;
    }

    @Override
    public boolean isAltimetryBased() {
        return false;
    }

    @Override
    public ArrayList<String> getAttributes() {
        ArrayList<String> atributes = new ArrayList<String>();
        atributes.add("distance");
        return atributes;
    }

    @Override
    public void setAttributes(ArrayList<Integer> attributes) {
        this.setDistance(attributes.get(0));
    }
}

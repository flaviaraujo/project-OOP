package src.activities;

import src.Activity;
import src.User;

import java.io.Serializable;
import java.util.ArrayList;

public class BTT extends Activity implements Serializable {

    private static final double MET_VALUE = 13.0;
    private static final int ALTIMETRY_FACTOR = 4;

    private int distance;
    private int altimetry;

    public BTT() {
        super();
        this.setDistance(0);
        this.setAltimetry(0);
    }

    public BTT(
        String name, int duration, int intensity,
        boolean hard, int calories
    ) {
        super(name, duration, intensity, hard, calories);
        this.setDistance(0);
        this.setAltimetry(0);
    }

    public BTT(
        String name, int duration, int intensity,
        boolean hard, int calories, int distance, int altimetry
    ) {
        super(name, duration, intensity, hard, calories);
        this.setDistance(distance);
        this.setAltimetry(altimetry);
    }

    public BTT(
        String name, int duration, int intensity,
        boolean hard, int calories, ArrayList<Integer> attributes
    ) {
        this(name, duration, intensity, hard, calories, attributes.get(0), attributes.get(1));
    }

    public BTT(BTT distanceAltimetry) {
        super(distanceAltimetry);
        this.setDistance(distanceAltimetry.getDistance());
        this.setAltimetry(distanceAltimetry.getAltimetry());
    }

    @Override
    public int getDistance() {
        return this.distance;
    }

    @Override
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

        BTT a = (BTT) o;
        return (
            this.getName().equals(a.getName()) &&
            this.getDuration() == a.getDuration() &&
            this.getIntensity() == a.getIntensity() &&
            this.getDistance() == a.getDistance() &&
            this.getAltimetry() == a.getAltimetry() &&
            this.getHard() == a.getHard()
            // The calories field is not considered in the equals method
            // since it varies depending on the user parameters
        );
    }

    @Override
    public Activity clone() {
        return new BTT(this);
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
        int altimetry = this.getAltimetry();

        return (int) (
            met * nutritionFactor *
            intensity *
            ((distance + altimetry * ALTIMETRY_FACTOR) / 1000.0)
        );
    }

    @Override
    public boolean isDistanceBased() {
        return true;
    }

    @Override
    public boolean isAltimetryBased() {
        return true;
    }

    @Override
    public ArrayList<String> getAttributes() {
        ArrayList<String> attributes = new ArrayList<String>();
        attributes.add("distance");
        attributes.add("altimetry");
        return attributes;
    }

    @Override
    public void setAttributes(ArrayList<Integer> attributes) {
        this.setDistance(attributes.get(0));
        this.setAltimetry(attributes.get(1));
    }
}

package src.activities;

import src.Activity;
import src.User;

import java.io.Serializable;
import java.util.ArrayList;

public class TrailRunning extends Activity implements Serializable {

    private static final double MET_VALUE = 10.0;
    private static final int ALTIMETRY_FACTOR = 4;

    private int distance;
    private int altimetry;

    public TrailRunning() {
        super();
        this.setDistance(0);
        this.setAltimetry(0);
    }

    public TrailRunning(
        String name, int duration, int intensity,
        boolean hard, int calories
    ) {
        super(name, duration, intensity, hard, calories);
        this.setDistance(0);
        this.setAltimetry(0);
    }

    public TrailRunning(
        String name, int duration, int intensity,
        boolean hard, int calories, int distance, int altimetry
    ) {
        super(name, duration, intensity, hard, calories);
        this.setDistance(distance);
        this.setAltimetry(altimetry);
    }

    public TrailRunning(
        String name, int duration, int intensity,
        boolean hard, int calories, ArrayList<Integer> attributes
    ) {
        this(name, duration, intensity, hard, calories, attributes.get(0), attributes.get(1));
    }

    public TrailRunning(TrailRunning distanceAltimetry) {
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

        TrailRunning a = (TrailRunning) o;
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
        return new TrailRunning(this);
    }

    @Override
    public int calculateCalories(User u) {

        // User parameters
        int nutritionMultiplier = u.getCaloriesMultiplier();
        double nutritionFactor = nutritionMultiplier / 100.0;

        int weight = u.getWeight();
        int height = u.getHeight();
        double weightFactor = Math.min((weight * 2) / User.MAX_WEIGHT, 0.6);
        double heightFactor = Math.min((height * 2) / User.MAX_HEIGHT, 0.6);
        double weightHeightFactor = weightFactor * heightFactor;

        int bpm = u.getHeartRate();
        double bpmFactor = (bpm / User.MAX_HEART_RATE) + 1;

        double met = MET_VALUE * ((weightHeightFactor + bpmFactor) / 2);

        // Activity parameters
        int altimetry = this.getAltimetry();
        int intensity = this.getIntensity();
        int distance = this.getDistance();

        return (int) (
            met * nutritionFactor *
            intensity / 100.0 *
            (distance + altimetry * ALTIMETRY_FACTOR) / 1000.0
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

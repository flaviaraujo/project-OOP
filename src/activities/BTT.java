package src.activities;

import src.Activity;
import src.User;

import java.io.Serializable;
import java.util.ArrayList;

public class BTT extends Activity implements Serializable {

    private static final double MET_VALUE = 14.0;
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

        int altimetry = this.getAltimetry();
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
        //     ((distance + altimetry * ALTIMETRY_FACTOR) / 1000.0));

        return (int)
            (weight * (nutritionMultiplier / 100.0) *
            MET_VALUE * (intensity / 100.0) * ((distance + (altimetry * ALTIMETRY_FACTOR)) / 1000.0));
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

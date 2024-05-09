package src.activities;

import src.Activity;
import src.User;

import java.io.Serializable;
import java.util.ArrayList;

public class Pilates extends Activity implements Serializable {

    private static final double MET_VALUE = 3.8;

    private int repetition;
    private int weight;

    public Pilates() {
        super();
        this.setRepetition(0);
        this.setWeight(0);
    }

    public Pilates(
        String name, int duration, int intensity,
        boolean hard, int calories
    ) {
        super(name, duration, intensity, hard, calories);
        this.setRepetition(0);
        this.setWeight(0);
    }

    public Pilates(
        String name, int duration, int intensity,
        boolean hard, int calories, int repetition, int weight
    ) {
        super(name, duration, intensity, hard, calories);
        this.setRepetition(repetition);
        this.setWeight(weight);
    }

    public Pilates(
        String name, int duration, int intensity,
        boolean hard, int calories, ArrayList<Integer> attributes
    ) {
        this(name, duration, intensity, hard, calories, attributes.get(0), attributes.get(1));
    }

    public Pilates(Pilates repetition) {
        super(repetition);
        this.setRepetition(repetition.getRepetition());
        this.setWeight(repetition.getWeight());
    }

    public int getRepetition() {
        return this.repetition;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Activity {\n");
        sb.append("  Name: " + this.getName() + ",\n");
        sb.append("  Duration: " + this.getDuration() + " minutes,\n");
        sb.append("  Intensity: " + this.getIntensity() + ",\n");
        sb.append("  Repetition: " + this.getRepetition() + " times\n");
        sb.append("  Weight: " + this.getWeight() + " kg\n");
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

        Pilates a = (Pilates) o;
        return (
            this.getName().equals(a.getName()) &&
            this.getDuration() == a.getDuration() &&
            this.getIntensity() == a.getIntensity() &&
            this.getRepetition() == a.getRepetition() &&
            this.getWeight() == a.getWeight() &&
            this.getHard() == a.getHard()
            // The calories field is not considered in the equals method
            // since it varies depending on the user parameters
        );
    }

    @Override
    public Activity clone() {
        return new Pilates(this);
    }

    @Override
    public int calculateCalories(User u) {

        int repetitions = this.getRepetition();
        int repetitionWeight = this.getWeight();
        int intensity = this.getIntensity();
        int duration = this.getDuration();
        int weight = u.getWeight();
        int height = u.getHeight();
        int nutritionMultiplier = u.getCaloriesMultiplier();
        double restingBPM = u.getHeartRate();
        double met = MET_VALUE;

        double weightFactor = Math.min(weight / 200.0, 2);
        weightFactor = Math.max(weightFactor, 1);

       double repWeightRatio = Math.min(1 + ((double) repetitionWeight / (double) weight), 2);

        // return (int)
        //     (weightFactor * (height / 100.0) * (nutritionMultiplier / 100.0) *
        //     met * (intensity / 100.0) * duration *
        //     repetitions * repWeightRatio);

        met += (restingBPM - 60) / 10.0 * 0.1;

        return (int)
        (met * weight * (((repetitions / 10.0) * (repetitionWeight/10.0))/repWeightRatio) *
        (intensity / 100.0) * (nutritionMultiplier / 100.0));
    }

    @Override
    public int getDistance() {
        return 0;
    }

    @Override
    public int getAltimetry() {
        return 0;
    }

    @Override
    public boolean isDistanceBased() {
        return false;
    }

    @Override
    public boolean isAltimetryBased() {
        return false;
    }

    @Override
    public ArrayList<String> getAttributes() {
        ArrayList<String> attributes = new ArrayList<String>();
        attributes.add("repetition");
        attributes.add("weight");
        return attributes;
    }

    @Override
    public void setAttributes(ArrayList<Integer> attributes) {
        this.setRepetition(attributes.get(0));
        this.setWeight(attributes.get(1));
    }
}


package src.activities;

import src.Activity;
import src.User;

// TODO remove this
import src.Controller;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public class RepetitionWeight extends Activity implements Serializable {

    private static final int ACTIVITY_TYPE = 4;
    private static final double MET_VALUE = 5.0;

    private int repetition;
    private int weight;

    public RepetitionWeight() {
        super();
        this.repetition = 0;
        this.weight = 0;
    }

    public RepetitionWeight(
        String name, int duration, int intensity,
        boolean hard, int calories
    ) {
        super(name, duration, intensity, hard, calories);
        this.repetition = 0;
        this.weight = 0;
    }

    public RepetitionWeight(
        String name, int duration, int intensity,
        boolean hard, int calories, int repetition, int weight
    ) {
        super(name, duration, intensity, hard, calories);
        this.repetition = repetition;
        this.weight = weight;
    }

    public RepetitionWeight(RepetitionWeight repetitionWeight) {
        super(repetitionWeight);
        this.repetition = repetitionWeight.getRepetition();
        this.weight = repetitionWeight.getWeight();
    }

    @Override
    public final int getACTIVITY_TYPE() {
        return ACTIVITY_TYPE;
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
        sb.append("  Name: " + this.name + ",\n");
        sb.append("  Duration: " + this.duration + " minutes,\n");
        sb.append("  Intensity: " + this.intensity + ",\n");
        sb.append("  Repetition: " + this.repetition + " times,\n");
        sb.append("  Weight: " + this.weight + " kg\n");
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

        RepetitionWeight repetitionWeight = (RepetitionWeight) o;
        return (
            this.name.equals(repetitionWeight.getName()) &&
            this.duration == repetitionWeight.getDuration() &&
            this.intensity == repetitionWeight.getIntensity() &&
            this.repetition == repetitionWeight.getRepetition() &&
            this.weight == repetitionWeight.getWeight() &&
            this.hard == repetitionWeight.getHard()
            // this.calories == repetitionWeight.getCalories()
        );
    }

    @Override
    public RepetitionWeight clone() {
        return new RepetitionWeight(this);
    }

    @Override
    public int calculateCalories(User u) {

        int repetitions = this.getRepetition();
        int repetitionWeight = this.getWeight();
        int intensity = this.getIntensity();
        int duration = this.getDuration();
        int weight = u.getWeight();
        int height = u.getHeight();
        int nutritionMultiplier = u.getType().getNutritionMultiplier();
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
    public Activity create(Scanner sc, ArrayList<Activity> userActivities) {

        RepetitionWeight activity = (RepetitionWeight) super.createAux(sc, userActivities, ACTIVITY_TYPE);
        if (activity == null) {
            return null;
        }

        // TODO remove this
        Controller c = new Controller();

        int repetitions = 0;
        int weight = 0;

        while (true) {
            System.out.print("Enter the number of repetitions: ");
            repetitions = c.readInt(sc);

            // check if repetitions is between 1 and 1000
            if (repetitions < 1 || repetitions > 1000) {
                System.out.println("Repetitions must be between 1 and 1000.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter the weight of the activity in kg: ");
            weight = c.readInt(sc);

            // check if weight is between 1 and 300 kg
            if (weight < 1 || weight > 300) {
                System.out.println("Weight must be between 1 and 300 kg.");
                continue;
            }
            break;
        }

        activity.setRepetition(repetitions);
        activity.setWeight(weight);
        return activity.clone();
    }
}

package src.activityTypes;

import src.Activity;
import src.User;

// TODO remove this
import src.Controller;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public abstract class RepetitionWeight extends Activity implements Serializable {

    private static final double MET_VALUE = 5.0;

    private int repetition;
    private int weight;

    public RepetitionWeight() {
        super();
        this.setRepetition(0);
        this.setWeight(0);
    }

    public RepetitionWeight(
        String name, int duration, int intensity,
        boolean hard, int calories
    ) {
        super(name, duration, intensity, hard, calories);
        this.setRepetition(0);
        this.setWeight(0);
    }

    public RepetitionWeight(
        String name, int duration, int intensity,
        boolean hard, int calories, int repetition, int weight
    ) {
        super(name, duration, intensity, hard, calories);
        this.setRepetition(repetition);
        this.setWeight(weight);
    }

    public RepetitionWeight(RepetitionWeight repetitionWeight) {
        super(repetitionWeight);
        this.setRepetition(repetitionWeight.getRepetition());
        this.setWeight(repetitionWeight.getWeight());
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
        sb.append("  Repetition: " + this.getRepetition() + " times,\n");
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

        RepetitionWeight repetitionWeight = (RepetitionWeight) o;
        return (
            this.getName().equals(repetitionWeight.getName()) &&
            this.getDuration() == repetitionWeight.getDuration() &&
            this.getIntensity() == repetitionWeight.getIntensity() &&
            this.getRepetition() == repetitionWeight.getRepetition() &&
            this.getWeight() == repetitionWeight.getWeight() &&
            this.getHard() == repetitionWeight.getHard()
            // The calories field is not considered in the equals method
            // since it varies depending on the user parameters
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

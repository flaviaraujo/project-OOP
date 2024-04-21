package src.activities;

import src.Activity;
import src.User;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public class RepetitionWeight extends Activity implements Serializable {

    private static final int ACTIVITY_TYPE = 4;
    private static final double MET_VALUE = 6.0;

    private int repetition;
    private int weight;

    public RepetitionWeight() {
        super();
        this.repetition = 0;
        this.weight = 0;
    }

    public RepetitionWeight(String name, int duration, int intensity, boolean hard) {
        super(name, duration, intensity, hard);
        this.repetition = 0;
        this.weight = 0;
    }

    public RepetitionWeight(String name, int duration, int intensity, boolean hard, int repetition, int weight) {
        super(name, duration, intensity, hard);
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
            this.weight == repetitionWeight.getWeight()
        );
    }

    @Override
    public RepetitionWeight clone() {
        return new RepetitionWeight(this);
    }

    @Override
    public int caloriesBurned(User u) {
 
        int repetitions = this.getRepetition();
        int repetitionWeight = this.getWeight();
        int intensity = this.getIntensity();
        int weight = u.getWeight();
        int height = u.getHeight();
        int nutritionMultiplier = u.getType().getNutritionMultiplier();
        double met = MET_VALUE;

        return (int)
            (weight * (height / 100.0) * (nutritionMultiplier / 100.0) *
            met * (intensity / 100.0) *
            repetitions * repetitionWeight);
    }


    @Override
    public Activity create(Scanner sc, ArrayList<Activity> userActivities) {

        RepetitionWeight activity = (RepetitionWeight) super.createAux(sc, userActivities, ACTIVITY_TYPE);
        if (activity == null) {
            return null;
        }

        int repetitions = 0;
        int weight = 0;

        while (true) {
            System.out.print("Enter the number of repetitions: ");
            try {
                repetitions = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Repetitions must be an integer.");
                continue;
            }

            // check if repetitions is between 1 and 1000
            if (repetitions < 1 || repetitions > 1000) {
                System.out.println("Repetitions must be between 1 and 1000.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter the weight of the activity in kg: ");
            try {
                weight = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Weight must be an integer.");
                sc.nextLine();
                continue;
            }

            // check if weight is between 1 and 300 kg
            if (weight < 1 || weight > 300) {
                System.out.println("Weight must be between 1 and 300 kg.");
                sc.nextLine();
                continue;
            }
            break;
        }

        activity.setRepetition(repetitions);
        activity.setWeight(weight);
        return activity; // TODO clone
    }
}

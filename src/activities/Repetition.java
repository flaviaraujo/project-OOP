package src.activities;

import src.Activity;
import src.User;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public class Repetition extends Activity implements Serializable {

    private static final int ACTIVITY_TYPE = 3;
    private static final double MET_VALUE = 3.0;

    private int repetition;

    public Repetition() {
        super();
        this.repetition = 0;
    }

    public Repetition(
        String name, int duration, int intensity,
        boolean hard, int calories
    ) {
        super(name, duration, intensity, hard, calories);
        this.repetition = 0;
    }

    public Repetition(
        String name, int duration, int intensity,
        boolean hard, int calories, int repetition
    ) {
        super(name, duration, intensity, hard, calories);
        this.repetition = repetition;
    }

    public Repetition(Repetition repetition) {
        super(repetition);
        this.repetition = repetition.getRepetition();
    }

    @Override
    public final int getACTIVITY_TYPE() {
        return ACTIVITY_TYPE;
    }

    public int getRepetition() {
        return this.repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Activity {\n");
        sb.append("  Name: " + this.name + ",\n");
        sb.append("  Duration: " + this.duration + " minutes,\n");
        sb.append("  Intensity: " + this.intensity + ",\n");
        sb.append("  Repetition: " + this.repetition + " times\n");
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

        Repetition repetition = (Repetition) o;
        return (
            this.name.equals(repetition.getName()) &&
            this.duration == repetition.getDuration() &&
            this.intensity == repetition.getIntensity() &&
            this.repetition == repetition.getRepetition() &&
            this.hard == repetition.getHard()
            // this.calories == repetition.getCalories()
        );
    }

    @Override
    public Repetition clone() {
        return new Repetition(this);
    }

    @Override
    public int calculateCalories(User u) {

        int repetitions = this.getRepetition();
        int intensity = this.getIntensity();
        int weight = u.getWeight();
        int nutritionMultiplier = u.getType().getNutritionMultiplier();
        double restingBPM = u.getHeartRate();
        double met = MET_VALUE;

        double weightFactor = Math.min(weight / 200.0, 2);
        weightFactor = Math.max(weightFactor, 1);

        met += (restingBPM - 60) / 10.0 * 0.1;

        return (int)
            (met * weight * (repetitions / 10.0) *
            (intensity / 100.0) * (nutritionMultiplier / 100.0));

    }

    @Override
    public Activity create(Scanner sc, ArrayList<Activity> userActivities) {

        Repetition activity = (Repetition) super.createAux(sc, userActivities, ACTIVITY_TYPE);
        if (activity == null) {
            return null;
        }

        int repetitions = 0;

        while (true) {
            System.out.print("Enter the number of repetitions: ");
            try {
                repetitions = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Repetitions must be an integer.");
                sc.nextLine();
                continue;
            }

            // check if repetitions is between 1 and 1000
            if (repetitions < 1 || repetitions > 1000) {
                System.out.println("Repetitions must be between 1 and 1000.");
                sc.nextLine();
                continue;
            }
            break;
        }

        activity.setRepetition(repetitions);
        return activity; // TODO clone
    }
}

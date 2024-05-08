package src.activityTypes;

import src.Activity;
import src.User;

// TODO remove this
import src.Controller;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public abstract class Repetition extends Activity implements Serializable {

    private static final double MET_VALUE = 3.0;

    private int repetition;

    public Repetition() {
        super();
        this.setRepetition(0);
    }

    public Repetition(
        String name, int duration, int intensity,
        boolean hard, int calories
    ) {
        super(name, duration, intensity, hard, calories);
        this.setRepetition(0);
    }

    public Repetition(
        String name, int duration, int intensity,
        boolean hard, int calories, int repetition
    ) {
        super(name, duration, intensity, hard, calories);
        this.setRepetition(repetition);
    }

    public Repetition(Repetition repetition) {
        super(repetition);
        this.setRepetition(repetition.getRepetition());
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
        sb.append("  Name: " + this.getName() + ",\n");
        sb.append("  Duration: " + this.getDuration() + " minutes,\n");
        sb.append("  Intensity: " + this.getIntensity() + ",\n");
        sb.append("  Repetition: " + this.getRepetition() + " times\n");
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

        Repetition repetition = (Repetition) o;
        return (
            this.getName().equals(repetition.getName()) &&
            this.getDuration() == repetition.getDuration() &&
            this.getIntensity() == repetition.getIntensity() &&
            this.getRepetition() == repetition.getRepetition() &&
            this.getHard() == repetition.getHard()
            // The calories field is not considered in the equals method
            // since it varies depending on the user parameters
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
        int nutritionMultiplier = u.getCaloriesMultiplier();
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

        // TODO remove this
        Controller c = new Controller();

        int repetitions = 0;

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

        activity.setRepetition(repetitions);
        return activity.clone();
    }
}

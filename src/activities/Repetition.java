package src.activities;

import src.Activity;
import src.User;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public class Repetition extends Activity implements Serializable {

    private static final int ACTIVITY_TYPE = 3;
    private static final double MET_VALUE = 8.0;

    private int repetition;

    public Repetition() {
        super();
        this.repetition = 0;
    }

    public Repetition(String name, int duration, int intensity, boolean hard) {
        super(name, duration, intensity, hard);
        this.repetition = 0;
    }

    public Repetition(String name, int duration, int intensity, boolean hard, int repetition) {
        super(name, duration, intensity, hard);
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
            this.repetition == repetition.getRepetition()
        );
    }

    @Override
    public Repetition clone() {
        return new Repetition(this);
    }

    @Override
    public int caloriesBurned(User u) {
        double weight = u.getWeight();
        int duration = this.getDuration();
        int intensity = this.getIntensity();
        int nutritionMultiplier = u.getType().getNutritionMultiplier();

        double met = MET_VALUE;

        double caloriesBurned = met * weight * (duration / 60.0);
        caloriesBurned *= ((intensity/100) * (nutritionMultiplier/100));
        return (int) caloriesBurned;
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
                continue;
            }

            // check if repetitions is between 1 and 1000
            if (repetitions < 1 || repetitions > 1000) {
                System.out.println("Repetitions must be between 1 and 1000.");
                continue;
            }
            break;
        }

        activity.setRepetition(repetitions);
        return activity; // TODO clone
    }
}

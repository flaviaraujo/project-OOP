package src.activities;

import src.Activity;
import src.User;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public class Repetition extends Activity implements Serializable {

    private static final int ACTIVITY_TYPE = 3;

    private int repetition;

    public Repetition() {
        super();
        this.repetition = 0;
    }

    public Repetition(String name, int duration, int intensity) {
        super(name, duration, intensity);
        this.repetition = 0;
    }

    public Repetition(String name, int duration, int intensity, int repetition) {
        super(name, duration, intensity);
        this.repetition = repetition;
    }

    public Repetition(Repetition repetition) {
        super(repetition);
        this.repetition = repetition.getRepetition();
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
        sb.append("Acivity {\n");
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
        // TODO Calculate calories burned based on user, repetition, duration, and intensity
        return 0;
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

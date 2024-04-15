package core;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Activity
 */
public abstract class Activity {

    private String name;
    private int duration; // in minutes
    private int intensity;

    public Activity() {
        this.duration = 0;
        this.intensity = 0;
    }

    public Activity(int duration, int intensity) {
        this.duration = duration;
        this.intensity = intensity;
    }

    public Activity(Activity activity) {
        this.duration = activity.getDuration();
        this.intensity = activity.getIntensity();
    }

    public int getDuration() {
        return this.duration;
    }

    public int getIntensity() {
        return this.intensity;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public abstract void toString(Activity a);

    public abstract void equals(Activity a);

    public abstract void clone(Activity a);

    public abstract int caloriesBurned(User u);

    public static Activity create(Scanner sc, ArrayList<Activity> userActivities) {

        while (true) {
            System.out.println();
            System.out.println("Enter the activity type:");
            System.out.println("(1) Distance");
            System.out.println("(2) Distance & altimetry");
            System.out.println("(3) Repetition");
            System.out.println("(4) Repetition with weights");
            System.out.print("Choice: ");

            int choice = 0;
            try {
                choice = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid choice. Please enter a number.");
                sc.nextLine(); // clear the buffer
                continue;
            }

            switch (choice) {
                case 1:
                    return Activities.Distance
                        .create(sc, userActivities);
                case 2:
                    return Activities.DistanceAltimetry
                        .create(sc, userActivities);
                case 3:
                    return Activities.Repetition
                        .create(sc, userActivities);
                case 4:
                    return Activities.RepetitionWeight
                        .create(sc, userActivities);
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }
}

package core;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Activity
 */
public abstract class Activity {

    protected String name;
    protected int duration; // in minutes
    protected int intensity;

    public Activity() {
        this.name = "";
        this.duration = 0;
        this.intensity = 0;
    }

    public Activity(String name, int duration, int intensity) {
        this.name = name;
        this.duration = duration;
        this.intensity = intensity;
    }

    public Activity(Activity activity) {
        this.name = activity.getName();
        this.duration = activity.getDuration();
        this.intensity = activity.getIntensity();
    }

    public String getName() {
        return this.name;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getIntensity() {
        return this.intensity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public abstract String toString();

    public abstract boolean equals(Object o);

    public abstract Activity clone();

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

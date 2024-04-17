package src;

import src.activities.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

/**
 * Activity
 */
public abstract class Activity implements Serializable {

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

    public boolean isDistance() {
        return this instanceof Distance;
    }

    public boolean isDistanceAltimetry() {
        return this instanceof DistanceAltimetry;
    }

    public boolean isRepetition() {
        return this instanceof Repetition;
    }

    public boolean isRepetitionWeight() {
        return this instanceof RepetitionWeight;
    }

    public abstract Activity create(Scanner sc, ArrayList<Activity> userActivities);

    public static Activity createAux(Scanner sc, ArrayList<Activity> userActivities, int activityType) {

        String name = "";
        int duration = 0;
        int intensity = 0;

        sc.nextLine(); // clear the buffer
        System.out.println();

        while (true) {
            System.out.print("Enter the name of the activity: ");
            String nameBuffer = sc.nextLine();

            // check if name is between 1 and 100 characters
            if (nameBuffer.length() < 1 || nameBuffer.length() > 100) {
                System.out.println("Activity name must be between 1 and 100 characters.");
                continue;
            }
            // check if name is unique
            boolean isUnique = userActivities.stream().noneMatch(a -> a.getName().equals(nameBuffer));
            if (!isUnique) {
                System.out.println("Activity name must be unique.");
                continue;
            }
            name = nameBuffer;
            break;
        }

        while (true) {
            System.out.print("Enter the duration of the activity in minutes: ");
            try {
                duration = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Duration must be an integer.");
                continue;
            }

            // check if duration is between 1 and 1440 minutes
            if (duration < 1 || duration > 1440) {
                System.out.println("Duration must be between 1 and 1440 minutes.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter the intensity of the activity (1-100): ");
            try {
                intensity = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Intensity must be an integer.");
                continue;
            }

            // check if intensity is between 1 and 100
            if (intensity < 1 || intensity > 100) {
                System.out.println("Intensity must be between 1 and 100.");
                continue;
            }
            break;
        }

        switch (activityType) {
            case 1:
                return new Distance(name, duration, intensity);
            case 2:
                return new DistanceAltimetry(name, duration, intensity);
            case 3:
                return new Repetition(name, duration, intensity);
            case 4:
                return new RepetitionWeight(name, duration, intensity);
            default:
                return null;
        }
    }

    public static Activity createMenu(Scanner sc, ArrayList<Activity> userActivities) {

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
                    Distance distance = new Distance();
                    return distance
                        .create(sc, userActivities);
                case 2:
                    DistanceAltimetry distanceAltimetry = new DistanceAltimetry();
                    return distanceAltimetry
                        .create(sc, userActivities);
                case 3:
                    Repetition repetition = new Repetition();
                    return repetition
                        .create(sc, userActivities);
                case 4:
                    RepetitionWeight repetitionWeight = new RepetitionWeight();
                    return repetitionWeight
                        .create(sc, userActivities);
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }

    public static Activity searchActivity(Scanner sc, ArrayList<Activity> userActivities) {

        sc.nextLine(); // clear the buffer

        System.out.println();
        System.out.print("Enter the name of the activity: ");
        String name = sc.nextLine();

        Activity activity = userActivities.stream()
            .filter(a -> a.getName().equals(name))
            .findFirst()
            .orElse(null);

        return activity;
    }
}


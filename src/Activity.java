package src;

import src.activities.Distance;
import src.activities.DistanceAltimetry;
import src.activities.Repetition;
import src.activities.RepetitionWeight;

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
    protected boolean hard;

    public Activity() {
        this.name = "";
        this.duration = 0;
        this.intensity = 0;
        this.hard = false;
    }

    public Activity(String name, int duration, int intensity, boolean hard) {
        this.name = name;
        this.duration = duration;
        this.intensity = intensity;
        this.hard = hard;
    }

    public Activity(Activity activity) {
        this.name = activity.getName();
        this.duration = activity.getDuration();
        this.intensity = activity.getIntensity();
        this.hard = activity.isHard();
    }

    public abstract int getACTIVITY_TYPE();

    public String getName() {
        return this.name;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getIntensity() {
        return this.intensity;
    }

    public boolean isHard() {
        return this.hard;
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

    public void setHard(boolean hard) {
        this.hard = hard;
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

    public int getType() {
        if (this.isDistance()) {
            Distance distance = (Distance) this;
            return distance.getACTIVITY_TYPE();
        }
        else if (this.isDistanceAltimetry()) {
            DistanceAltimetry distanceAltimetry = (DistanceAltimetry) this;
            return distanceAltimetry.getACTIVITY_TYPE();
        }
        else if (this.isRepetition()) {
            Repetition repetition = (Repetition) this;
            return repetition.getACTIVITY_TYPE();
        }
        else if (this.isRepetitionWeight()) {
            RepetitionWeight repetitionWeight = (RepetitionWeight) this;
            return repetitionWeight.getACTIVITY_TYPE();
        }
        else {
            return 0;
        }
    }

    public String getType(int type) {
        switch (type) {
            case 1:
                return "Distance";
            case 2:
                return "Distance & altimetry";
            case 3:
                return "Repetition";
            case 4:
                return "Repetition with weights";
            default:
                return "";
        }
    }

    public abstract Activity create(Scanner sc, ArrayList<Activity> userActivities);

    public static Activity createAux(Scanner sc, ArrayList<Activity> userActivities, int activityType) {

        String name = "";
        int duration = 0;
        int intensity = 0;
        boolean hard = false;

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

        while (true) {
            System.out.print("Is the activity hard? (y/n): ");
            String hardBuffer = "";
            try {
                hardBuffer = sc.nextLine();
            }
            catch (Exception e) {
                System.out.println("Invalid choice. Please enter 'y' or 'n'.");
                continue;
            }

            if (hardBuffer.equals("y")) {
                hard = true;
                break;
            } else if (hardBuffer.equals("n")) {
                hard = false;
                break;
            } else {
                System.out.println("Invalid choice. Please enter 'y' or 'n'.");
            }
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

        // Print activities names
        System.out.println("User activities:");
        for (Activity a : userActivities) {
            System.out.println("  -> " + a.getName());
        }
        System.out.print("Enter the name of the activity: ");
        String name = sc.next();

        Activity activity = userActivities.stream()
            .filter(a -> a.getName().equals(name))
            .findFirst()
            .orElse(null);

        return activity;
    }
}


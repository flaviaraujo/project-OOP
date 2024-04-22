package src;

import src.activities.Distance;
import src.activities.DistanceAltimetry;
import src.activities.Repetition;
import src.activities.RepetitionWeight;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Activity
 */
public abstract class Activity implements Serializable {

    protected String name;
    protected int duration; // in minutes
    protected int intensity;
    protected boolean hard;
    protected int calories; // calories burned

    public Activity() {
        this.name = "";
        this.duration = 0;
        this.intensity = 0;
        this.hard = false;
        this.calories = 0;
    }

    public Activity(
        String name, int duration, int intensity,
        boolean hard, int calories
    ) {
        this.name = name;
        this.duration = duration;
        this.intensity = intensity;
        this.hard = hard;
        this.calories = calories;
    }

    public Activity(Activity activity) {
        this.name = activity.getName();
        this.duration = activity.getDuration();
        this.intensity = activity.getIntensity();
        this.hard = activity.getHard();
        this.calories = activity.getCalories();
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

    public boolean getHard() {
        return this.hard;
    }

    public int getCalories() {
        return this.calories;
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

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public abstract String toString();

    public abstract boolean equals(Object o);

    public abstract Activity clone();

    public abstract int calculateCalories(User u);

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

    public String getTypeString(int type) {
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

        IO io = new IO();

        System.out.println();

        while (true) {
            System.out.print("Enter the name of the activity: ");
            String nameBuffer = io.readString(sc);

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
            duration = io.readInt(sc);

            // check if duration is between 1 and 1440 minutes
            if (duration < 1 || duration > 1440) {
                System.out.println("Duration must be between 1 and 1440 minutes.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter the intensity of the activity (1-100): ");
            intensity = io.readInt(sc);

            // check if intensity is between 1 and 100
            if (intensity < 1 || intensity > 100) {
                System.out.println("Intensity must be between 1 and 100.");
                continue;
            }
            break;
        }

        System.out.print("Is the activity hard? [y/n]: ");
        String hardBuffer = io.readYesNo(sc);
        hard = hardBuffer.equals("y");

        switch (activityType) {
            case 1:
                return new Distance(name, duration, intensity, hard, 0);
            case 2:
                return new DistanceAltimetry(name, duration, intensity, hard, 0);
            case 3:
                return new Repetition(name, duration, intensity, hard, 0);
            case 4:
                return new RepetitionWeight(name, duration, intensity, hard, 0);
            default:
                return null;
        }
    }

    public static Activity createMenu(Scanner sc, ArrayList<Activity> userActivities) {

        IO io = new IO();

        while (true) {
            System.out.println();
            System.out.println("Enter the activity type:");
            System.out.println("(1) Distance");
            System.out.println("(2) Distance & altimetry");
            System.out.println("(3) Repetition");
            System.out.println("(4) Repetition with weights");
            System.out.print("Choice: ");

            int choice = io.readInt(sc);

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
                    continue;
            }
        }
    }

    public static Activity search(Scanner sc, ArrayList<Activity> userActivities) {

        IO io = new IO();
        Activity activity = null;
        while (true) {
            // Print activities names
            System.out.println("User activities:");
            for (Activity a : userActivities) {
                System.out.println("  -> " + a.getName());
            }
            System.out.print("Enter the name of the activity: ");
            String name = io.readString(sc);

            activity = userActivities.stream()
                .filter(a -> a.getName().equals(name))
                .findFirst()
                .orElse(null);

            if (activity == null) {
                System.out.println("Activity not found.");
                continue;
            }

            return activity;
        }
    }

    public static User register(Scanner sc, User u) {

        Activity a = null;
        IO io = new IO();

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("(1) Register an existing activity");
            System.out.println("(2) Register a new activity");
            System.out.print("Option: ");

            int option = io.readInt(sc);

            ArrayList<Activity> userActivities = u.getActivities();
            switch (option) {
                case 1:
                    if (userActivities.isEmpty()) {
                        System.out.println("No activities found.");
                        System.out.println("Please register a new activity.");
                        continue;
                    }
                    a = search(sc, userActivities);
                    u.addActivity(a);
                    break;
                case 2:
                    a = createMenu(sc, userActivities);
                    u.addActivity(a);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    continue;
            }
            break;
        }

        // Enter date manually or use current date
        System.out.println("");
        System.out.println("Registering activity: " + a.getName());
        System.out.println("(1) Enter date manually");
        System.out.println("(2) Use current date");
        System.out.print("Option: ");

        int dateOption = io.readInt(sc);

        LocalDateTime datetime = LocalDateTime.now();

        if (dateOption == 1) {
            while (true) {
                System.out.print("Enter date (yyyy-mm-dd): ");
                String date = io.readString(sc);

                System.out.print("Enter time (hh:mm): ");
                String time = io.readString(sc);

                try {
                    datetime = LocalDateTime.parse(date + "T" + time);
                }
                catch (DateTimeParseException e) {
                    System.out.println("Invalid datetime format.");
                    continue;
                }

                if (!datetime.isBefore(LocalDateTime.now())) {
                    System.out.println("Date must be less than current date.");
                    continue;
                }
                break;
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Create a new register
        Activity register = a.clone();

        int calories = register.calculateCalories(u);
        register.setCalories(calories);

        // Register an activity in the user
        u.register(datetime, register);

        // Print the registered activity
        System.out.println("Activity registered successfully: ");
        System.out.println(register.getName() + " on " + datetime.format(formatter));
        System.out.println(register.getCalories() + " calories burned.");

        return u;
    }
}

package src;

import java.io.Serializable;
import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event implements Serializable {

    private static final int MAX_REPETITIONS = 3;

    private Activity activity;
    private int activityRepetitions; // (1-max_repetitions)
    private int day; // week day (1-7)
    private LocalTime time;

    public Event() {
        this.activity = null;
        this.activityRepetitions = 1;
        this.day = 1;
        this.time = null;
    }

    public Event(
        Activity activity, int activityRepetitions, int day,
        LocalTime time
    ) {
        this.activity = activity.clone();
        this.activityRepetitions = activityRepetitions;
        this.day = day;
        this.time = time;
    }

    public Event(Event event) {
        this.activity = event.getActivity();
        this.activityRepetitions = event.getActivityRepetitions();
        this.day = event.getDay();
        this.time = event.getTime();
    }

    public final int getMAX_REPETITIONS() {
        return MAX_REPETITIONS;
    }

    public Activity getActivity() {
        return this.activity.clone();
    }

    public int getActivityRepetitions() {
        return this.activityRepetitions;
    }

    public int getDay() {
        return this.day;
    }

    public LocalTime getTime() {
        return this.time;
    }

    public void setActivity(Activity activity) {
        this.activity = activity.clone();
    }

    public void setActivityRepetitions(int activityRepetitions) {
        this.activityRepetitions = activityRepetitions;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Activity: " + this.activity.getName() + "\n");
        sb.append("Repetitions: " + this.activityRepetitions + "\n");
        sb.append("Day: " + convertDayToString(this.day) + "\n");
        sb.append("Time: " + convertTimeToString(this.time));
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Event e = (Event) o;
        return (
            this.activity.equals(e.getActivity()) &&
            this.activityRepetitions == e.getActivityRepetitions() &&
            this.day == e.getDay() &&
            this.time.equals(e.getTime())
        );
    }

    public Event clone() {
        return new Event(this);
    }

    public String convertDayToString(int day) {
        switch (day) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "Invalid day";
        }
    }

    public String convertTimeToString(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    public boolean isValidDay(int day) {
        return day >= 1 && day <= 7;
    }

    public Event create(Scanner sc, ArrayList<Activity> userActivities, int maxRepetitions, int day) {

        if (userActivities.size() == 0) {
            System.out.println("You have no activities to schedule an event for.");
            return null;
        }

        Activity activity;
        while (true) {
            activity = Activity.search(sc, userActivities);
            break;
        }

        IO io = new IO();

        int activityRepetitions = 1;
        if (maxRepetitions != 1) {
            while (true) {
                System.out.print("Enter the number of times you want to repeat " +
                    "the activity (1-" + maxRepetitions + "): ");
                activityRepetitions = io.readInt(sc);

                if (activityRepetitions < 1 || activityRepetitions > maxRepetitions) {
                    System.out.println("Invalid number of repetitions. Please enter a number " +
                        "between 1 and " + maxRepetitions + ".");
                    continue;
                }
                break;
            }
        }

        LocalTime time;
        while (true) {
            System.out.print("Enter the time of the event (HH:mm): ");
            String timeBuffer = io.readString(sc);
            try {
                time = LocalTime.parse(timeBuffer);
            }
            catch (DateTimeParseException e) {
                System.out.println("Invalid time format.");
                continue;
            }
            break;
        }

        Event event = new Event(activity, activityRepetitions, day, time);
        System.out.println("Event scheduled successfully.");
        return event.clone();
    }
}

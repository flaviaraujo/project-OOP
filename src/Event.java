package src;

import java.io.Serializable;
import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Event implements Serializable {
    private Activity activity;
    private int day;
    private LocalDateTime eventDateTime;

    public Event() {
        this.activity = null;
        this.day = 1;
        this.eventDateTime = null;
    }

    public Event(Activity activity, int day, LocalDateTime eventDateTime) {
        this.activity = activity.clone();
        this.day = day;
        this.eventDateTime = eventDateTime;
    }

    public Event(Event event) {
        this.activity = event.getActivity();
        this.day = event.getDay();
        this.eventDateTime = event.getEventDateTime();
    }

    public Activity getActivity() {
        return this.activity.clone();
    }

    public int getDay() {
        return this.day;
    }

    public LocalDateTime getEventDateTime() {
        return this.eventDateTime;
    }

    public void setActivity(Activity activity) {
        this.activity = activity.clone();
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setEventDateTime(LocalDateTime eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getDayString() {
        switch (this.day) {
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

    public String getHourString() {
        return String.format("%02d:00", this.eventDateTime.getHour());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Activity: " + this.activity.getName() + "\n");
        sb.append("Day: " + getDayString() + "\n");
        sb.append("Hour: " + getHourString() + "\n");
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
        return this.activity.equals(e.getActivity()) && this.day == e.getDay() && this.eventDateTime.equals(e.getEventDateTime());
    }

    public Event clone() {
        return new Event(this);
    }

    public boolean isValidDay(int day) {
        return day >= 1 && day <= 7;
    }

    public boolean isValidHour(int hour) {
        return hour >= 0 && hour <= 23;
    }

    // Read the event details from the user
    public void readEvent(Scanner sc, ArrayList<Activity> userActivities) {
        System.out.println("Enter the day of the week (1-7): ");
        int day = sc.nextInt();
        while (!isValidDay(day)) {
            System.out.println("Invalid day. Please enter a valid day of the week (1-7): ");
            day = sc.nextInt();
        }

        System.out.println("Enter the hour of the day (0-23): ");
        int hour = sc.nextInt();
        while (!isValidHour(hour)) {
            System.out.println("Invalid hour. Please enter a valid hour of the day (0-23): ");
            hour = sc.nextInt();
        }

        LocalDateTime eventDateTime = LocalDateTime.of(2024, 1, day, hour, 0); // Assuming year 2024 for simplicity

        sc.nextLine(); // Clear the buffer

        Activity activity = Activity.searchActivity(sc, userActivities);
        if (activity == null) {
            System.out.println("Activity not found.");
        } else {
            System.out.println("You have an event scheduled for: " + activity.getName() + " on " + getDayString() + " at " + getHourString() + ".\n");
            this.activity = activity;
            this.day = day;
            this.eventDateTime = eventDateTime;
        }
    }
}

package src;

import java.io.Serializable;
import java.time.LocalTime;

public class Event implements Serializable {

    public static final int MAX_REPETITIONS = 3;

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
        sb.append("Day: " + Controller.convertDayToString(this.day) + "\n");
        sb.append("Time: " + Controller.convertTimeToString(this.time));
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

    public boolean isValidDay(int day) {
        return day >= 1 && day <= 7;
    }
}

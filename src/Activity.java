package src;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Activity implements Serializable {

    private String name;
    private int duration; // in minutes
    private int intensity;
    private boolean hard;
    private int calories; // calories burned

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

    public abstract int getDistance();

    public abstract int getAltimetry();

    public abstract boolean isDistanceBased();

    public abstract boolean isAltimetryBased();

    public abstract ArrayList<String> getAttributes();

    public abstract void setAttributes(ArrayList<Integer> attributes);
}

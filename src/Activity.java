/**
 * Activity
 */
public abstract class Activity {

    protected int duration; // in minutes
    protected int intensity;

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

    public abstract int caloriesBurned(User u);

    public abstract String toString();

    public abstract boolean equals(Object o);

    public abstract Activity clone();
}
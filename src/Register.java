package src;

import java.io.Serializable;

/**
 * Register
 */
public class Register implements Serializable {

    private Activity activity;
    private int caloriesBurned;
    // private int distance; // in meters
    // private int altimetry; // in meters

    public Register() {
        this.activity = null;
        this.caloriesBurned = 0;
    }

    public Register(Activity activity, User user) {
        this.activity = activity.clone();
        this.caloriesBurned = activity.caloriesBurned(user);
    }

    public Register(Activity activity, int caloriesBurned) {
        this.activity = activity.clone();
        this.caloriesBurned = caloriesBurned;
    }

    public Register(Register register) {
        this.activity = register.getActivity();
        this.caloriesBurned = register.getCaloriesBurned();
    }

    public Activity getActivity() {
        return this.activity.clone();
    }

    public int getCaloriesBurned() {
        return this.caloriesBurned;
    }

    public void setActivity(Activity activity) {
        this.activity = activity.clone();
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.activity.toString());
        sb.append("\nCalories burned: " + this.caloriesBurned);
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o == this) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        Register r = (Register) o;
        return (
            this.activity.equals(r.getActivity()) &&
            this.caloriesBurned == r.getCaloriesBurned()
        );
    }

    public Register clone() {
        return new Register(this.activity, this.caloriesBurned);
    }
}

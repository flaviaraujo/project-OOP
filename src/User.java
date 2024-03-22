import java.util.ArrayList;
import java.util.Scanner;

/**
 * User
 * TODO: Dúvida em agregação é necessário clone de String's?
 */
public class User {

    public enum UserType {
        // OLYMPIC(98),
        PROFESSIONAL(90),
        AMATEUR(60),
        OCCASIONAL(40);

        private int nutritionMultiplier;

        UserType(int nutritionMultiplier) {
            this.nutritionMultiplier = nutritionMultiplier;
        }

        public int getNutritionMultiplier() {
            return nutritionMultiplier;
        }
    }

    private String name;
    private String email;
    private String address;
    private int heartRate; // BPM
    private UserType userType;
    private ArrayList<Activity> activities;

    /* Default Constructor */
    public User() {
        this.name = "";
        this.email = "";
        this.address = "";
        this.heartRate = 0;
        this.userType = UserType.OCCASIONAL;
        this.activities = new ArrayList<Activity>();
    }

    /* Parameterized Constructor */
    public User(
        String name,
        String email,
        String address,
        int heartRate,
        UserType userType,
        ArrayList<Activity> activities
    ) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.heartRate = heartRate;
        this.userType = userType;
        this.activities = activities;
    }

    /* Copy Constructor */
    public User(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.heartRate = user.getHeartRate();
        this.userType = user.getUserType();
        this.activities = user.getActivities(); // TODO does this mehod should be in the Activity class instead?
    }

    /* Instance Methods - Getters */
    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getAddress() {
        return this.address;
    }

    public int getHeartRate() {
        return this.heartRate;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public ArrayList<Activity> getActivities() {
        ArrayList<Activity> activities = new ArrayList<Activity>();
        for (Activity activity : this.activities) {
            // activities.add(new Activity(activity)); // TODO clone?
        }
        return activities;
    }

    /* Instance Methods - Setters */
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = new ArrayList<Activity>(); // TODO Duvida does this clear the array?
        for (Activity activity : activities) {
            // this.activities.add(new Activity(activity)); // TODO clone?
        }
    }

    /* Object methods */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("User {\n");
        sb.append("  name: " + this.name + ",\n");
        sb.append("  email: " + this.email + ",\n");
        sb.append("  address: " + this.address + ",\n");
        sb.append("  heartRate: " + this.heartRate + " BPM,\n");
        sb.append("  userType: " + this.userType + ",\n");
        sb.append("  activities: [\n");
        for (Activity activity : this.activities) {
            sb.append("    " + activity.toString() + ",\n");
        }
        sb.append("  ]\n}");
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o == this) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        User user = (User) o;
        return (
            user.getName().equals(this.name) &&
            user.getEmail().equals(this.email) &&
            user.getAddress().equals(this.address) &&
            user.getHeartRate() == this.heartRate &&
            user.getUserType() == this.userType &&
            user.getActivities().equals(this.activities)
        );
    }

    public User clone() {
        return new User(this);
    }

    public User createUser(Scanner sc) {
        // TODO
        return new User();
    }
}

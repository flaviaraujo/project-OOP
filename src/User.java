package src;

import src.exceptions.ActivityIsRegisteredException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class User implements Serializable {

    public static final int MIN_NAME_LENGTH = 3;
    public static final int MAX_NAME_LENGTH = 256;

    public static final int MAX_EMAIL_LENGTH = 320;

    public static final int MIN_ADDRESS_LENGTH = 5;
    public static final int MAX_ADDRESS_LENGTH = 1024;

    public static final int MIN_HEART_RATE = 20;
    public static final int MAX_HEART_RATE = 200;

    public static final int MIN_WEIGHT = 20;
    public static final int MAX_WEIGHT = 200;

    public static final int MIN_HEIGHT = 100;
    public static final int MAX_HEIGHT = 220;

    private int id;
    private String name;
    private String email;
    private String address;
    private int heartRate; // BPM
    private int weight; // kg
    private int height; // cm
    private ArrayList<Activity> activities;
    private HashMap<LocalDateTime, Activity> registers; // alternative LinkedHashMap
    private Plan plan;
 
    /* Default Constructor */
    public User() {
        this.id = 0;
        this.name = "";
        this.email = "";
        this.address = "";
        this.heartRate = 0;
        this.weight = 0;
        this.height = 0;
        this.activities = new ArrayList<Activity>();
        this.registers = new HashMap<LocalDateTime, Activity>();
        this.plan = new Plan();
    }

    /* Parameterized Constructors */
    public User(
        int id, String name, String email,
        String address, int heartRate, int weight,
        int height
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.heartRate = heartRate;
        this.weight = weight;
        this.height = height;
        this.activities = new ArrayList<Activity>();
        this.registers = new HashMap<LocalDateTime, Activity>();
        this.plan = null;
    }

    public User(
        int id, String name, String email,
        String address, int heartRate, int weight,
        int height,
        ArrayList<Activity> activities,
        HashMap<LocalDateTime, Activity> registers,
        Plan plan
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.heartRate = heartRate;
        this.weight = weight;
        this.height = height;
        this.activities = new ArrayList<>();
        for (Activity activity : activities) {
            this.activities.add(activity.clone());
        }
        this.registers = new HashMap<>();
        for (Entry<LocalDateTime, Activity> entry : registers.entrySet()) {
            this.registers.put(entry.getKey(), entry.getValue().clone());
        }
        this.plan = plan.clone();
    }

    /* Copy Constructor */
    public User(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.heartRate = user.getHeartRate();
        this.weight = user.getWeight();
        this.height = user.getHeight();
        this.activities = user.getActivities();
        this.registers = user.getRegisters();
        this.plan = user.getPlan();
    }

    /* Instance Methods - Getters */
    public int getId() {
        return this.id;
    }

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

    public int getWeight() {
        return this.weight;
    }

    public int getHeight() {
        return this.height;
    }

    public abstract int getCaloriesMultiplier();

    public ArrayList<Activity> getActivities() {
        ArrayList<Activity> activities = new ArrayList<Activity>();

        for (Activity activity : this.activities) {
            activities.add(activity.clone());
        }
        return activities;
    }

    public HashMap<LocalDateTime, Activity> getRegisters() {
        HashMap<LocalDateTime, Activity> registers =
            new HashMap<LocalDateTime, Activity>();

        for (LocalDateTime date : this.registers.keySet()) {
            registers.put(date, this.registers.get(date).clone());
        }
        return registers;
    }

    public Plan getPlan() {
        if (this.plan == null) {
            return null;
        }
        return this.plan.clone();
    }

    /* Instance Methods - Setters */
    public void setId(int id) {
        this.id = id;
    }

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

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = new ArrayList<Activity>();

        for (Activity activity : activities) {
            this.activities.add(activity.clone());
        }
    }

    public void setRegisters(HashMap<LocalDateTime, Activity> registers) {
        HashMap<LocalDateTime, Activity> tmpRegisters =
            new HashMap<LocalDateTime, Activity>();

        for (LocalDateTime date : registers.keySet()) {
            tmpRegisters.put(date, registers.get(date).clone());
        }
        this.registers = tmpRegisters;
    }

    public void setPlan(Plan plan) {
        if (plan == null) {
            this.plan = null;
            return;
        }
        this.plan = plan.clone();
    }

    /* Object methods */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("User {\n");
        sb.append("  id: " + this.id + ",\n");
        sb.append("  name: " + this.name + ",\n");
        sb.append("  email: " + this.email + ",\n");
        sb.append("  address: " + this.address + ",\n");
        sb.append("  heartRate: " + this.heartRate + " BPM,\n");
        sb.append("  weight: " + this.weight + " kg,\n");
        sb.append("  height: " + this.height + " cm,\n");
        sb.append("  type: " + this.getClass().getSimpleName() + ",\n");
        sb.append("  caloriesMultiplier: " + this.getCaloriesMultiplier() + ",\n");

        sb.append("  activities: [");
        for (Activity a : this.activities) {
            sb.append("\n    " + a.getName() + ",");
        }
        if (!this.activities.isEmpty()) {
            sb.setLength(sb.length() - 1); // Remove the trailing comma
            sb.append("\n  "); // Add indentation for the closing bracket
        }
        sb.append("],\n");

        // Display registers sorted by date
        List<LocalDateTime> sortedDates = new ArrayList<>(this.registers.keySet());
        Collections.sort(sortedDates);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        sb.append("  registers: [");
        for (LocalDateTime date : sortedDates) {
            sb.append("\n    " + date.format(formatter) + " - " + this.registers.get(date).getName() + ",");
        }
        if (!sortedDates.isEmpty()) {
            sb.setLength(sb.length() - 1); // Remove the trailing comma
            sb.append("\n  "); // Add indentation for the closing bracket
        }
        sb.append("],\n");

        if (this.plan != null) {
            sb.append("  plan: " + this.plan.getName() + "\n");
        }
        else {
            sb.append("  no training plan\n");
        }

        sb.append("}");
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o == this) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        User user = (User) o;
        return (
            user.getId() == this.id &&
            user.getName().equals(this.name) &&
            user.getEmail().equals(this.email) &&
            user.getAddress().equals(this.address) &&
            user.getHeartRate() == this.heartRate &&
            user.getWeight() == this.weight &&
            user.getHeight() == this.height &&
            user.getCaloriesMultiplier() == this.getCaloriesMultiplier() &&
            user.getActivities().equals(this.activities) &&
            user.getRegisters().equals(this.registers) &&
            user.getPlan().equals(this.plan)
        );
    }

    public abstract User clone();

    // Activities methods
    public void addActivity(Activity activity) {
        this.activities.add(activity.clone());
    }

    public void deleteActivity(Activity activity) throws ActivityIsRegisteredException {

        if (this.registers.containsValue(activity)) {
            throw new ActivityIsRegisteredException();
        }

        this.activities.remove(activity);
    }

    // Registers methods
    public void register(LocalDateTime date, Activity register) {
        this.registers.put(date, register.clone());
    }

    public void viewRegisters() {
        // Display registers sorted by date
        List<LocalDateTime> sortedDates = new ArrayList<>(this.registers.keySet());
        Collections.sort(sortedDates);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (LocalDateTime date : sortedDates) {
            System.out.println(date.format(formatter) + ": {");
            System.out.println(this.registers.get(date));
            System.out.println("}");
        }
    }
}

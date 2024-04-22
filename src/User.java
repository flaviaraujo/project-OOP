package src;

import src.exceptions.UserNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class User implements Serializable {

    public enum Type implements Serializable {
        OCCASIONAL(40),
        AMATEUR(60),
        PROFESSIONAL(90);
        // OLYMPIC(98);

        private int nutritionMultiplier;

        Type(int nutritionMultiplier) {
            this.nutritionMultiplier = nutritionMultiplier;
        }

        public int getNutritionMultiplier() {
            return nutritionMultiplier;
        }
    }

    private int id;
    private String name;
    private String email;
    private String address;
    private int heartRate; // BPM
    private int weight; // kg
    private int height; // cm
    private Type type;
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
        this.type = Type.OCCASIONAL;
        this.activities = new ArrayList<Activity>();
        this.registers = new HashMap<LocalDateTime, Activity>();
        this.plan = new Plan();
    }

    /* Parameterized Constructors */
    public User(
        int id, String name, String email,
        String address, int heartRate, int weight,
        int height, Type type
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.heartRate = heartRate;
        this.weight = weight;
        this.height = height;
        this.type = type;
        this.activities = new ArrayList<Activity>();
        this.registers = new HashMap<LocalDateTime, Activity>();
        this.plan = null;
    }

    public User(
        int id, String name, String email,
        String address, int heartRate, int weight,
        int height, Type type,
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
        this.type = type;
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
        this.type = user.getType();
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

    public Type getType() {
        return this.type; // immutable
    }

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

    public void setType(Type type) {
        this.type = type;
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
        sb.append("  type: " + this.type + ",\n");

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
            user.getType() == this.type &&
            user.getActivities().equals(this.activities) &&
            user.getRegisters().equals(this.registers) &&
            user.getPlan().equals(this.plan)
        );
    }

    public User clone() {
        return new User(this);
    }

    // Manage users methods
    public String enterName(Scanner sc) {

        IO io = new IO();
        String name;
        while (true) {
            System.out.print("Enter user full name: ");
            name = io.readString(sc);
            // check if name is between 3 and 256 characters
            if (name.length() < 3 || name.length() > 256) {
                System.out.println("Name must be between 3 and 256 characters.");
                continue;
            }
            break;
        }
        return name;
    }

    public String enterEmail(Scanner sc, HashMap<Integer, User> users) {

        IO io = new IO();
        String email;
        while (true) {
            System.out.print("Enter the user email: ");
            String emailBuffer = io.readString(sc);
            // check if email is valid using regex
            if (!emailBuffer.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                System.out.println("Email is not valid.");
                continue;
            }
            // check if email is 320 characters or less
            if (emailBuffer.length() > 320) {
                System.out.println("Email must be 320 characters or less.");
                continue;
            }
            // check if email is unique
            boolean isUnique = users.entrySet().stream()
                .noneMatch(entry -> entry.getValue().getEmail().equals(emailBuffer));
            if (!isUnique) {
                System.out.println("Email already exists.");
                continue;
            }
            email = emailBuffer;
            break;
        }
        return email;
    }

    public String enterAddress(Scanner sc) {

        IO io = new IO();
        String address;
        while (true) {
            System.out.print("Enter the user address: ");
            address = io.readString(sc);
            // check if address is between 5 and 1024 characters
            if (address.length() < 5 || address.length() > 1024) {
                System.out.println("Address must be between 5 and 1024 characters.");
                continue;
            }
            break;
        }
        return address;
    }

    public int enterHeartRate(Scanner sc) {

        IO io = new IO();
        int heartRate;
        while (true) {
            System.out.print("Enter the user resting heart rate (in BPM): ");
            heartRate = io.readInt(sc);

            // check if heart rate is between 20 and 200
            if (20 > heartRate || heartRate > 200) {
                System.out.println("Heart rate must be between 20 and 200 BPM.");
                continue;
            }
            break;
        }
        return heartRate;
    }

    public int enterWeight(Scanner sc) {

        IO io = new IO();
        int weight;
        while (true) {
            System.out.print("Enter the user weight (in kg): ");
            weight = io.readInt(sc);

            // check if weight is between 20 and 200
            if (20 > weight || weight > 200) {
                System.out.println("Weight must be between 20 and 200 kg.");
                continue;
            }
            break;
        }
        return weight;
    }

    public int enterHeight(Scanner sc) {

        IO io = new IO();
        int height;
        while (true) {
            System.out.print("Enter the user height (in cm): ");
            height = io.readInt(sc);

            // check if height is between 100 and 220
            if (100 > height || height > 220) {
                System.out.println("Height must be between 100 and 220 cm.");
                continue;
            }
            break;
        }
        return height;
    }

    public Type enterType(Scanner sc) {

        IO io = new IO();
        while (true) {
            System.out.println("Possible user types:");
            System.out.println("  1 - OCCASIONAL");
            System.out.println("  2 - AMATEUR");
            System.out.println("  3 - PROFESSIONAL");
            // System.out.println("  4 - OLYMPIC");

            System.out.print("Enter the user type: ");
            int typeCode = io.readInt(sc);

            // check if user type is valid
            if (typeCode < 1 || typeCode > 3) {
                System.out.println("Invalid user type.");
                continue;
            }

            switch (typeCode) {
                case 1:
                    return Type.OCCASIONAL;
                case 2:
                    return Type.AMATEUR;
                case 3:
                    return Type.PROFESSIONAL;
            }
            break;
        }
        return null;
    }

    public static User create(Scanner sc, HashMap<Integer, User> users) {

        User user = new User();

        // increment the biggest id by 1 and set it as the new user id
        int id = users.size() == 0 ? 1 : users.keySet().stream().max(Integer::compare).get() + 1;

        String name = user.enterName(sc);

        String email = user.enterEmail(sc, users);

        String address = user.enterAddress(sc);

        int heartRate = user.enterHeartRate(sc);

        int weight = user.enterWeight(sc);

        int height = user.enterHeight(sc);

        Type type = user.enterType(sc);

        return new User(id, name, email, address, heartRate, weight, height, type);
    }

    public static User search(Scanner sc, HashMap<Integer, User> users) throws UserNotFoundException {

        IO io = new IO();
        int option;
        while (true) {
            System.out.println();
            System.out.println("Chose how to search for an user:");
            System.out.println("(1) By ID");
            System.out.println("(2) By email");
            System.out.print("Option: ");
            option = io.readInt(sc);

            if (!(option == 1 || option == 2)) {
                System.out.println("Invalid option.");
                continue;
            }
            break;
        }

        int id;
        String email;
        User user = null;

        if (option == 1) {
            System.out.print("Enter the user ID: ");
            id = io.readInt(sc);
            if (id == -1) {
                System.out.println("Invalid ID.");
                throw new UserNotFoundException("User not found.");
            }
            user = users.get(id);
        } else {
            System.out.print("Enter the user email: ");
            email = io.readString(sc);
            user = users.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
        }

        if (user == null) {
            throw new UserNotFoundException("User not found.");
        }

        System.out.println("Selected user: " + user.getName());
        return user;
    }

    public static void view(Scanner sc, HashMap<Integer, User> users) throws UserNotFoundException {

        User user;
        try {
            user = search(sc, users);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println(user);
    }

    public static void delete(Scanner sc, HashMap<Integer, User> users) throws UserNotFoundException {

        User user;
        try {
            user = search(sc, users);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        users.remove(user.getId());
        System.out.println("User deleted successfully.");
    }

    public static void edit(Scanner sc, HashMap<Integer, User> users, User user) throws UserNotFoundException {

        IO io = new IO();

        while (true) {
            System.out.println();
            System.out.println("Chose what to edit:");
            System.out.println("(1) Name");
            System.out.println("(2) Email");
            System.out.println("(3) Address");
            System.out.println("(4) Heart rate");
            System.out.println("(5) Weight");
            System.out.println("(6) Height");
            System.out.println("(7) Type");
            System.out.println("(8) Go back");
            System.out.print("Option: ");

            int option = io.readInt(sc);

            switch (option) {
                case 1:
                    String name = user.enterName(sc);
                    user.setName(name);
                    System.out.println("Name updated successfully.");
                    break;

                case 2:
                    String email = user.enterEmail(sc, users);
                    user.setEmail(email);
                    System.out.println("Email updated successfully.");
                    break;

                case 3:
                    String address = user.enterAddress(sc);
                    user.setAddress(address);
                    System.out.println("Address updated successfully.");
                    break;

                case 4:
                    int heartRate = user.enterHeartRate(sc);
                    user.setHeartRate(heartRate);
                    System.out.println("Heart rate updated successfully.");
                    break;
                case 5:
                    int weight = user.enterWeight(sc);
                    user.setWeight(weight);
                    System.out.println("Weight updated successfully.");
                    break;
                case 6:
                    int height = user.enterHeight(sc);
                    user.setHeight(height);
                    System.out.println("Height updated successfully.");
                    break;
                case 7:
                    Type type = user.enterType(sc);
                    user.setType(type);
                    System.out.println("Type updated successfully.");
                    break;
                case 8:
                    // Go back
                    return;
            }
        }
    }

    // Activities methods
    public void addActivity(Activity activity) {
        this.activities.add(activity.clone());
    }

    public void deleteActivity(Activity activity) {
        this.activities.remove(activity);
    }

    // Registers methods
    public void registerActivity(LocalDateTime date, Activity register) {
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

package src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * User
 * TODO: Dúvida em agregação é necessário clone de String's?
 */
public class User implements Serializable {

    // private static final long serialVersionUID = 1L; // TODO: check if necessary

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
    private Type type;
    private ArrayList<Activity> activities;
    private HashMap<LocalDateTime, Register> registers; // alternative LinkedHashMap
    // TODO: private Dictionary<Int, ArrayList<Plan>> plans;
 
    /* Default Constructor */
    public User() {
        this.id = 0;
        this.name = "";
        this.email = "";
        this.address = "";
        this.heartRate = 0;
        this.type = Type.OCCASIONAL;
        this.activities = new ArrayList<Activity>();
        this.registers = new HashMap<LocalDateTime, Register>();
    }

    /* Parameterized Constructors */
    public User(
        int id, String name, String email,
        String address, int heartRate, Type type
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.heartRate = heartRate;
        this.type = type;
        this.activities = new ArrayList<Activity>();
        this.registers = new HashMap<LocalDateTime, Register>();
    }

    public User(
        int id, String name, String email,
        String address, int heartRate, Type type,
        ArrayList<Activity> activities,
        HashMap<LocalDateTime, Register> registers
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.heartRate = heartRate;
        this.type = type;
        this.activities = activities; // TODO clone
        this.registers = registers; // TODO clone
    }

    /* Copy Constructor */
    public User(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.heartRate = user.getHeartRate();
        this.type = user.getType();
        this.activities = user.getActivities();
        this.registers = user.getRegisters();
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

    public Type getType() {
        return this.type;
    }

    public ArrayList<Activity> getActivities() {
        ArrayList<Activity> activities = new ArrayList<Activity>(this.activities.size());
        for (Activity activity : this.activities) {
            activities.add(activity.clone());
        }
        return activities;
    }

    public HashMap<LocalDateTime, Register> getRegisters() {
        HashMap<LocalDateTime, Register> registers = new HashMap<LocalDateTime, Register>(this.registers.size());
        for (LocalDateTime date : this.registers.keySet()) {
            registers.put(date, this.registers.get(date).clone());
        }
        return registers;
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

    public void setType(Type type) {
        this.type = type;
    }

    public void setActivities(ArrayList<Activity> activities) {
        // TODO clone
        this.activities = activities;
    }

    public void setRegisters(HashMap<LocalDateTime, Register> registers) {
        // TODO clone
        this.registers = registers;
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity.clone());
    }

    public void deleteActivity(Activity activity) {
        this.activities.remove(activity);
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
        sb.append("  type: " + this.type + ",\n");

        sb.append("  activities: [");
        for (int i = 0; i < this.activities.size() - 1; i++) {
            sb.append("\n    " + this.activities.get(i).getName() + ",");
        }
        if (this.activities.size() > 0) {
            sb.append("\n    " + this.activities.get(this.activities.size() - 1).getName() + "\n  ");
        }
        sb.append("],\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        sb.append("  registers: [");
        // TODO sort registers by date
        for (LocalDateTime date : this.registers.keySet()) {
            sb.append("\n    " + date.format(formatter) + " - " + this.registers.get(date).getActivity().getName() + ",");
        }
        sb.append("]\n");

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
            user.getType() == this.type &&
            user.getActivities().equals(this.activities) &&
            user.getRegisters().equals(this.registers) // TODO check this
        );
    }

    public User clone() {
        return new User(this);
    }

    public static User create(Scanner sc, ArrayList<User> users) {
        sc.nextLine();

        String name, email, address;
        int heartRate;
        Type type;

        // increment the last user id to get the new user id
        int id = users.size() == 0 ? 1 : users.get(users.size() - 1).getId() + 1;

        while (true) {
            System.out.print("Enter user full name: ");
            name = sc.nextLine();
            // check if name is between 3 and 256 characters
            if (name.length() < 3 || name.length() > 256) {
                System.out.println("Name must be between 3 and 256 characters.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter the user email: ");
            String emailBuffer = sc.nextLine();
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
            boolean isUnique = users.stream().noneMatch(user -> user.getEmail().equals(emailBuffer));
            if (!isUnique) {
                System.out.println("Email already exists.");
                continue;
            }
            email = emailBuffer;
            break;
        }

        while (true) {
            System.out.print("Enter the user address: ");
            address = sc.nextLine();
            // check if address is between 5 and 1024 characters
            if (address.length() < 5 || address.length() > 1024) {
                System.out.println("Address must be between 5 and 1024 characters.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter the user resting heart rate (in BPM): ");
            try {
                heartRate = sc.nextInt();
            }
            catch (Exception e) {
                System.out.println("Invalid heart rate.");
                sc.nextLine(); // clear buffer
                continue;
            }
            // check if heart rate is between 20 and 200
            if (20 > heartRate || heartRate > 200) {
                System.out.println("Heart rate must be between 20 and 200 BPM.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.println("Possible user types:");
            System.out.println("  1 - OCCASIONAL");
            System.out.println("  2 - AMATEUR");
            System.out.println("  3 - PROFESSIONAL");
            // System.out.println("  4 - OLYMPIC");
            System.out.print("Enter the user type: ");
            int typeCode = 0;
            try {
                typeCode = sc.nextInt();
            }
            catch (Exception e) {
                System.out.println("Invalid user type.");
                sc.nextLine(); // clear buffer
                continue;
            }
            // check if user type is valid
            if (typeCode < 1 || typeCode > 3) {
                System.out.println("Invalid user type.");
                continue;
            }
            type =
                typeCode == 1 ? Type.OCCASIONAL :
                typeCode == 2 ? Type.AMATEUR :
                Type.PROFESSIONAL;
            break;
        }

        sc.nextLine(); // clear buffer

        System.out.println("User created successfully. (ID: " + id + ")");

        return new User(id, name, email, address, heartRate, type); // TODO clone isn't necessary here?
    }

    public static User search(Scanner sc, ArrayList<User> users) {

        sc.nextLine(); // clear buffer

        int option;
        while (true) {
            System.out.println();
            System.out.println("Chose how to search for an user:");
            System.out.println("(1) By ID");
            System.out.println("(2) By email");
            System.out.print("Option: ");
            try {
                option = sc.nextInt();
            } catch (Exception e) {
                sc.nextLine(); // clear buffer
                System.out.println("Invalid option.");
                continue;
            }
            if (option == 1 || option == 2) {
                sc.nextLine(); // clear buffer
                break;
            }
            System.out.println("Invalid option.");
        }

        int id;
        String email;
        User user = null;

        if (option == 1) {
            System.out.print("Enter the user ID: ");
            try {
                id = sc.nextInt();
            } catch (Exception e) {
                sc.nextLine(); // clear buffer
                System.out.println("Invalid ID.");
                return null;
            }
            user = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
        } else {
            System.out.print("Enter the user email: ");
            email = sc.nextLine();
            user = users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
        }

        return user;
    }

    public static void view(Scanner sc, ArrayList<User> users) {

        User user = search(sc, users);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println(user);
    }

    public static void delete(Scanner sc, ArrayList<User> users) {

        User user = search(sc, users);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        users.remove(user);
        System.out.println("User deleted successfully.");
    }

    // Registers methods
    public void registerActivity(LocalDateTime date, Register register) {
        this.registers.put(date, register.clone());
    }

    public void viewRegisters() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (LocalDateTime date : this.registers.keySet()) {
            System.out.println(date.format(formatter) + " - " + this.registers.get(date));
        }
    }
}

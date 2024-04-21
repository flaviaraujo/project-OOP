package src;

import src.User;
import src.exceptions.UserNotFoundException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * Main
 */
public class Main {

    private boolean updatedState = false;
    private HashMap<Integer, User> users = new HashMap<>(); // currently loaded users
    private final String defaultStateFilepath = "data/state.ser"; // default state file path

    public static void main(String[] args) {
        Main m = new Main();
        String stateFilepath = m.defaultStateFilepath;
        Scanner sc = new Scanner(System.in);

        // Parse command line arguments
        if (args.length > 0) {
            // TODO --help option
            // TODO --user <--id|--email> <id|email> [state] option
            stateFilepath = args[0];
            loadState(stateFilepath, m, sc);
        }

        System.out.println("Welcome to the Activity Planner!");

        // Main loop
        while (true) {
            mainMenu(sc, stateFilepath, m);
        }
    }

    // TODO user perspective

    private static void mainMenu(Scanner sc, String stateFilepath, Main m) {
        System.out.println();
        System.out.println("[Main menu] Please select an option:");
        System.out.println("(1) Manage users (create, delete, view)");
        System.out.println("(2) Manage user activities (create, delete, view)");
        System.out.println("(3) Manage user registered activities (register, view)");
        System.out.println("(4) Manage user plan (create, delete, view)");
        System.out.println("(5) Start a simulation");
        System.out.println("(6) Statistics menu");
        System.out.println("(7) Save program state");
        System.out.println("(8) Load program state");
        System.out.println("(9) Exit");
        System.out.print("Option: ");

        int option = 0;
        try {
            option = sc.nextInt();
        }
        catch (Exception e) { // TODO catch specific exceptions
            sc.nextLine(); // clear buffer
            // e.printStackTrace(); // removed to avoid clutter
        }

        User user;
        int submenuOption;

        switch (option) {
            case 1:
                // Manage users
                // TODO User.manageUsersMenu(sc, m);

                while (true) {
                    System.out.println();
                    System.out.println("[Manage users menu] Please select an option:");
                    System.out.println("(1) Create an user");
                    System.out.println("(2) Delete an user");
                    System.out.println("(3) View an user");
                    System.out.println("(4) View all users");
                    System.out.println("(5) Back to main menu");
                    System.out.print("Option: ");

                    submenuOption = 0;
                    try {
                        submenuOption = sc.nextInt();
                    }
                    catch (Exception e) {
                        sc.nextLine(); // clear buffer
                    }

                    switch (submenuOption) {
                        case 1:
                            // Create an user
                            try {
                                user = User.create(sc, m.users);
                                m.addUser(user, user.getId());
                                m.setUpdatedState(true);
                                System.out.println("User created successfully. (ID: " + user.getId() + ")");
                            }
                            catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        case 2:
                            // Delete an user
                            try {
                                User.delete(sc, m.users);
                                m.setUpdatedState(true);
                            }
                            catch (UserNotFoundException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        case 3:
                            // View an user
                            try {
                                User.view(sc, m.users);
                            }
                            catch (UserNotFoundException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        case 4:
                            // View all users
                            for (Entry<Integer, User> entry : m.users.entrySet()) {
                                System.out.println(entry.getValue());
                            }
                            break;
                        case 5:
                            // Back to main menu
                            return;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                }
            case 2:
                // Select an user to manage activities
                try {
                    user = User.search(sc, m.users);
                }
                catch (UserNotFoundException e) {
                    System.out.println(e.getMessage());
                    break;
                }

                // Manage user activities
                // TODO: Activity.manageUserActivitiesMenu(sc, m, user);

                while (true) {
                    System.out.println();
                    System.out.println("[Manage user activities menu] Please select an option:");
                    System.out.println("(1) Create an activity");
                    System.out.println("(2) Delete an activity");
                    System.out.println("(3) View an activity");
                    System.out.println("(4) View all activities");
                    System.out.println("(5) Back to main menu");
                    System.out.print("Option: ");

                    submenuOption = 0;
                    try {
                        submenuOption = sc.nextInt();
                    }
                    catch (Exception e) {
                        sc.nextLine(); // clear buffer
                    }

                    ArrayList<Activity> userActivities = user.getActivities();
                    Activity a;

                    switch (submenuOption) {
                        case 1:
                            // Create an activity
                            Activity activity = Activity.createMenu(sc, userActivities);
                            if (activity == null) {
                                System.out.println("Activity not created.");
                                break;
                            }
                            // Add activity to user
                            user.addActivity(activity);
                            // Update the Main instance by replacing the user
                            try {
                                m.updateUser(user);
                            }
                            catch (UserNotFoundException e) {
                                System.out.println(e.getMessage());
                                break;
                            }
                            // The state is updated when a new activity for an user is created
                            m.setUpdatedState(true);
                            System.out.println("Activity created successfully.");
                            break;
                        case 2:
                            if (userActivities.size() == 0) {
                                System.out.println("The selected user has no activities.");
                                break;
                            }

                            // Delete an activity
                            a = Activity.searchActivity(sc, userActivities);
                            if (a == null) {
                                System.out.println("Activity not found.");
                                break;
                            }
                            user.deleteActivity(a);
                            // Update the Main instance by replacing the user
                            try {
                                m.updateUser(user);
                            }
                            catch (UserNotFoundException e) {
                                System.out.println(e.getMessage());
                                break;
                            }
                            // The state is updated when an activity for an user is deleted
                            m.setUpdatedState(true);
                            System.out.println("Activity deleted successfully.");
                            break;
                        case 3:
                            if (userActivities.size() == 0) {
                                System.out.println("The selected user has no activities.");
                                break;
                            }

                            // View an activity
                            a = Activity.searchActivity(sc, userActivities);
                            if (a == null) {
                                System.out.println("Activity not found.");
                                break;
                            }
                            System.out.println(a);
                            break;
                        case 4:
                            // View all activities
                            if (userActivities.size() == 0) {
                                System.out.println("The selected user has no activities.");
                                break;
                            }
                            for (Activity t : userActivities) System.out.println(t);
                            break;
                        case 5:
                            // Back to main menu
                            return;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                }
            case 3:
                // Select an user to add a registered activity or view them
                try {
                    user = User.search(sc, m.users);
                }
                catch (UserNotFoundException e) {
                    System.out.println(e.getMessage());
                    break;
                }

                Activity a;
                while (true) {
                    // Manage user registed activities
                    System.out.println();
                    System.out.println("[Manage user registered activities menu] Please select an option:");
                    System.out.println("(1) Register an activity");
                    System.out.println("(2) View registered activities");
                    System.out.println("(3) Back to main menu");
                    System.out.print("Option: ");

                    submenuOption = 0;
                    try {
                        submenuOption = sc.nextInt();
                    }
                    catch (Exception e) {
                        sc.nextLine(); // clear buffer
                    }

                    switch (submenuOption) {
                        case 1:
                            a = Activity.searchActivity(sc, user.getActivities());
                            if (a == null) {
                                System.out.println("Activity not found.");
                                break;
                            }

                            // Enter date manually or use current date
                            System.out.println("");
                            System.out.println("Registering activity: " + a.getName());
                            System.out.println("(1) Enter date manually");
                            System.out.println("(2) Use current date");
                            System.out.print("Option: ");

                            int dateOption = 0;
                            try {
                                dateOption = sc.nextInt();
                            }
                            catch (Exception e) {
                                sc.nextLine(); // clear buffer
                            }

                            LocalDateTime datetime = LocalDateTime.now();

                            if (dateOption == 1) {
                                while (true) {
                                    try {
                                        System.out.print("Enter date (yyyy-mm-dd): ");
                                        String date = sc.next();
                                        System.out.print("Enter time (hh:mm): ");
                                        String time = sc.next();
                                        datetime = LocalDateTime.parse(date + "T" + time);
                                    }
                                    catch (Exception e) {
                                        System.out.println("Invalid date or time format.");
                                        continue;
                                    }
                                    if (datetime.isBefore(LocalDateTime.now())) {
                                        break;
                                    }
                                    System.out.println("Date must be less than current date.");
                                }
                            }

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                            // Create a new register
                            Register register = new Register(a, user);

                            // Register an activity in the user
                            user.registerActivity(datetime, register);

                            // Print the registered activity
                            System.out.println("Activity registered successfully: ");
                            System.out.println(register.getActivity().getName() + " on " + datetime.format(formatter));
                            System.out.println(register.getCaloriesBurned() + " calories burned.");

                            // Update the Main instance by replacing the user
                            try {
                                m.updateUser(user);
                            }
                            catch (UserNotFoundException e) {
                                System.out.println(e.getMessage());
                                break;
                            }

                            // The state is updated when an activity for an user is registered
                            m.setUpdatedState(true);
                            break;
                        case 2:
                            // View registered activities
                            user.viewRegisters();
                            break;
                        case 3:
                            // Back to main menu
                            return;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                }
            case 4:
                // Manage user plan
                // Select an user to manage plan
                try {
                    user = User.search(sc, m.users);
                }
                catch (UserNotFoundException e) {
                    System.out.println(e.getMessage());
                    break;
                }

                while (true) {
                    // Manage user plan
                    System.out.println();
                    System.out.println("[Manage user plan menu] Please select an option:");
                    System.out.println("(1) Create plan interactively");
                    System.out.println("(2) Create plan based on user goals");
                    System.out.println("(3) Delete plan");
                    System.out.println("(4) View plan");
                    System.out.println("(5) Back to main menu");
                    System.out.print("Option: ");

                    submenuOption = 0;
                    try {
                        submenuOption = sc.nextInt();
                    }
                    catch (Exception e) {
                        sc.nextLine(); // clear buffer
                    }

                    ArrayList<Activity> userActivities = user.getActivities();

                    switch (submenuOption) {
                        case 1:
                            // create plan interactively
                            // delete plan if it already exists
                            Plan old = user.getPlan();
                            if (old != null) {
                                System.out.println("A plan already exists for the selected user.");
                                System.out.print("Do you want to delete the current plan? [y/n]: ");
                                String delete = sc.next();
                                if (delete.equals("y")) {
                                    user.setPlan(null);
                                    System.out.println("Plan deleted successfully.");
                                }
                                else {
                                    System.out.println("Plan wasn't deleted.");
                                    break;
                                }
                            }
                            Plan p = new Plan();
                            p = p.create(sc, userActivities);
                            user.setPlan(p);
                            if (old == null && p == null) {
                                System.out.println("Plan not created.");
                                break;
                            }
                            System.out.println("Plan created successfully.");
                            try {
                                m.updateUser(user);
                            }
                            catch (UserNotFoundException e) {
                                System.out.println(e.getMessage());
                                break;
                            }
                            m.setUpdatedState(true);
                            break;
                        case 2:
                            // TODO: create plan based on user goals
                            break;
                        case 3:
                            // Delete plan
                            user.setPlan(null);
                            System.out.println("Plan deleted successfully.");
                            try {
                                m.updateUser(user);
                            }
                            catch (UserNotFoundException e) {
                                System.out.println(e.getMessage());
                                break;
                            }
                            m.setUpdatedState(true);
                            break;
                        case 4:
                            // View plan
                            if (user.getPlan() == null) {
                                System.out.println("The selected user has no plan.");
                                break;
                            }
                            System.out.print(user.getPlan());
                            break;
                        case 5:
                            // Back to main menu
                            return;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                }
            case 5:
                // Start a simulation
                Simulation simulation = new Simulation();
                simulation.run(sc, m.users);
                break;
            case 6:
                // Statistics menu
                Stats stats = new Stats();
                stats.statsMenu(sc, m.users);
                break;
            case 7:
                // Save program state
                saveState(stateFilepath, m);
                break;
            case 8:
                // Load program state
                loadState(stateFilepath, m, sc);
                break;
            case 9:
                // state: to save or not to save
                if (m.updatedState) {
                    System.out.print("Do you want to save the current state before exiting? [y/n]: ");
                    String save = sc.next();
                    if (save.equals("y")) {
                        saveState(stateFilepath, m);
                    }
                }
                System.out.println("Exiting...");
                sc.close();
                System.exit(0);
            default:
                System.out.println("Invalid option");
                break;
        }
    }

    public boolean getUpdatedState() {
        return updatedState;
    }

    public void setUpdatedState(boolean updatedState) {
        this.updatedState = updatedState;
    }

    public HashMap<Integer, User> getUsers() {
        HashMap<Integer, User> users = new HashMap<>(this.users.size());
        for (Entry<Integer, User> entry : this.users.entrySet()) {
            users.put(entry.getKey(), entry.getValue().clone());
        }
        return users;
    }

    public void setUsers(HashMap<Integer, User> users) {
        HashMap<Integer, User> tmpUsers = new HashMap<>(users.size());
        for (Entry<Integer, User> entry : users.entrySet()) {
            tmpUsers.put(entry.getKey(), entry.getValue().clone());
        }
        this.users = tmpUsers;
    }

    public void addUser(User user, int id) throws IllegalArgumentException {
        if (this.users.containsKey(id)) {
            throw new IllegalArgumentException("User with id " + id + " already exists.");
        }
        this.users.put(id, user.clone());
    }

    public void removeUser(User user) {
        this.users.remove(user.getId());
    }

    /**
     *
     * @param user the new updated user
     */
    public void updateUser(User user) throws UserNotFoundException {
        if (this.users.containsKey(user.getId())) {
            this.users.put(user.getId(), user.clone());
        } else {
            throw new UserNotFoundException("User with ID " + user.getId() + " does not exist.");
        }
    }

    private static void saveState(String stateFilepath, Main m) {
        if (!m.updatedState) {
            System.out.println("No data to save");
            return;
        }
        System.out.println("Saving state to " + stateFilepath);
        try {
            FileOutputStream fileOut = new FileOutputStream(stateFilepath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(m.users);

            out.flush();
            out.close();
            fileOut.close();

            System.out.println("State saved successfully, " + m.users.size() + " users saved.");
            m.updatedState = false;
        }
        catch (NotSerializableException e) {
            System.out.println("Object not serializable: " + e.getMessage());
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found: \"" + stateFilepath + "\"");
        }
        catch (IOException e) {
            System.out.println("IOException. Error: " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Error saving state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadState(String stateFilepath, Main m, Scanner sc) {
        if (m.updatedState) {
            System.out.print("Warning: current state will be lost. Do you want to continue? [y/n]: ");
            String cont = sc.next();
            if (!cont.equals("y")) {
                return;
            }
        }
        System.out.println("Loading state from " + stateFilepath);
        try {
            FileInputStream fileIn = new FileInputStream(stateFilepath);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            HashMap<Integer, User> tmpUsers = (HashMap<Integer, User>) in.readObject(); //TODO cast exception

            m.users = new HashMap<>(tmpUsers.size());
            for (Object obj : tmpUsers.values()) {
                if (obj instanceof User) {
                    m.users.put(((User) obj).getId(), ((User) obj));
                }
            }

            in.close();
            fileIn.close();
            System.out.println("State loaded successfully, " + m.users.size() + " users loaded.");
            m.updatedState = false;

        }
        catch (NotSerializableException e) {
            System.out.println("Object not serializable: " + e.getMessage());
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found: \"" + stateFilepath + "\"");
        }
        catch (IOException e) {
            System.out.println("IOException. Error: " + e.getMessage());
        }
        catch (ClassNotFoundException e) {
            System.out.println("Couldn't find class: " + e.getMessage());
        }
        catch (ClassCastException e) {
            System.out.println("Couldn't cast object: " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Error loading state: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

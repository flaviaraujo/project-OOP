package src;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main
 */
public class Main {

    private boolean updatedState = false;
    private ArrayList<User> users = new ArrayList<>(); // currently loaded users

    public static void main(String[] args) {
        Main m = new Main();
        String stateFilepath = "data/state.ser"; // default state file path
        Scanner sc = new Scanner(System.in);

        // Parse command line argument
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
        catch (Exception e) {
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
                            user = User.create(sc, m.users);

                            // Add user to the loaded users array
                            m.addUser(user);
                            // state is updated when a new user is created
                            m.setUpdatedState(true);
                            break;
                        case 2:
                            // Delete an user
                            User.delete(sc, m.users);
                            // state is updated when an user is deleted
                            m.setUpdatedState(true);
                            break;
                        case 3:
                            // View an user
                            User.view(sc, m.users);
                            break;
                        case 4:
                            // View all users
                            for (User u : m.users) System.out.println(u);
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
                user = User.search(sc, m.users);
                if (user == null) {
                    System.out.println("No user selected");
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
                            m.updateUser(user);
                            // The state is updated when a new activity for an user is created
                            m.setUpdatedState(true);
                            System.out.println("Activity created successfully.");
                            break;
                        case 2:
                            if (userActivities.size() == 0) {
                                System.out.println("The selected user has no activities.");
                                break;
                            }
                            System.out.println("User activities:");
                            for (Activity t : userActivities) System.out.println("  -> " + t.getName());
                            // Delete an activity
                            a = Activity.searchActivity(sc, userActivities);
                            if (a == null) {
                                System.out.println("Activity not found.");
                                break;
                            }
                            user.deleteActivity(a);
                            // Update the Main instance by replacing the user
                            m.updateUser(user);
                            // The state is updated when an activity for an user is deleted
                            m.setUpdatedState(true);
                            System.out.println("Activity deleted successfully.");
                            break;
                        case 3:
                            if (userActivities.size() == 0) {
                                System.out.println("The selected user has no activities.");
                                break;
                            }
                            System.out.println("User activities:");
                            for (Activity t : userActivities) System.out.println("  -> " + t.getName());
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
                user = User.search(sc, m.users);
                if (user == null) {
                    System.out.println("No user selected");
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
                            m.updateUser(user);

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
                user = User.search(sc, m.users);
                if (user == null) {
                    System.out.println("No user selected");
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
                            m.updateUser(user);
                            m.setUpdatedState(true);
                            break;
                        case 2:
                            // TODO: create plan based on user goals
                            break;
                        case 3:
                            // Delete plan
                            user.setPlan(null);
                            System.out.println("Plan deleted successfully.");
                            m.updateUser(user);
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

    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>(this.users.size());
        for (User user : this.users) {
            users.add(user.clone());
        }
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        ArrayList<User> tmpUsers = new ArrayList<>(users.size());
        for (User user : users) {
            tmpUsers.add(user.clone());
        }
        this.users = tmpUsers;
    }

    public void addUser(User user) {
        this.users.add(user.clone());
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    /**
     * @brief Update the user loaded in the ArrayList in the
     * Main instance with the user passed as argument.
     *
     * @param user the new updated user
     */
    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user.clone());
                break;
            }
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
            out.close();
            fileOut.close();
            System.out.println("State saved successfully, " + m.users.size() + " users saved.");
            m.updatedState = false;
        } catch (Exception e) {
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
            ArrayList<?> tempUsers = (ArrayList<?>) in.readObject();
            m.users = new ArrayList<>(tempUsers.size());
            for (Object obj : tempUsers) {
                if (obj instanceof User) {
                    m.users.add((User) obj); // TODO should I clone the obj and them destroy him?
                }
            }
            in.close();
            fileIn.close();
            System.out.println("State loaded successfully, " + m.users.size() + " users loaded.");
            m.updatedState = false;
        } catch (Exception e) {
            System.out.println("Error loading state: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        try {
            // Parse command line argument
            if (args.length > 0) {
                stateFilepath = args[0];
                loadState(stateFilepath, m, sc);
            }

            System.out.println("Welcome to the Activity Planner!");

            // Main loop
            while (true) {
                mainMenu(sc, stateFilepath, m);
            }
        } catch (Exception e) {
            System.out.println("Interrupted: " + e.getMessage());
        }
        sc.close();
    }

    private static void mainMenu(Scanner sc, String stateFilepath, Main m) {
        System.out.println();
        System.out.println("[Main menu] Please select an option:");
        System.out.println("(1) Manage users (create, delete, view)");
        System.out.println("(2) Create plan for an user");
        System.out.println("(3) Create plans based on user objectives");
        System.out.println("(4) Start a simulation");
        System.out.println("(5) Statistics menu");
        System.out.println("(6) Save program state");
        System.out.println("(7) Load program state");
        System.out.println("(8) Exit");
        System.out.print("Option: ");
        int option = 0;
        try {
            option = sc.nextInt();
        }
        catch (Exception e) {
            sc.nextLine(); // clear buffer
            // e.printStackTrace(); // removed to avoid clutter
        }
        switch (option) {
            case 1:
                while (true) {
                    System.out.println();
                    System.out.println("[Manage users menu] Please select an option:");
                    System.out.println("(1) Create an user");
                    System.out.println("(2) Delete an user");
                    System.out.println("(3) View an user");
                    System.out.println("(4) View all users");
                    System.out.println("(5) Back to main menu");
                    System.out.print("Option: ");
                    int manageUsersOption = 0;
                    try {
                        manageUsersOption = sc.nextInt();
                    }
                    catch (Exception e) {
                        sc.nextLine(); // clear buffer
                    }
                    switch (manageUsersOption) {
                        case 1:
                            // Create an user
                            User user = User.create(sc, m.users);

                            // Add user to the loaded users array
                            m.users.add(user);
                            // state is updated when a new user is created
                            m.updatedState = true;
                            break;
                        case 2:
                            // Delete an user
                            User.delete(sc, m.users);
                            break;
                        case 3:
                            // View an user
                            // TODO is it worth to implement: view nultiple users by their name?
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
                // TODO Create plan for an user
                break;
            case 3:
                // TODO Create plan based on user objectives
                break;
            case 4:
                // TODO Start a simulation
                break;
            case 5:
                // TODO Statistics menu
                break;
            case 6:
                // Save program state
                saveState(stateFilepath, m);
                break;
            case 7:
                // Load program state
                loadState(stateFilepath, m, sc);
                break;
            case 8:
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

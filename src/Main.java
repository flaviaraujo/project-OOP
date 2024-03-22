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

    private ArrayList<Activity> createdActivities = new ArrayList<>();
    private ArrayList<User> createdUsers = new ArrayList<>();
    private ArrayList<Plan> createdPlans = new ArrayList<>();

    public static void main(String[] args) {
        Main main = new Main();
        String filepath = "data/state.ser";
        try {
            // Parse command line argument
            if (args.length > 0) {
                filepath = args[0];
                loadState(filepath);
            }

            System.out.println("Welcome to the Activity Planner!");

            // Main loop
            while (true) {
                Scanner sc = new Scanner(System.in);
                mainMenu(sc, filepath, main);
            }
        } catch (Exception e) {
            System.out.println("Interrupted: " + e.getMessage());
        }
    }

    private static void mainMenu(Scanner scanner, String filepath, Main main) {
        System.out.println("[Main menu] Please select an option:");
        System.out.println("(1) Create users, activities and plans");
        System.out.println("(2) Create plans based on user objectives");
        System.out.println("(3) Start a simulation");
        System.out.println("(4) Statistics menu");
        System.out.println("(5) Save program state");
        System.out.println("(6) Load program state");
        System.out.println("(7) Exit");
        System.out.print("Option: ");
        int option = scanner.nextInt();
        switch (option) {
            case 1:
                // Create users, activities and plans
                break;
            case 2:
                // Create plans based on user objectives
                break;
            case 3:
                // Start a simulation
                break;
            case 4:
                // Statistics menu
                break;
            case 5:
                // Save program state
                saveState(filepath, main);
                break;
            case 6:
                // Load program state
                loadState(filepath);
                break;
            case 7:
                // state: to save or not to save
                System.out.println("Exiting...");
                System.exit(0);
            default:
                System.out.println("Invalid option");
                break;
        }
    }

    private static void saveState(String filepath, Main main) {
        // save state
        if (
            main.createdActivities.size() == 0 &&
            main.createdUsers.size() == 0 &&
            main.createdPlans.size() == 0
        ) {
            System.out.println("No data to save");
            return;
        }
        System.out.println("Saving state to " + filepath);
        // TODO
    }

    private static void loadState(String filepath) {
        // load state
        // TODO
    }
}

package src;

import src.Activity;
import src.ActivityPlanner;
import src.activities.Distance;

import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.time.format.DateTimeParseException;

import src.exceptions.UserNotFoundException;
import src.exceptions.ActivityIsRegisteredException;
import src.exceptions.StateNotSavedException;
import src.exceptions.StateNotLoadedException;

public class Controller {

    private ActivityPlanner m;
    private Scanner sc;

    public Controller() {
        this.m = new ActivityPlanner();
        this.sc = new Scanner(System.in);
    }

    public static void main(String[] args) {

        // Initialize the Controller
        Controller c = new Controller();

        // User to use in the user perspective menu (if any is passed as argument)
        User user = null;

        // Parse command line arguments
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--help") || args[i].equals("-h")) {
                System.out.println(
                        "Usage: java src.Main [--load <file>] [--user <--id|--email> <id|email>]" +
                        "Options:" +
                        "  -h --help: show this help message" +
                        "  -l --load: load a program state from file" +
                        "  -u --user: " +
                        "select an user by id or email to view his perspective" +
                        "  -e --email: select an user by email (use with --user option after --load option)" +
                        "  -i --id: select an user by ID (use with --user option after --load option)");
                System.exit(0);
            }
            else if (args[i].equals("--load") || args[i].equals("-l")) {
                c.m.setStateFilepath(args[++i]);
                c.loadStateIO(c.m, c.sc);
            }
            else if (args[i].equals("--user") || args[i].equals("-u")) {
                if (args.length < i + 2 + 1) {
                    System.out.println("Invalid number of arguments for option: " + args[i]);
                    System.exit(1);
                }
                String option = args[++i];
                String value = args[++i];
                if (option.equals("--id") || option.equals("-i")) {
                    try {
                        int id = Integer.parseInt(value);
                        user = c.m.getUserById(id);
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Invalid user ID: " + value);
                        System.exit(1);
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Note that you must load the state file first.");
                        System.exit(2);
                    }
                }
                else if (option.equals("--email") || option.equals("-e")) {
                    try {
                        user = c.m.getUserByEmail(value);
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Note that you must load the state file first.");
                        System.exit(2);
                    }
                }
                else {
                    System.out.println("Invalid user selector option: " + option);
                    System.exit(1);
                }
            }
            else {
                System.out.println("Invalid option: " + args[i]);
                System.exit(1);
            }
        }

        if (user != null) {
            System.out.println("Welcome to Activity Planner, " + user.getName() + "!");
            // User perspective menu
            while (true) {
                c.userMenu(c.m, c.sc, user);
            }
        }
        else {
            System.out.println("Welcome to Activity Planner!");
            // Main menu
            while (true) {
                c.mainMenu(c.m, c.sc);
            }
        }
    }

    /* main menu */
    private void mainMenu(ActivityPlanner m, Scanner sc) {
        System.out.println(
                "\n" +
                "[Main menu] Please select an option:\n" +
                "(1) Manage users (create, delete, edit, view)\n" +
                "(2) Manage user activities (create, delete, view)\n" +
                "(3) Manage user registered activities (register, view)\n" +
                "(4) Manage user plan (create, delete, view)\n" +
                "(5) Start a simulation\n" +
                "(6) Statistics menu\n" +
                "(7) Save program state\n" +
                "(8) Load program state\n" +
                "(9) Exit");

        System.out.print("Option: ");
        int option = readInt(sc);

        switch (option) {
            case 1:
                // Manage users
                manageUserSubMenu(m, sc);
                break;
            case 2:
                // Manage user activities
                manageUserActivitiesSubMenu(m, sc);
                break;
            case 3:
                // Manage user registered activities
                manageUserRegisteredActivitiesSubMenu(m, sc);
                break;
            case 4:
                // Manage user plan
                manageUserPlanSubMenu(m, sc);
                break;
            case 5:
                // Simulation
                simulationSubMenu(m, sc);
                break;
            case 6:
                // Statistics
                statisticsSubMenu(m, sc);
                break;
            case 7:
                // Save program state
                saveStateIO(m);
                break;
            case 8:
                // Load program state
                loadStateIO(m, sc);
                break;
            case 9:
                // Exit
                exit(m, sc);
            default:
                System.out.println("Invalid option");
                break;
        }
    }
    /* sub-menus */
    private void manageUserSubMenu(ActivityPlanner m, Scanner sc) {

        while (true) {
            System.out.println(
                    "\n" +
                    "[Manage users menu] Please select an option:\n" +
                    "(1) Create an user\n" +
                    "(2) Delete an user\n" +
                    "(3) View an user\n" +
                    "(4) View all users\n" +
                    "(5) Edit an user\n" +
                    "(6) Back to main menu");

            System.out.print("Option: ");
            int option = readInt(sc);

            User user = null;
            ArrayList<String> emails = m.getUsersEmails();

            switch (option) {
                case 1:
                    // Create an user
                    int id = m.getNextUserId();
                    user = createUser(sc, emails, id);
                    m.addUser(user);
                    System.out.println("User created successfully. (ID: " + user.getId() + ")");
                    break;
                case 2:
                    // Delete an user
                    try {
                        user = searchUserIO(m, sc);
                        m.removeUser(user);
                        System.out.println("User deleted successfully.");
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    // View an user
                    try {
                        user = searchUserIO(m, sc);
                        System.out.println(user);
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    // View all users
                    ArrayList<User> users = new ArrayList<>(m.getUsers().values());
                    for (User u : users) {
                        System.out.println(u);
                    }
                    break;
                case 5:
                    // Edit an user
                    try {
                        user = searchUserIO(m, sc);
                        editUser(sc, emails, user);
                        m.updateUser(user);
                        System.out.println("User edited successfully.");
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                case 6:
                    // Back to main menu
                    return;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private void manageUserActivitiesSubMenu(ActivityPlanner m, Scanner sc) {

        // Select an user to manage activities
        User user = null;
        try {
            user = searchUserIO(m, sc);
        }
        catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        // User activities submenu
        while (true) {
            System.out.println();
            System.out.println("[Manage user activities menu] Please select an option:");
            System.out.println("(1) Create an activity");
            System.out.println("(2) Delete an activity");
            System.out.println("(3) View an activity");
            System.out.println("(4) View all activities");
            System.out.println("(5) Back to main menu");
            System.out.print("Option: ");

            int option = readInt(sc);

            ArrayList<Activity> userActivities = user.getActivities();
            Activity a;

            switch (option) {
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
                    System.out.println("Activity created successfully.");
                    break;
                case 2:
                    if (userActivities.size() == 0) {
                        System.out.println("The selected user has no activities.");
                        break;
                    }

                    // Delete an activity
                    a = Activity.search(sc, userActivities);
                    try {
                        user.deleteActivity(a);
                    }
                    catch (ActivityIsRegisteredException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    // Update the Main instance by replacing the user
                    m.updateUser(user);
                    System.out.println("Activity deleted successfully.");
                    break;
                case 3:
                    if (userActivities.size() == 0) {
                        System.out.println("The selected user has no activities.");
                        break;
                    }

                    // View an activity
                    a = Activity.search(sc, userActivities);
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
    }

    private void manageUserRegisteredActivitiesSubMenu(ActivityPlanner m, Scanner sc) {

        // Select an user to add a registered activity or view them
        User user = null;
        try {
            user = searchUserIO(m, sc);
        }
        catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        while (true) {
            // Manage user registed activities
            System.out.println();
            System.out.println("[Manage user registered activities menu] Please select an option:");
            System.out.println("(1) Register an activity");
            System.out.println("(2) View registered activities");
            System.out.println("(3) Back to main menu");
            System.out.print("Option: ");

            int option = readInt(sc);

            switch (option) {
                case 1:
                    Activity activity = (Activity) new Distance();
                    user = activity.register(sc, user);
                    // Update the Main instance by replacing the user
                    m.updateUser(user);
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
    }

    private void manageUserPlanSubMenu(ActivityPlanner m, Scanner sc) {

        // Select an user to manage plan
        User user = null;
        try {
            user = searchUserIO(m, sc);
        }
        catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return;
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

            int option = readInt(sc);

            ArrayList<Activity> userActivities = user.getActivities();

            switch (option) {
                case 1:
                    // create plan interactively
                    // delete plan if it already exists
                    Plan old = user.getPlan();
                    if (old != null) {
                        System.out.println("A plan already exists for the selected user.");
                        System.out.print("Do you want to overwrite the current plan? [y/n]: ");
                        String delete = readYesNo(sc);
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
                    break;
                case 2:
                    // Create plan based on user goals
                    Plan oldPlan = user.getPlan();
                    if (oldPlan != null) {
                        System.out.print("Do you want to overwrite the user current plan? [y/n]: ");
                        String delete = readYesNo(sc);
                        if (delete.equals("y")) {
                            user.setPlan(null);
                            System.out.println("Plan deleted successfully.");
                        }
                        else {
                            System.out.println("Plan wasn't deleted.");
                            break;
                        }
                    }

                    Plan plan = new Plan();
                    plan = plan.createBasedOnGoals(sc, user);
                    if (plan == null) {
                        System.out.println("Plan not created.");
                        break;
                    }
                    user.setPlan(plan);
                    break;
                case 3:
                    // Delete plan
                    if (user.getPlan() == null) {
                        System.out.println("The selected user has no plan.");
                        break;
                    }
                    user.setPlan(null);
                    System.out.println("Plan deleted successfully.");
                    m.updateUser(user);
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
    }

    private void simulationSubMenu(ActivityPlanner m, Scanner sc) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = getSimulationEndDate(sc, startDate);
        System.out.println(m.runSimulation(startDate, endDate));
    }

    /* statistics menu */
    private void statisticsSubMenu(ActivityPlanner m, Scanner sc) {

        User user = null;
        int option = 0, option2 = 0;
        while (true) {
            System.out.println(
                    "\n" +
                    "[Stats Menu] Choose an option:\n" +
                    "(1) The user with most calories burned\n" +
                    "(2) The user with the most completed activities\n" +
                    "(3) The type of activity most practiced by the users\n" +
                    "(4) How many km’s were traveled by one user\n" +
                    "(5) How many meters of altimetry were climbed by one user\n" +
                    "(6) Whats the practice plan with more calories burned\n" +
                    "(7) List the activities of a user\n" +
                    "(8) Return to main menu");

            System.out.print("Option: ");
            option = readInt(sc);

            switch (option) {
                case 1:
                    // 1. The user with most calories burned
                    displayStatsSubMenu();
                    System.out.print("Option: ");
                    option2 = readInt(sc);

                    switch (option2) {
                        case 1:
                            user = m.mostCaloriesBurned();
                            if (user == null) {
                                System.out.println("No users found.");
                                break;
                            }
                            System.out.println("The user with most calories burned is: " + user.getName());
                            break;
                        case 2:
                            LocalDate start = insertStatisticsDate(sc, true);
                            LocalDate end = insertStatisticsDate(sc, false);
                            if (start.isAfter(end)) {
                                System.out.println("Invalid date range, start date is after end date.");
                                break;
                            }
                            user = m.mostCaloriesBurned(start, end);
                            if (user == null) {
                                System.out.println("No users found.");
                                break;
                            }
                            System.out.println("The user with most calories burned between "
                                + start + " and " + end + " is: " + user.getName());
                            break;
                        case 3:
                            break;
                        default:
                            System.out.println("Invalid option, try again.");
                    }
                    break;
                case 2:
                    // 2. The user with the most activities (registered/completed)
                    displayStatsSubMenu();
                    System.out.print("Option: ");
                    option2 = readInt(sc);

                    switch (option2) {
                        case 1:
                            user = m.mostActivities();
                            if (user == null) {
                                System.out.println("No users found.");
                                break;
                            }
                            System.out.println("The user with most activities is: "
                                + user.getName() + " with " + user.getRegisters().size() + " activities.");
                            break;
                        case 2:
                            LocalDate start = insertStatisticsDate(sc, true);
                            LocalDate end = insertStatisticsDate(sc, false);
                            if (start.isAfter(end)) {
                                System.out.println("Invalid date range, start date is after end date.");
                                break;
                            }

                            user = m.mostActivities(start, end);
                            if (user == null) {
                                System.out.println("No users found.");
                                break;
                            }
                            System.out.println("The user with most activities between "
                                + start + " and " + end + " is: " + user.getName() + " with "
                                + user.getRegisters().size() + " activities.");
                            break;
                        case 3:
                            break;
                        default:
                            System.out.println("Invalid option, try again.");
                    }

                    break;
                case 3:
                    // 3. The type of activity most practiced by the users
                    String activity = m.mostPracticedActivityType();
                    System.out.println("The type of activity most practiced by the users is: " + activity);
                    break;
                case 4:
                    // 4. How many km’s were traveled by one user
                    // Select an user to get the km traveled
                    try {
                        user = searchUserIO(m, sc);
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    displayStatsSubMenu();
                    System.out.print("Option: ");
                    option2 = readInt(sc);

                    switch (option2) {
                        case 1:
                            double km = m.kmTraveled(user);
                            System.out.println("The user " + user.getName() + " has traveled " + km + " km.");
                            break;
                        case 2:
                            LocalDate start = insertStatisticsDate(sc, true);
                            LocalDate end = insertStatisticsDate(sc, false);
                            if (start.isAfter(end)) {
                                System.out.println("Invalid date range, start date is after end date.");
                                break;
                            }

                            km = m.kmTraveled(user, start, end);
                            System.out.println("The user " + user.getName() + " has traveled " + km + " km between "
                                + start + " and " + end + ".");
                            break;
                        case 3:
                            break;
                        default:
                            System.out.println("Invalid option, try again.");
                        }

                    break;
                case 5:
                    // 5. How many meters of altimetry were climbed by one user
                    // Select an user to get the altimetry climbed
                    try {
                        user = searchUserIO(m, sc);
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    displayStatsSubMenu();
                    System.out.print("Option: ");
                    option2 = readInt(sc);

                    switch (option2) {
                        case 1:
                            int altimetry = m.altimetryClimbed(user);
                            System.out.println("The user " + user.getName() + " has climbed "
                                    + altimetry + " meters.");
                            break;
                        case 2:
                            LocalDate start = insertStatisticsDate(sc, true);
                            LocalDate end = insertStatisticsDate(sc, false);
                            if (start.isAfter(end)) {
                                System.out.println("Invalid date range, start date is after end date.");
                                break;
                            }

                            altimetry = m.altimetryClimbed(user, start, end);
                            System.out.println("The user " + user.getName() + " has climbed "
                                + altimetry + " meters between "
                                + start + " and " + end + ".");
                            break;
                        case 3:
                            break;
                        default:
                            System.out.println("Invalid option, try again.");
                    }
                    break;
                case 6:
                    // 6. Whats the practice plan with more calories burned
                    System.out.println(m.mostCaloriesBurnedPlan());
                    break;
                case 7:
                    // 7. List the activities of a user
                    // Select an user to list the activities
                    try {
                        user = searchUserIO(m, sc);
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println(m.listActivities(user));
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    private void displayStatsSubMenu() {
        System.out.println("Choose an option:");
        System.out.println("(1) All time");
        System.out.println("(2) In a period of time");
        System.out.println("(3) Return to stats menu");
    }

    private void exit(ActivityPlanner m, Scanner sc) {

        if (m.getUpdatedState()) {
            System.out.print("Do you want to save the current state before exiting? [y/n]: ");
            String save = readYesNo(sc);
            if (save.equals("y")) {
                try {
                    m.saveState();
                }
                catch (StateNotSavedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.println("Exiting...");
        sc.close();
        System.exit(0);
    }

    /* user perspective menu TODO */
    private void userMenu(ActivityPlanner m, Scanner sc, User user) {}

    /* user IO methods */
    private String enterUserName(Scanner sc) {

        String name;
        while (true) {
            System.out.print("Enter user full name: ");
            name = readString(sc);
            // check if name is between 3 and 256 characters
            if (name.length() < User.MIN_NAME_LENGTH || name.length() > User.MAX_NAME_LENGTH) {
                System.out.println("Name must be between " + User.MIN_NAME_LENGTH +
                        " and " + User.MAX_NAME_LENGTH + " characters.");
                continue;
            }
            break;
        }
        return name;
    }

    private String enterUserEmail(Scanner sc, ArrayList<String> emails) { // TODO get emails from m

        String email;
        while (true) {
            System.out.print("Enter the user email: ");
            String emailBuffer = readString(sc);
            // check if email is valid using regex
            if (!emailBuffer.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                System.out.println("Email is not valid.");
                continue;
            }
            // check if email is 320 characters or less
            if (emailBuffer.length() > User.MAX_EMAIL_LENGTH) {
                System.out.println("Email must be " + User.MAX_EMAIL_LENGTH + " characters or less.");
                continue;
            }
            // check if email is unique
            boolean isUnique = emails.stream()
                .noneMatch(entry -> entry.equals(emailBuffer));
            if (!isUnique) {
                System.out.println("Email already exists.");
                continue;
            }
            email = emailBuffer;
            break;
        }
        return email;
    }

    private String enterUserAddress(Scanner sc) {

        String address;
        while (true) {
            System.out.print("Enter the user address: ");
            address = readString(sc);
            // check if address is between 5 and 1024 characters
            if (address.length() < User.MIN_ADDRESS_LENGTH || address.length() > User.MAX_ADDRESS_LENGTH) {
                System.out.println("Address must be between " + User.MIN_ADDRESS_LENGTH +
                        " and " + User.MAX_ADDRESS_LENGTH + " characters.");
                continue;
            }
            break;
        }
        return address;
    }

    private int enterUserHeartRate(Scanner sc) {

        int heartRate;
        while (true) {
            System.out.print("Enter the user resting heart rate (in BPM): ");
            heartRate = readInt(sc);

            // check if heart rate is between 20 and 200
            if (User.MIN_HEART_RATE > heartRate || heartRate > User.MAX_HEART_RATE) {
                System.out.println("Heart rate must be between " + User.MIN_HEART_RATE +
                        " and " + User.MAX_HEART_RATE + " BPM.");
                continue;
            }
            break;
        }
        return heartRate;
    }

    private int enterUserWeight(Scanner sc) {

        int weight;
        while (true) {
            System.out.print("Enter the user weight (in kg): ");
            weight = readInt(sc);

            // check if weight is between 20 and 200
            if (User.MIN_WEIGHT > weight || weight > User.MAX_WEIGHT) {
                System.out.println("Weight must be between " + User.MIN_WEIGHT +
                        " and " + User.MAX_WEIGHT + " kg.");
                continue;
            }
            break;
        }
        return weight;
    }

    private int enterUserHeight(Scanner sc) {

        int height;
        while (true) {
            System.out.print("Enter the user height (in cm): ");
            height = readInt(sc);

            // check if height is between 100 and 220
            if (User.MIN_HEIGHT > height || height > User.MAX_HEIGHT) {
                System.out.println("Height must be between " + User.MIN_HEIGHT +
                        " and " + User.MAX_HEIGHT + " cm.");
                continue;
            }
            break;
        }
        return height;
    }

    private User.Type enterUserType(Scanner sc) {

        while (true) {
            System.out.println("Possible user types:");
            System.out.println("  1 - OCCASIONAL");
            System.out.println("  2 - AMATEUR");
            System.out.println("  3 - PROFESSIONAL");
            // System.out.println("  4 - OLYMPIC");

            System.out.print("Enter the user type: ");
            int typeCode = readInt(sc);

            // check if user type is valid
            if (typeCode < 1 || typeCode > 3) {
                System.out.println("Invalid user type.");
                continue;
            }

            switch (typeCode) {
                case 1:
                    return User.Type.OCCASIONAL;
                case 2:
                    return User.Type.AMATEUR;
                case 3:
                    return User.Type.PROFESSIONAL;
            }
            break;
        }
        return null;
    }

    private User createUser(Scanner sc, ArrayList<String> emails, int id) {

        String name = enterUserName(sc);

        String email = enterUserEmail(sc, emails);

        String address = enterUserAddress(sc);

        int heartRate = enterUserHeartRate(sc);

        int weight = enterUserWeight(sc);

        int height = enterUserHeight(sc);

        User.Type type = enterUserType(sc);

        return new User(id, name, email, address, heartRate, weight, height, type);
    }

    // if 1 id, if 2 email
    private int chooseHowToSearchUser(Scanner sc) {

        int option;
        while (true) {
            System.out.println();
            System.out.println("Chose how to search for an user:");
            System.out.println("(1) By ID");
            System.out.println("(2) By email");

            System.out.print("Option: ");
            option = readInt(sc);

            if (!(option == 1 || option == 2)) {
                System.out.println("Invalid option.");
                continue;
            }
            break;
        }
        return option;
    }

    private User searchUserIO(ActivityPlanner m, Scanner sc) throws UserNotFoundException {

        User user = null;

        int option = chooseHowToSearchUser(sc);

        if (option == 1) {
            System.out.print("Enter the user ID: ");
            int id = readInt(sc);
            if (id == -1) {
                System.out.println("Invalid ID.");
                throw new UserNotFoundException("User not found.");
            }
            user = m.searchUser(id);
        } else {
            System.out.print("Enter the user email: ");
            String email = readString(sc);
            user = m.searchUser(email);
        }

        if (user == null) {
            throw new UserNotFoundException("User not found.");
        }

        System.out.println("Selected user: " + user.getName());
        return user;
    }

    private void editUser(Scanner sc, ArrayList<String> emails, User user) {

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

            int option = readInt(sc);

            switch (option) {
                case 1:
                    String name = enterUserName(sc);
                    user.setName(name);
                    System.out.println("Name updated successfully.");
                    break;

                case 2:
                    String email = enterUserEmail(sc, emails);
                    user.setEmail(email);
                    System.out.println("Email updated successfully.");
                    break;

                case 3:
                    String address = enterUserAddress(sc);
                    user.setAddress(address);
                    System.out.println("Address updated successfully.");
                    break;

                case 4:
                    int heartRate = enterUserHeartRate(sc);
                    user.setHeartRate(heartRate);
                    System.out.println("Heart rate updated successfully.");
                    break;
                case 5:
                    int weight = enterUserWeight(sc);
                    user.setWeight(weight);
                    System.out.println("Weight updated successfully.");
                    break;
                case 6:
                    int height = enterUserHeight(sc);
                    user.setHeight(height);
                    System.out.println("Height updated successfully.");
                    break;
                case 7:
                    User.Type type = enterUserType(sc);
                    user.setType(type);
                    System.out.println("Type updated successfully.");
                    break;
                case 8:
                    // Go back
                    return;
            }
        }
    }

    /* atomic IO methods */
    /* private int inputToSearchUserById(sc, ArrayList<Integer> ids) */
    /* private String inputToSearchUserByEmail(sc, ArrayList<String> emails) */
    /* private String inputToSearchActivityByName(sc, ArrayList<String> names) */
    /* private String inputToSearchPlanByName(sc, ArrayList<String> names) */

    private LocalDate getSimulationEndDate(Scanner sc, LocalDate startDate) {

        LocalDate endDate = null;
        while (true) {
            System.out.print("Enter the end date of the simulation (yyyy-mm-dd): ");

            endDate = readDate(sc);
            if (endDate == null) {
                continue;
            }

            if (endDate.isBefore(startDate)) {
                System.out.println("The end date must be after the start date.");
                continue;
            }
            break;
        }
        return endDate;
    }

    private LocalDate insertStatisticsDate(Scanner sc, boolean start) {

        LocalDate date = null;
        while (true) {
            System.out.print("Insert the " + (start ? "start" : "end") + " date (yyyy-mm-dd): ");

            date = readDate(sc);
            if (date == null) {
                continue;
            }

            break;
        }
        return date;
    }

    /* simple IO methods */

    // TODO change to private
    public String readString(Scanner sc) {
        String option = "";
        try {
            option = sc.nextLine();
        }
        catch (NoSuchElementException e) {
            System.out.println("\nEOF detected, exiting...");
            sc.close();
            System.exit(0);
        }
        catch (IllegalStateException e) {
            System.out.println("Scanner is closed, exiting...");
            System.exit(0);
        }
        return option;
    }

    // TODO change to private
    public int readInt(Scanner sc) {
        int option = 0;
        try {
            option = Integer.parseInt(readString(sc));
        }
        catch (NumberFormatException e) {
            return -1;
        }

        return option;
    }

    // TODO change to private
    public String readYesNo(Scanner sc) {
        String option = "";
        while (true) {
            option = readString(sc).toLowerCase();
            if (option.equals("y") || option.equals("n")) {
                break;
            }
            System.out.print("Invalid option. Please enter 'y' or 'n': ");
        }
        return option;
    }

    private LocalDate readDate(Scanner sc) {
        LocalDate date = null;
        try {
            date = LocalDate.parse(readString(sc));
        }
        catch (DateTimeParseException e) {
            System.out.println("Invalid date format.");
            return null;
        }
        return date;
    }

    /* state management methods with IO */
    private void saveStateIO(ActivityPlanner m) {
        try {
            System.out.println("Saving state to " + m.getStateFilepath());
            m.saveState();
            System.out.println("State saved successfully, " + m.getUsersSize() + " users saved.");
        }
        catch (StateNotSavedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadStateIO(ActivityPlanner m, Scanner sc) {

        if (m.getUpdatedState()) {
            System.out.print("Warning: current state will be lost. Do you want to continue? [y/n]: ");
            String cont = readYesNo(sc);
            if (cont.equals("n")) {
                return;
            }
        }

        try {
            System.out.println("Loading state from " + m.getStateFilepath());
            m.loadState();
            System.out.println("State loaded successfully, " + m.getUsersSize() + " users loaded.");
        }
        catch (StateNotLoadedException e) {
            System.out.println(e.getMessage());
        }
    }
}

package src;

import src.exceptions.*;

import java.util.Scanner;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Controller {

    private static final int MAX_TRIES = 3;

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
                        "Usage: java src.Controller [--load <file>] [--user <--id|--email> <id|email>]\n" +
                        "Options:\n" +
                        "  -h --help: show this help message\n" +
                        "  -l --load: load a program state from file\n" +
                        "  -u --user: \n" +
                        "select an user by id or email to view his perspective\n" +
                        "  -e --email: select an user by email (use with --user option after --load option)\n" +
                        "  -i --id: select an user by ID (use with --user option after --load option)");
                System.exit(0);
            }
            else if (args[i].equals("--load") || args[i].equals("-l")) {
                c.m.setStateFilepath(args[++i]);
                c.loadStateIO();
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
                c.userMenu(user);
            }
        }
        else {
            System.out.println("Welcome to Activity Planner!");
            // Main menu
            while (true) {
                c.mainMenu();
            }
        }
    }

    /* main menu */
    private void mainMenu() {
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
        int option = readInt();

        switch (option) {
            case 1:
                // Manage users
                this.manageUserSubMenu();
                break;
            case 2:
                // Manage user activities
                this.manageUserActivitiesSubMenu();
                break;
            case 3:
                // Manage user registered activities
                this.manageUserRegisteredActivitiesSubMenu();
                break;
            case 4:
                // Manage user plan
                this.manageUserPlanSubMenu();
                break;
            case 5:
                // Simulation
                this.simulationSubMenu();
                break;
            case 6:
                // Statistics
                this.statisticsSubMenu();
                break;
            case 7:
                // Save program state
                this.saveStateIO();
                break;
            case 8:
                // Load program state
                this.loadStateIO();
                break;
            case 9:
                // Exit
                this.exit();
            default:
                System.out.println("Invalid option");
                break;
        }
    }
    /* sub-menus */
    private void manageUserSubMenu() {

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
            int option = readInt();

            User user = null;
            ArrayList<String> emails = this.m.getUsersEmails();
            ArrayList<String> types = this.m.getUserTypes();

            switch (option) {
                case 1:
                    // Create an user
                    int id = this.m.getNextUserId();
                    user = createUser(emails, types, id);
                    this.m.addUser(user);
                    System.out.println("User created successfully. (ID: " + user.getId() + ")");
                    break;
                case 2:
                    // Delete an user
                    try {
                        user = searchUserIO();
                        this.m.removeUser(user);
                        System.out.println("User deleted successfully.");
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    // View an user
                    try {
                        user = searchUserIO();
                        System.out.println(user);
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    // View all users
                    ArrayList<User> users = new ArrayList<>(this.m.getUsers().values());
                    for (User u : users) {
                        System.out.println(u);
                    }
                    break;
                case 5:
                    // Edit an user
                    try {
                        user = searchUserIO();
                        editUser(emails, user);
                        this.m.updateUser(user);
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

    private void manageUserActivitiesSubMenu() {

        // Select an user to manage activities
        User user = null;
        try {
            user = searchUserIO();
        }
        catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        // User activities submenu
        while (true) {
            System.out.println(
                "\n" +
                "[Manage user activities menu] Please select an option:\n" +
                "(1) Create an activity\n" +
                "(2) Delete an activity\n" +
                "(3) View an activity\n" +
                "(4) View all activities\n" +
                "(5) Back to main menu");

            System.out.print("Option: ");
            int option = readInt();

            ArrayList<Activity> activities = user.getActivities();
            ArrayList<String> activitiesNames = this.m.getActivitiesNames(user);
            ArrayList<String> activitiesAvailable = this.m.getActivities();
            Activity a;

            switch (option) {
                case 1:
                    // Create an activity
                    Activity activity = createActivity(activitiesNames, activitiesAvailable);
                    if (activity == null) {
                        System.out.println("Activity not created.");
                        break;
                    }
                    // Add activity to user
                    user.addActivity(activity);
                    this.m.updateUser(user);
                    System.out.println("Activity created successfully.");
                    break;
                case 2:
                    if (activities.size() == 0) {
                        System.out.println("The selected user has no activities.");
                        break;
                    }

                    // Delete an activity
                    try {
                        String activityName = searchActivityIO(activitiesNames);
                        a = this.m.getUserActivity(activities, activityName);
                    }
                    catch (ActivityNotFoundException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    try {
                        user.deleteActivity(a);
                    }
                    catch (ActivityIsRegisteredException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    this.m.updateUser(user);
                    System.out.println("Activity deleted successfully.");
                    break;
                case 3:
                    if (activities.size() == 0) {
                        System.out.println("The selected user has no activities.");
                        break;
                    }

                    // View an activity
                    try {
                        String activityName = searchActivityIO(activitiesNames);
                        a = this.m.getUserActivity(activities, activityName);
                    }
                    catch (ActivityNotFoundException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    System.out.println(a);
                    break;
                case 4:
                    // View all activities
                    if (activities.size() == 0) {
                        System.out.println("The selected user has no activities.");
                        break;
                    }
                    for (Activity t : activities) System.out.println(t);
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

    private void manageUserRegisteredActivitiesSubMenu() {

        // Select an user to add a registered activity or view them
        User user = null;
        try {
            user = searchUserIO();
        }
        catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        while (true) {
            // Manage user registed activities
            System.out.println(
                "\n" +
                "[Manage user registered activities menu] Please select an option:\n" +
                "(1) Register an activity\n" +
                "(2) View registered activities\n" +
                "(3) Back to main menu");

            System.out.print("Option: ");
            int option = readInt();

            switch (option) {
                case 1:
                    user = registerActivityIO(user);
                    this.m.updateUser(user);
                    break;
                case 2:
                    // View registered activities
                    // Display registers sorted by date
                    HashMap<LocalDateTime, Activity> registers = user.getRegisters();
                    List<LocalDateTime> sortedDates = new ArrayList<>(registers.keySet());
                    Collections.sort(sortedDates);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                    for (LocalDateTime date : sortedDates) {
                        System.out.println(date.format(formatter) + ": {");
                        System.out.println(registers.get(date));
                        System.out.println("}");
                    }
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

    private void manageUserPlanSubMenu() {

        // Select an user to manage plan
        User user = null;
        try {
            user = searchUserIO();
        }
        catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        while (true) {
            // Manage user plan
            System.out.println(
                "\n" +
                "[Manage user plan menu] Please select an option:\n" +
                "(1) Create plan interactively\n" +
                "(2) Create plan based on user goals\n" +
                "(3) Delete plan\n" +
                "(4) View plan\n" +
                "(5) Back to main menu");

            System.out.print("Option: ");
            int option = readInt();

            ArrayList<Activity> userActivities = user.getActivities();
            Plan p = null;

            switch (option) {
                case 1:
                    // create plan interactively
                    // delete plan if it already exists
                    Plan old = user.getPlan();
                    if (old != null) {
                        System.out.println("A plan already exists for the selected user.");
                        System.out.print("Do you want to overwrite the current plan? [y/n]: ");
                        String delete = readYesNo();
                        if (delete.equals("y")) {
                            user.setPlan(null);
                            System.out.println("Plan deleted successfully.");
                        }
                        else {
                            System.out.println("Plan wasn't deleted.");
                            break;
                        }
                    }

                    p = createPlan(userActivities);
                    user.setPlan(p);
                    if (old == null && p == null) {
                        System.out.println("Plan not created.");
                        break;
                    }
                    System.out.println("Plan created successfully.");
                    this.m.updateUser(user);
                    break;
                case 2:
                    // Create plan based on user goals
                    Plan oldPlan = user.getPlan();
                    if (oldPlan != null) {
                        System.out.print("Do you want to overwrite the user current plan? [y/n]: ");
                        String delete = readYesNo();
                        if (delete.equals("y")) {
                            user.setPlan(null);
                            System.out.println("Plan deleted successfully.");
                        }
                        else {
                            System.out.println("Plan wasn't deleted.");
                            break;
                        }
                    }

                    createPlanBasedOnGoalsIO(user);
                    break;
                case 3:
                    // Delete plan
                    if (user.getPlan() == null) {
                        System.out.println("The selected user has no plan.");
                        break;
                    }
                    user.setPlan(null);
                    System.out.println("Plan deleted successfully.");
                    this.m.updateUser(user);
                    break;
                case 4:
                    // View plan
                    p = user.getPlan();
                    if (p == null) {
                        System.out.println("The selected user has no plan.");
                        break;
                    }
                    System.out.print(p);
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

    /* Simulation menu */

    private void simulationSubMenu() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = getSimulationEndDate(startDate);
        System.out.println(this.m.runSimulation(startDate, endDate));
    }

    /* statistics menu */

    private void statisticsSubMenu() {

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
            option = readInt();

            switch (option) {
                case 1:
                    // 1. The user with most calories burned
                    this.displayStatsSubMenu();
                    System.out.print("Option: ");
                    option2 = readInt();

                    switch (option2) {
                        case 1:
                            user = this.m.mostCaloriesBurned();
                            if (user == null) {
                                System.out.println("No users found.");
                                break;
                            }
                            System.out.println("The user with most calories burned is: " + user.getName());
                            break;
                        case 2:
                            LocalDate start = this.insertStatisticsDate(true);
                            LocalDate end = this.insertStatisticsDate(false);
                            if (start.isAfter(end)) {
                                System.out.println("Invalid date range, start date is after end date.");
                                break;
                            }
                            user = this.m.mostCaloriesBurned(start, end);
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
                    this.displayStatsSubMenu();
                    System.out.print("Option: ");
                    option2 = readInt();

                    switch (option2) {
                        case 1:
                            user = this.m.mostActivities();
                            if (user == null) {
                                System.out.println("No users found.");
                                break;
                            }
                            System.out.println("The user with most activities is: "
                                + user.getName() + " with " + user.getRegisters().size() + " activities.");
                            break;
                        case 2:
                            LocalDate start = this.insertStatisticsDate(true);
                            LocalDate end = this.insertStatisticsDate(false);
                            if (start.isAfter(end)) {
                                System.out.println("Invalid date range, start date is after end date.");
                                break;
                            }

                            user = this.m.mostActivities(start, end);
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
                    String activity = this.m.mostPracticedActivityType();
                    System.out.println("The type of activity most practiced by the users is: " + activity);
                    break;
                case 4:
                    // 4. How many km’s were traveled by one user
                    // Select an user to get the km traveled
                    try {
                        user = searchUserIO();
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    this.displayStatsSubMenu();
                    System.out.print("Option: ");
                    option2 = readInt();

                    switch (option2) {
                        case 1:
                            double km = this.m.kmTraveled(user);
                            System.out.println("The user " + user.getName() + " has traveled " + km + " km.");
                            break;
                        case 2:
                            LocalDate start = this.insertStatisticsDate(true);
                            LocalDate end = this.insertStatisticsDate(false);
                            if (start.isAfter(end)) {
                                System.out.println("Invalid date range, start date is after end date.");
                                break;
                            }

                            km = this.m.kmTraveled(user, start, end);
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
                        user = searchUserIO();
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    this.displayStatsSubMenu();
                    System.out.print("Option: ");
                    option2 = readInt();

                    switch (option2) {
                        case 1:
                            int altimetry = this.m.altimetryClimbed(user);
                            System.out.println("The user " + user.getName() + " has climbed "
                                    + altimetry + " meters.");
                            break;
                        case 2:
                            LocalDate start = this.insertStatisticsDate(true);
                            LocalDate end = this.insertStatisticsDate(false);
                            if (start.isAfter(end)) {
                                System.out.println("Invalid date range, start date is after end date.");
                                break;
                            }

                            altimetry = this.m.altimetryClimbed(user, start, end);
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
                    System.out.println(this.m.mostCaloriesBurnedPlan());
                    break;
                case 7:
                    // 7. List the activities of a user
                    // Select an user to list the activities
                    try {
                        user = searchUserIO();
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println(this.m.listActivities(user));
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

    private void exit() {

        if (this.m.getUpdatedState()) {
            System.out.print("Do you want to save the current state before exiting? [y/n]: ");
            String save = readYesNo();
            if (save.equals("y")) {
                try {
                    this.m.saveState();
                }
                catch (StateNotSavedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.println("Exiting...");
        this.sc.close();
        System.exit(0);
    }

    /* user perspective menu */
    private void userMenu(User user) {
        System.out.println(
            "\n" +
            "[User menu] Please select an option:\n" +
            "( 1) View your profile\n" +
            "( 2) Edit your profile\n" +
            "( 3) Create an activity\n" +
            "( 4) Delete an activity\n" +
            "( 5) View an activity\n" +
            "( 6) View all activities\n" +
            "( 7) Register activity\n" +
            "( 8) View registered activities\n" +
            "( 9) Create your plan\n" +
            "(10) Create plan based on your goals\n" +
            "(11) Delete your plan\n" +
            "(12) View your plan\n" +
            "(13) Statistics menu\n" +
            "(14) Save program state\n" +
            "(15) Load program state\n" +
            "(16) Exit");

        System.out.print("Option: ");
        int option = readInt();

        ArrayList<String> emails = this.m.getUsersEmails();
        ArrayList<String> activitiesNames = this.m.getActivitiesNames(user);
        ArrayList<String> activitiesAvailable = this.m.getActivities();

        Plan p = null;

        Activity a = null;
        ArrayList<Activity> activities = user.getActivities();

        switch (option) {
            case 1:
                // View your profile
                System.out.println(user);
                break;
            case 2:
                // Edit your profile
                editUser(emails, user);
                this.m.updateUser(user);
                System.out.println("Edited your profile successfully.");
                break;
            case 3:
                // Create an activity
                Activity activity = createActivity(activitiesNames, activitiesAvailable);
                if (activity == null) {
                    System.out.println("Activity not created.");
                    break;
                }
                // Add activity to user
                user.addActivity(activity);
                this.m.updateUser(user);
                System.out.println("Activity created successfully.");
                break;
            case 4:
                // Delete an activity
                if (activities.size() == 0) {
                    System.out.println("You have no activities.");
                    break;
                }

                // Delete an activity
                try {
                    String activityName = searchActivityIO(activitiesNames);
                    a = this.m.getUserActivity(activities, activityName);
                }
                catch (ActivityNotFoundException e) {
                    System.out.println(e.getMessage());
                    break;
                }

                try {
                    user.deleteActivity(a);
                }
                catch (ActivityIsRegisteredException e) {
                    System.out.println(e.getMessage());
                    break;
                }
                this.m.updateUser(user);
                System.out.println("Activity deleted successfully.");
                break;
            case 5:
                // View an activity
                if (activities.size() == 0) {
                    System.out.println("You have no activities.");
                    break;
                }

                // View an activity
                try {
                    String activityName = searchActivityIO(activitiesNames);
                    a = this.m.getUserActivity(activities, activityName);
                }
                catch (ActivityNotFoundException e) {
                    System.out.println(e.getMessage());
                    break;
                }
                System.out.println(a);
                break;
            case 6:
                // View all activities
                if (activities.size() == 0) {
                    System.out.println("The selected user has no activities.");
                    break;
                }
                for (Activity t : activities) System.out.println(t);
                break;
            case 7:
                // Register activity
                user = registerActivityIO(user);
                this.m.updateUser(user);
                break;
            case 8:
                // View registered activities
                // Display registers sorted by date
                HashMap<LocalDateTime, Activity> registers = user.getRegisters();
                List<LocalDateTime> sortedDates = new ArrayList<>(registers.keySet());
                Collections.sort(sortedDates);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                for (LocalDateTime date : sortedDates) {
                    System.out.println(date.format(formatter) + ": {");
                    System.out.println(registers.get(date));
                    System.out.println("}");
                }
                break;
            case 9:
                // create plan interactively
                // delete plan if it already exists
                Plan old = user.getPlan();
                if (old != null) {
                    System.out.println("A plan already exists.");
                    System.out.print("Do you want to overwrite your current plan? [y/n]: ");
                    String delete = readYesNo();
                    if (delete.equals("y")) {
                        user.setPlan(null);
                        System.out.println("Plan deleted successfully.");
                    }
                    else {
                        System.out.println("Plan wasn't deleted.");
                        break;
                    }
                }

                p = createPlan(activities);
                user.setPlan(p);
                if (old == null && p == null) {
                    System.out.println("Plan not created.");
                    break;
                }
                System.out.println("Plan created successfully.");
                this.m.updateUser(user);
                break;
            case 10:
                // Create plan based on user goals
                Plan oldPlan = user.getPlan();
                if (oldPlan != null) {
                    System.out.print("Do you want to overwrite your current plan? [y/n]: ");
                    String delete = readYesNo();
                    if (delete.equals("y")) {
                        user.setPlan(null);
                        System.out.println("Plan deleted successfully.");
                    }
                    else {
                        System.out.println("Plan wasn't deleted.");
                        break;
                    }
                }

                createPlanBasedOnGoalsIO(user);
                break;
            case 11:
                // Delete plan
                if (user.getPlan() == null) {
                    System.out.println("You have no plan already.");
                    break;
                }
                user.setPlan(null);
                System.out.println("Plan deleted successfully.");
                this.m.updateUser(user);
                break;
            case 12:
                // View your plan
                p = user.getPlan();
                if (p == null) {
                    System.out.println("The selected user has no plan.");
                    break;
                }
                System.out.print(p);
                break;
            case 13:
                // Statistics menu
                this.statisticsSubMenu();
                break;
            case 14:
                // Save program state
                this.saveStateIO();
                break;
            case 15:
                // Load program state
                this.loadStateIO();
                break;
            case 16:
                // Exit
                this.exit();
                break;
            default:
                System.out.println("Invalid option");
                break;
        }
    }

    /* user IO methods */
    private String enterUserName() {

        int maxTries = MAX_TRIES;

        String name;
        while (true) {
            System.out.print("Enter user full name: ");
            name = readString();
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

    private String enterUserEmail(ArrayList<String> emails) {

        String email;
        while (true) {
            System.out.print("Enter the user email: ");
            String emailBuffer = readString();
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

    private String enterUserAddress() {

        String address;
        while (true) {
            System.out.print("Enter the user address: ");
            address = readString();
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

    private int enterUserHeartRate() {

        int heartRate;
        while (true) {
            System.out.print("Enter the user resting heart rate (in BPM): ");
            heartRate = readInt();

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

    private int enterUserWeight() {

        int weight;
        while (true) {
            System.out.print("Enter the user weight (in kg): ");
            weight = readInt();

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

    private int enterUserHeight() {

        int height;
        while (true) {
            System.out.print("Enter the user height (in cm): ");
            height = readInt();

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

    private String enterUserType(ArrayList<String> types) {

        int N = types.size();

        String result = null;

        while (true) {
            System.out.println("Possible user types:");

            for (int i = 0; i < N; i++) {
                System.out.println("  " + (i + 1) + " - " + types.get(i));
            }

            System.out.print("Enter the user type: ");
            int typeCode = readInt();

            // check if user type is valid
            if (typeCode < 1 || typeCode > N) {
                System.out.println("Invalid user type.");
                continue;
            }

            result = types.get(typeCode - 1);
            break;
        }
        return result;
    }

    private User createUser(ArrayList<String> emails, ArrayList<String> types, int id) {

        String name = enterUserName();
        String email = enterUserEmail(emails);
        String address = enterUserAddress();
        int heartRate = enterUserHeartRate();
        int weight = enterUserWeight();
        int height = enterUserHeight();
        String type = enterUserType(types);

        // Now instantiate the user based on the type
        try {
            Class<?> userClass = Class.forName("src.users." + type);
            Constructor<?> constructor =
                userClass.getConstructor(int.class,
                                         String.class,
                                         String.class,
                                         String.class,
                                         int.class,
                                         int.class,
                                         int.class);
            return (User) constructor.newInstance(id, name, email, address, heartRate, weight, height);
        }
        catch (NoSuchMethodException |
            ClassNotFoundException |
            IllegalAccessException |
            InvocationTargetException |
            InstantiationException e
        ) {
            System.out.println("Error creating user: " + e.getMessage());
            return null;
        }
    }

    // if 1 id, if 2 email
    private int chooseHowToSearchUser() {

        int option;
        while (true) {
            System.out.println();
            System.out.println("Chose how to search for an user:");
            System.out.println("(1) By ID");
            System.out.println("(2) By email");

            System.out.print("Option: ");
            option = readInt();

            if (!(option == 1 || option == 2)) {
                System.out.println("Invalid option.");
                continue;
            }
            break;
        }
        return option;
    }

    private User searchUserIO() throws UserNotFoundException {

        User user = null;

        int option = chooseHowToSearchUser();

        if (option == 1) {
            System.out.print("Enter the user ID: ");
            int id = readInt();
            if (id == -1) {
                System.out.println("Invalid ID.");
                throw new UserNotFoundException("User not found.");
            }
            user = this.m.searchUser(id);
        } else {
            System.out.print("Enter the user email: ");
            String email = readString();
            user = this.m.searchUser(email);
        }

        if (user == null) {
            throw new UserNotFoundException("User not found.");
        }

        System.out.println("Selected user: " + user.getName());
        return user;
    }

    private void editUser(ArrayList<String> emails, User user) {

        while (true) {
            System.out.println(
                "\n" +
                "Chose what to edit:\n" +
                "(1) Name\n" +
                "(2) Email\n" +
                "(3) Address\n" +
                "(4) Heart rate\n" +
                "(5) Weight\n" +
                "(6) Height\n" +
                "(7) Go back");

            System.out.print("Option: ");
            int option = readInt();

            switch (option) {
                case 1:
                    String name = enterUserName();
                    user.setName(name);
                    System.out.println("Name updated successfully.");
                    break;
                case 2:
                    String email = enterUserEmail(emails);
                    user.setEmail(email);
                    System.out.println("Email updated successfully.");
                    break;
                case 3:
                    String address = enterUserAddress();
                    user.setAddress(address);
                    System.out.println("Address updated successfully.");
                    break;
                case 4:
                    int heartRate = enterUserHeartRate();
                    user.setHeartRate(heartRate);
                    System.out.println("Heart rate updated successfully.");
                    break;
                case 5:
                    int weight = enterUserWeight();
                    user.setWeight(weight);
                    System.out.println("Weight updated successfully.");
                    break;
                case 6:
                    int height = enterUserHeight();
                    user.setHeight(height);
                    System.out.println("Height updated successfully.");
                    break;
                case 7:
                    // Go back
                    return;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }

    /* Activity IO methods */
    private Activity createActivity(ArrayList<String> names, ArrayList<String> activities) {

        int maxTries = MAX_TRIES;

        // Get the activity sub-class from model
        String activity = null;
        int N = activities.size();
        while (--maxTries > 0) {
            System.out.println("Possible activities:");
            for (int i = 0; i < N; i++) {
                System.out.println("  " + (i + 1) + " - " + activities.get(i));
            }

            System.out.print("Enter the activity: ");
            int activityCode = readInt();

            // check if activity is valid
            if (activityCode < 1 || activityCode > N) {
                System.out.println("Invalid activity.");
                continue;
            }

            activity = activities.get(activityCode - 1);
            break;
        }

        if (maxTries == 0) {
            System.out.println("Activity not created.");
            return null;
        }

        maxTries = MAX_TRIES;

        // Get the Activity attributes
        String name = "";
        while (--maxTries > 0) {
            System.out.print("Enter the name of the activity: ");
            String nameBuffer = readString();

            // check if name is between 1 and 100 characters
            if (nameBuffer.length() < 1 || nameBuffer.length() > 100) {
                System.out.println("Activity name must be between 1 and 100 characters.");
                continue;
            }
            // check if name is unique
            boolean isUnique = names.stream().noneMatch(a -> a.equals(nameBuffer));
            if (!isUnique) {
                System.out.println("Activity name must be unique.");
                continue;
            }
            name = nameBuffer;
            break;
        }

        if (maxTries == 0) {
            System.out.println("Activity not created.");
            return null;
        }

        maxTries = MAX_TRIES;

        int duration = 0;
        while (--maxTries > 0) {
            System.out.print("Enter the duration of the activity in minutes: ");
            duration = readInt();

            // check if duration is between 1 and 1440 minutes
            if (duration < 1 || duration > 1440) {
                System.out.println("Duration must be between 1 and 1440 minutes.");
                continue;
            }
            break;
        }

        if (maxTries == 0) {
            System.out.println("Activity not created.");
            return null;
        }

        maxTries = MAX_TRIES;

        int intensity = 0;
        while (--maxTries > 0) {
            System.out.print("Enter the intensity of the activity (1-100): ");
            intensity = readInt();

            // check if intensity is between 1 and 100
            if (intensity < 1 || intensity > 100) {
                System.out.println("Intensity must be between 1 and 100.");
                continue;
            }
            break;
        }

        if (maxTries == 0) {
            System.out.println("Activity not created.");
            return null;
        }

        maxTries = MAX_TRIES;

        System.out.print("Is the activity hard? [y/n]: ");
        String hardBuffer = readYesNo();
        boolean hard = hardBuffer.equals("y");

        // Instantiate activity with current attributes
        Activity a = null;
        try {
            Class<?> activityClass = Class.forName("src.activities." + activity);
            Constructor<?> constructor =
                activityClass.getConstructor(String.class, int.class, int.class, boolean.class, int.class);
            a = (Activity) constructor.newInstance(name, duration, intensity, hard, 0);
        }
        catch (NoSuchMethodException |
            ClassNotFoundException |
            IllegalAccessException |
            InvocationTargetException |
            InstantiationException e
        ) {
            System.out.println("Error creating activity: " + e.getMessage());
            return null;
        }

        // Fill the dynamic attributes
        ArrayList<String> attributes = a.getAttributes();
        ArrayList<Integer> attributesToSet = new ArrayList<>();

        for (String attribute : attributes) {
            while (--maxTries > 0) {
                System.out.print("Enter the " + attribute + ": ");
                int value = readInt();
                if (value <= 0) {
                    System.out.println("Invalid value.");
                    continue;
                }
                attributesToSet.add(value);
                break;
            }

            if (maxTries == 0) {
                System.out.println("Activity not created.");
                return null;
            }

            maxTries = MAX_TRIES;
        }

        // Set the dynamic attributes
        a.setAttributes(attributesToSet);
        return a;
    }

    private String searchActivityIO(ArrayList<String> userActivitiesNames) throws ActivityNotFoundException {

        int maxTries = MAX_TRIES;
        String activity = null;
        while (maxTries-- > 0) {
            // Print activities names
            System.out.println("User activities:");
            for (String n : userActivitiesNames) {
                System.out.println("  -> " + n);
            }
            System.out.print("Enter the name of the activity: ");
            String name = this.readString();

            activity = userActivitiesNames.stream()
                .filter(n -> n.equals(name))
                .findFirst()
                .orElse(null);

            if (activity == null) {
                System.out.println("Activity not found.");
                continue;
            }

            return activity;
        }

        if (activity == null) {
            throw new ActivityNotFoundException("Activity not found.");
        }

        return activity;
    }

    /* Register IO methods */

    private User registerActivityIO(User u) {

        int maxTries = MAX_TRIES;

        Activity a = null;
        while (--maxTries > 0) {
            System.out.println("Choose an option:");
            System.out.println("(1) Register an existing activity");
            System.out.println("(2) Register a new activity");
            System.out.print("Option: ");

            int option = this.readInt();

            ArrayList<Activity> userActivities = u.getActivities();
            switch (option) {
                case 1:
                    if (userActivities.isEmpty()) {
                        System.out.println("No activities found.");
                        System.out.println("Please register a new activity.");
                        continue;
                    }

                    try {
                        String activityName = searchActivityIO(this.m.getActivitiesNames(u));
                        a = this.m.getUserActivity(userActivities, activityName);
                    }
                    catch (ActivityNotFoundException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                    break;
                case 2:
                    ArrayList<String> activitiesNames = this.m.getActivitiesNames(u);
                    ArrayList<String> activitiesAvailable = this.m.getActivities();

                    a = createActivity(activitiesNames, activitiesAvailable);
                    if (a == null) {
                        System.out.println("Activity not created.");
                        continue;
                    }
                    u.addActivity(a);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    continue;
            }
            break;
        }

        if (maxTries == 0) {
            System.out.println("Activity not registered.");
            return u;
        }

        maxTries = MAX_TRIES;

        // Enter date manually or use current date
        System.out.println("");
        System.out.println("Registering activity: " + a.getName());
        System.out.println("(1) Enter date manually");
        System.out.println("(2) Use current date");
        System.out.print("Option: ");

        int dateOption = this.readInt();

        LocalDateTime datetime = LocalDateTime.now();

        if (dateOption == 1) {
            while (--maxTries > 0) {
                System.out.print("Enter date (yyyy-mm-dd): ");
                String date = this.readString();

                System.out.print("Enter time (hh:mm): ");
                String time = this.readString();

                try {
                    datetime = LocalDateTime.parse(date + "T" + time);
                }
                catch (DateTimeParseException e) {
                    System.out.println("Invalid datetime format.");
                    continue;
                }

                if (!datetime.isBefore(LocalDateTime.now())) {
                    System.out.println("Date must be less than current date.");
                    continue;
                }
                break;
            }

            if (maxTries == 0) {
                System.out.println("Activity not registered.");
                return u;
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Create a new register
        Activity register = a.clone();

        int calories = register.calculateCalories(u);
        register.setCalories(calories);

        // Register an activity in the user
        u.register(datetime, register);

        // Print the registered activity
        System.out.println("Activity registered successfully: ");
        System.out.println(register.getName() + " on " + datetime.format(formatter));
        System.out.println(register.getCalories() + " calories burned.");

        return u;
    }

    /* Plan IO methods */

    private Plan createPlan(ArrayList<Activity> userActivities) {

        if (userActivities.isEmpty()) {
            System.out.println("There are no activities available.");
            System.out.println("Please add activities before creating a plan.");
            return null;
        }

        int maxActivities = Event.MAX_REPETITIONS;

        // Plan
        Plan plan = new Plan();

        // Get name of the plan
        System.out.print("Enter the name of the plan: ");
        String name = this.readString();
        plan.setName(name);

        // start on sunday and ask how many activities wants to add on that day, and so on
        int counterDaysOff = 0;
        for (int i = 1; i <= 7; i++) {

            int maxTries = MAX_TRIES;

            int activities = 0;
            while (--maxTries > 0) {
                System.out.print("How many activities do you want on " +
                    convertDayToString(i) + "? (0-" + maxActivities + "): ");

                activities = this.readInt();

                if (activities < 0 || activities > maxActivities) {
                    System.out.println("Invalid number of activities. " +
                        "Please enter a number between 0 and " + maxActivities + ".");
                    continue;
                }
                break;
            }

            if (maxTries == 0) {
                System.out.println("Plan not created.");
                return null;
            }

            if (activities == 0) {
                counterDaysOff++;
                continue;
            }

            int eventCount = 0;
            for (int j = 0; j < activities;) {

                System.out.println("Event number " + (++eventCount));

                Event e = createEvent(userActivities, activities - j, i);
                if (e == null) {
                    System.out.println("Event not created.");
                    continue;
                }

                plan.addEvent(e);
                j += e.getActivityRepetitions();
            }
        }

        if (counterDaysOff == 7) {
            System.out.println("You have scheduled no activities for the week.");
            System.out.println("You must be joking with the programmer kings.");
            return null;
        }

        return plan.clone();
    }

    private void createPlanBasedOnGoalsIO(User user) {
        if (user.getActivities().isEmpty()) {
            System.out.println("There are no activities available.");
            System.out.println("Please add activities before trying to generate a plan.");
            return;
        }

        int caloriesGoal = 0;
        int maxActivitiesPerDay = 0;
        int maxDisitinctActivitiesPerDay = 0;
        int nActivityRepetitionPerWeek = 0;
        ArrayList<Activity> selectedActivities = new ArrayList<Activity>();

        int maxTries = MAX_TRIES;

        while (--maxTries > 0) {
            System.out.print("Enter caloric goal per week: ");
            caloriesGoal = readInt();
            if (caloriesGoal < 0) {
                System.out.println("Invalid caloric goal. Please enter a positive number.");
                continue;
            }
            break;
        }

        if (maxTries == 0) {
            System.out.println("Plan not created.");
            return;
        }

        maxTries = MAX_TRIES;

        while (--maxTries > 0) {
            System.out.print("Enter max activities per day: ");
            maxActivitiesPerDay = readInt();
            if (maxActivitiesPerDay < 1 || maxActivitiesPerDay > 3) {
                System.out.println("Invalid number of activities. Please enter a number between 1 and 3.");
                continue;
            }
            break;
        }

        if (maxTries == 0) {
            System.out.println("Plan not created.");
            return;
        }

        maxTries = MAX_TRIES;

        while (--maxTries > 0) {
            System.out.print("Enter max distinct activities per day: ");
            maxDisitinctActivitiesPerDay = readInt();
            if (maxDisitinctActivitiesPerDay < 1 || maxDisitinctActivitiesPerDay > 3) {
                System.out.println("Invalid number of activities. Please enter a number between 1 and 3.");
                continue;
            }
            break;
        }

        if (maxTries == 0) {
            System.out.println("Plan not created.");
            return;
        }

        maxTries = MAX_TRIES;

        while (--maxTries > 0) {
            System.out.print("Enter number of repetitions of activities per week: ");
            nActivityRepetitionPerWeek = readInt();
            if (nActivityRepetitionPerWeek < 1) {
                System.out.println("Invalid number of repetitions. Please enter a number greater than 0.");
                continue;
            }
            break;
        }

        if (maxTries == 0) {
            System.out.println("Plan not created.");
            return;
        }

        maxTries = MAX_TRIES;

        while (--maxTries > 0) {
            ArrayList<Activity> userActivities = user.getActivities();
            System.out.print("Enter number of your activities to select (1-" + userActivities.size() + "): ");
            int nActivities = readInt();
            if (nActivities < 1 || nActivities > userActivities.size()) {
                System.out.println("Invalid number of activities. Please enter a number between 1 and " +
                    user.getActivities().size() + ".");
                continue;
            }

            ArrayList<Activity> toSearch = user.getActivities();

            for (int i = 0; i < nActivities; i++) {
                System.out.println("Select activity number " + (i + 1) + ".");

                Activity activity;
                ArrayList<String> activitiesNames = this.m.getActivitiesNames(toSearch);
                try {
                    String activityName = searchActivityIO(activitiesNames);
                    activity = this.m.getUserActivity(toSearch, activityName);
                }
                catch (ActivityNotFoundException e) {
                    System.out.println(e.getMessage());
                    i--;
                    continue;
                }
                selectedActivities.add(activity);
                toSearch.remove(activity);
            }
            break;
        }

        if (maxTries == 0) {
            System.out.println("Plan not created.");
            return;
        }

        String output = this.m.createPlanBasedOnGoals(
                            user,
                            caloriesGoal,
                            maxActivitiesPerDay,
                            maxDisitinctActivitiesPerDay,
                            nActivityRepetitionPerWeek,
                            selectedActivities);
       System.out.println(output);
    }

    /* Event IO methods */

    private Event createEvent(ArrayList<Activity> activities, int maxRepetitions, int day) {

        if (activities.size() == 0) {
            System.out.println("You have no activities to schedule an event for.");
            return null;
        }

        Activity activity;
        ArrayList<String> activitiesNames = this.m.getActivitiesNames(activities);
        try {
            String activityName = searchActivityIO(activitiesNames);
            activity = this.m.getUserActivity(activities, activityName);
        }
        catch (ActivityNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }

        int maxTries = MAX_TRIES;

        int activityRepetitions = 1;
        if (maxRepetitions != 1) {
            while (--maxTries > 0) {
                System.out.print("Enter the number of times you want to repeat " +
                    "the activity (1-" + maxRepetitions + "): ");
                activityRepetitions = this.readInt();

                if (activityRepetitions < 1 || activityRepetitions > maxRepetitions) {
                    System.out.println("Invalid number of repetitions. Please enter a number " +
                        "between 1 and " + maxRepetitions + ".");
                    continue;
                }
                break;
            }
        }

        if (maxTries == 0) {
            System.out.println("Event not created.");
            return null;
        }

        maxTries = MAX_TRIES;

        LocalTime time = null;
        while (--maxTries > 0) {
            System.out.print("Enter the time of the event (HH:mm): ");
            String timeBuffer = this.readString();
            try {
                time = LocalTime.parse(timeBuffer);
            }
            catch (DateTimeParseException e) {
                System.out.println("Invalid time format.");
                continue;
            }
            break;
        }

        if (maxTries == 0) {
            System.out.println("Event not created.");
            return null;
        }

        Event event = new Event(activity, activityRepetitions, day, time);
        System.out.println("Event scheduled successfully.");
        return event.clone();
    }

    /* Simulation IO methods */

    private LocalDate getSimulationEndDate(LocalDate startDate) {

        LocalDate endDate = null;
        while (true) {
            System.out.print("Enter the end date of the simulation (yyyy-mm-dd): ");

            endDate = readDate();
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

    /* Statistics IO methods */

    private LocalDate insertStatisticsDate(boolean start) {

        LocalDate date = null;
        while (true) {
            System.out.print("Insert the " + (start ? "start" : "end") + " date (yyyy-mm-dd): ");

            date = readDate();
            if (date == null) {
                continue;
            }

            break;
        }
        return date;
    }

    /* Utility static methods */

    public static String convertDayToString(int day) {
        switch (day) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "Invalid day";
        }
    }

    public static String convertTimeToString(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    /* simple IO methods */

    private String readString() {
        String option = "";
        try {
            option = this.sc.nextLine();
        }
        catch (NoSuchElementException e) {
            System.out.println("\nEOF detected, exiting...");
            this.sc.close();
            System.exit(0);
        }
        catch (IllegalStateException e) {
            System.out.println("Scanner is closed, exiting...");
            System.exit(0);
        }
        return option;
    }

    private int readInt() {
        int option = 0;
        try {
            option = Integer.parseInt(readString());
        }
        catch (NumberFormatException e) {
            return -1;
        }

        return option;
    }

    private String readYesNo() {
        String option = "";
        while (true) {
            option = readString().toLowerCase();
            if (option.equals("y") || option.equals("n")) {
                break;
            }
            System.out.print("Invalid option. Please enter 'y' or 'n': ");
        }
        return option;
    }

    private LocalDate readDate() {
        LocalDate date = null;
        try {
            date = LocalDate.parse(readString());
        }
        catch (DateTimeParseException e) {
            System.out.println("Invalid date format.");
            return null;
        }
        return date;
    }

    /* State management IO methods */

    private void saveStateIO() {
        try {
            System.out.println("Saving state to " + this.m.getStateFilepath());
            this.m.saveState();
            System.out.println("State saved successfully, " + this.m.getUsersSize() + " users saved.");
        }
        catch (StateNotSavedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadStateIO() {

        if (this.m.getUpdatedState()) {
            System.out.print("Warning: current state will be lost. Do you want to continue? [y/n]: ");
            String cont = readYesNo();
            if (cont.equals("n")) {
                return;
            }
        }

        try {
            System.out.println("Loading state from " + this.m.getStateFilepath());
            this.m.loadState();
            System.out.println("State loaded successfully, " + this.m.getUsersSize() + " users loaded.");
        }
        catch (StateNotLoadedException e) {
            System.out.println(e.getMessage());
        }
    }
}

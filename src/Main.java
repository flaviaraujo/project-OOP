/* package src;

import src.ActivityPlanner;
import src.activities.Distance;
import src.exceptions.ActivityIsRegisteredException;
import src.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {

    // User perspective menu
    private static void userMenu(ActivityPlanner m, Scanner sc, User user) {

        System.out.println();
        System.out.println("[User menu] Please select an option:");
        System.out.println("( 1) View your profile");
        System.out.println("( 2) Edit your profile");
        System.out.println("( 3) Create an activity");
        System.out.println("( 4) Delete an activity");
        System.out.println("( 5) View an activity");
        System.out.println("( 6) View all activities");
        System.out.println("( 7) Register activity");
        System.out.println("( 8) View registered activities");
        System.out.println("( 9) Create your plan");
        System.out.println("(10) Create plan based on your goals");
        System.out.println("(11) Delete your plan");
        System.out.println("(12) View your plan");
        System.out.println("(13) Statistics menu");
        System.out.println("(14) Save program state");
        System.out.println("(15) Load program state");
        System.out.println("(16) Exit");
        System.out.print("Option: ");

        int option = readInt(sc);

        Activity a = null;
        ArrayList<Activity> userActivities;

        switch (option) {
            case 1:
                // View your profile
                System.out.println(user);
                break;
            case 2:
                // Edit your profile
                try {
                    user.edit(sc, m.users, user);
                    m.updateUser(user);
                }
                catch (UserNotFoundException e) {
                    System.out.println(e.getMessage());
                    break;
                }
                m.setUpdatedState(true);
                System.out.println("User edited successfully.");
                break;
            case 3:
                // Create an activity
                userActivities = user.getActivities();

                a = Activity.createMenu(sc, userActivities);
                if (a == null) {
                    System.out.println("Activity not created.");
                    break;
                }

                // Add activity to user
                user.addActivity(a);
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
            case 4:
                // Delete an activity
                userActivities = user.getActivities();
                if (userActivities.size() == 0) {
                    System.out.println("You have no activities.");
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
            case 5:
                // View an activity
                userActivities = user.getActivities();

                if (userActivities.size() == 0) {
                    System.out.println("The selected user has no activities.");
                    break;
                }

                // View an activity
                a = Activity.search(sc, userActivities);
                System.out.println(a);

                break;
            case 6:
                // View all activities
                userActivities = user.getActivities();
                if (userActivities.size() == 0) {
                    System.out.println("The selected user has no activities.");
                    break;
                }
                for (Activity t : userActivities)
                    System.out.println(t);
                break;
            case 7:
                // Register activity
                a = (Activity) new Distance();
                user = a.register(sc, user);
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
            case 8:
                // View registered activities
                user.viewRegisters();
                break;
            case 9:
                // Create your plan
                // delete plan if it already exists
                Plan old = user.getPlan();
                if (old != null) {
                    System.out.print("Do you want to overwrite your current plan? [y/n]: ");
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
                userActivities = user.getActivities();
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
            case 10:
                // Create plan based on your goals
                Plan oldPlan = user.getPlan();
                if (oldPlan != null) {
                    System.out.print("Do you want to overwrite your current plan? [y/n]: ");
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
            case 11:
                // Delete your plan
                if (user.getPlan() == null) {
                    System.out.println("You have no plan.");
                    break;
                }

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
            case 12:
                // View your plan
                if (user.getPlan() == null) {
                    System.out.println("The selected user has no plan.");
                    break;
                }
                System.out.print(user.getPlan());
                break;
            case 13:
                // Statistics menu
                // Stats stats = new Stats();
                // stats.statsMenu(sc, m.users);
                break;
            case 14:
                // Save program state
                saveState(stateFilepath, m);
                break;
            case 15:
                // Load program state
                loadState(stateFilepath, m, sc);
                break;
            case 16:
                // Exit
                exit(stateFilepath, m, sc);
                break;
            default:
                System.out.println("Invalid option");
                break;
        }
    }
} */

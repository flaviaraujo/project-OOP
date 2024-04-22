package src;

import src.exceptions.UserNotFoundException;

import src.Activity;
import src.activities.Distance;
import src.activities.DistanceAltimetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Stats {

    // 1. The user with most calories burned
    // 1.1 All time
    public User mostCaloriesBurned(HashMap<Integer, User> users) {

        User user = null;
        if (!users.isEmpty()) {
            user = users.entrySet().iterator().next().getValue();
        }
        int max = 0;

        for (User u : users.values()) {

            int tmp = 0;

            // calculate the total calories burned by the user
            for (Activity r : u.getRegisters().values()) {
                tmp += r.getCalories();
            }

            // if the user has burned more calories than the current max
            // we update the max and the user
            if (tmp > max) {
                max = tmp;
                user = u;
            }
        }

        return user;
    }

    // 1.2 In a period of time (date -> date)
    public User mostCaloriesBurned(HashMap<Integer, User> users, LocalDate start, LocalDate end) {

        User user = null;
        if (!users.isEmpty()) {
            user = users.entrySet().iterator().next().getValue();
        }
        int max = 0;

        for (User u : users.values()) {

            int tmp = 0;

            // calculate the total calories burned by the user
            // checking if the key on the map entry is between the start and end date
            for (Entry<LocalDateTime, Activity> entry : u.getRegisters().entrySet()) {
                LocalDateTime date = entry.getKey();

                if (date.isAfter(start.atStartOfDay()) && date.isBefore(end.atStartOfDay())) {
                    tmp += entry.getValue().getCalories();
                }
            }

            // if the user has burned more calories than the current max
            // we update the max and the user
            if (tmp > max) {
                max = tmp;
                user = u;
            }
        }

        return user;
    }

    // 2. The user with the most activities (registered/completed)
    // in case of draw returns the first user, if empty users array
    // is passed return null
    // 2.1 All time
    public User mostActivities(HashMap<Integer, User> users) {

        User user = null;
        if (!users.isEmpty()) {
            user = users.entrySet().iterator().next().getValue();
        }
        int max = 0;

        // for each user get the number of registered activities
        for (User u : users.values()) {

            int tmp = u.getRegisters().size();
            if (tmp > max) {
                max = tmp;
                user = u;
            }
        }

        return user;
    }

    // 2.2 In a period of time (date -> date)
    public User mostActivities(HashMap<Integer, User> users, LocalDate start, LocalDate end) {

        User user = null;
        if (!users.isEmpty()) {
            user = users.get(0);
        }
        int max = 0;

        // for each user get the number of registered activities
        for (User u : users.values()) {

            int c = 0;
            for (LocalDateTime date : u.getRegisters().keySet()) {

                if (date.isAfter(start.atStartOfDay()) && date.isBefore(end.atStartOfDay())) {
                    c++;
                }
            }
            if (c > max) {
                max = c;
                user = u;
            }
        }

        return user;
    }

    // 3. The type of activity most practiced by the users
    public String mostPracticedActivityType(HashMap<Integer, User> users) {

        String result = "None";
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (User u : users.values()) {

            for (Activity r : u.getRegisters().values()) {
                int type = r.getType();
                if (map.containsKey(type)) {
                    map.put(type, map.get(type) + 1);
                }
                else {
                    map.put(type, 1);
                }
            }
        }

        int max = 0;
        Activity a = (Activity) new Distance();
        for (Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                result = a.getType((int) entry.getKey());
            }
        }

        return result;
    }

    // 4. How many km’s were traveled by one user
    // 4.1 All time
    public double kmTraveled(User user) {

        int c = 0;

        for (Activity r : user.getRegisters().values()) {

            if (r.isDistance()) {
                Distance da = (Distance) r;
                c += da.getDistance();
            }
            else if (r.isDistanceAltimetry()) {
                DistanceAltimetry da = (DistanceAltimetry) r;
                c += da.getDistance();
            }
        }

        return c / 1000.00;
    }

    // 4.2 In a period of time (date -> date)
    public double kmTraveled(User user, LocalDate start, LocalDate end) {
        int c = 0;

        for (Entry<LocalDateTime, Activity> entry : user.getRegisters().entrySet()) {

            LocalDateTime date = entry.getKey();

            if (date.isAfter(start.atStartOfDay()) && date.isBefore(end.atStartOfDay())) {

                Activity r = entry.getValue();

                if (r.isDistance()) {
                    Distance da = (Distance) r;
                    c += da.getDistance();
                }
                else if (r.isDistanceAltimetry()) {
                    DistanceAltimetry da = (DistanceAltimetry) r;
                    c += da.getDistance();
                }
            }
        }

        return c / 1000.00;
    }

    // 5. How many meters of altimetry were climbed by one user
    // 5.1 All time
    public int altimetryClimbed(User user) {

        int c = 0;

        for (Activity r : user.getRegisters().values()) {

            if (r.isDistanceAltimetry()) {
                DistanceAltimetry da = (DistanceAltimetry) r;
                c += da.getAltimetry();
            }
        }

        return c;
    }

    // 5.2 In a period of time (date -> date)
    public int altimetryClimbed(User user, LocalDate start, LocalDate end) {

        int c = 0;

        for (Entry<LocalDateTime, Activity> entry : user.getRegisters().entrySet()) {

            LocalDateTime date = entry.getKey();

            if (date.isAfter(start.atStartOfDay()) && date.isBefore(end.atStartOfDay())) {

                Activity r = entry.getValue();

                if (r.isDistanceAltimetry()) {
                    DistanceAltimetry da = (DistanceAltimetry) r;
                    c += da.getAltimetry();
                }
            }
        }

        return c;
    }

    // 6. Whats the practice plan with more calories burned
    public Plan mostCaloriesBurnedPlan(HashMap<Integer, User> users) {

        User user = null;
        Plan result = null;
        int max = 0;

        for (User u : users.values()) {

            Plan p = u.getPlan();
            if (p == null)
                continue;
            else if (result == null) {
                result = p;
                user = u;
                for (Event e : p.getEvents()) {
                    max += e.getActivity().calculateCalories(u);
                }
                continue;
            }

            int tmp = 0;
            for (Event e : p.getEvents()) {
                tmp += e.getActivity().calculateCalories(u);
            }
            if (tmp > max) {
                max = tmp;
                user = u;
                result = p;
            }
        }

        if (result == null) {
            System.out.println("No plans found.");
        }
        else {
            System.out.println("The plan with the most calories burned is \""
                + result.getName() + "\" with " + max + " calories burned, by "
                + "the user: " + user.getName());
        }

        return result;
    }

    public void displayStatsMenu() {
        System.out.println("[Stats Menu] Choose an option:");
        System.out.println("(1) The user with most calories burned");
        System.out.println("(2) The user with the most completed activities");
        System.out.println("(3) The type of activity most practiced by the users");
        System.out.println("(4) How many km’s were traveled by one user");
        System.out.println("(5) How many meters of altimetry were climbed by one user");
        System.out.println("(6) Whats the practice plan with more calories burned");
        System.out.println("(7) List the activities of a user");
        System.out.println("(8) Return to main menu");
    }

    public void displayStatsMenu2() {
        System.out.println("Choose an option:");
        System.out.println("(1) All time");
        System.out.println("(2) In a period of time");
        System.out.println("(3) Return to stats menu");
    }

    public LocalDate insertDate(Scanner sc, boolean start) {

        IO io = new IO();

        LocalDate date = null;
        while (true) {
            System.out.print("Insert the " + (start ? "start" : "end") + " date (yyyy-mm-dd): ");
            String buffer = io.readString(sc);
            try {
                date = LocalDate.parse(buffer);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format.");
                continue;
            }
            break;
        }
        return date;
    }

    public void statsMenu(Scanner sc, HashMap<Integer, User> users) {
        User user = null;
        int option = 0, option2 = 0;
        IO io = new IO();
        while (true) {
            displayStatsMenu();
            System.out.print("Option: ");

            option = io.readInt(sc);

            switch (option) {
                case 1:
                    // 1. The user with most calories burned
                    displayStatsMenu2();
                    System.out.print("Option: ");
                    option2 = io.readInt(sc);

                    switch (option2) {
                        case 1:
                            user = mostCaloriesBurned(users);
                            if (user == null) {
                                System.out.println("No users found.");
                                break;
                            }
                            System.out.println("The user with most calories burned is: " + user.getName());
                            break;
                        case 2:
                            LocalDate start = insertDate(sc, true);
                            LocalDate end = insertDate(sc, false);
                            user = mostCaloriesBurned(users, start, end);
                            if (user != null) {
                                System.out.println("The user with most calories burned between "
                                    + start + " and " + end + " is: " + user.getName());
                            } else {
                                System.out.println("No users found.");
                            }
                            break;
                        case 3:
                            break;
                        default:
                            System.out.println("Invalid option, try again.");
                    }
                    break;
                case 2:
                    // 2. The user with the most activities (registered/completed)
                    displayStatsMenu2();
                    System.out.print("Option: ");
                    option2 = io.readInt(sc);

                    switch (option2) {
                        case 1:
                            user = mostActivities(users);
                            if (user != null) {
                                System.out.println("The user with most activities is: "
                                    + user.getName() + " with " + user.getRegisters().size() + " activities.");
                            } else {
                                System.out.println("No users found.");
                            }
                            break;
                        case 2:
                            LocalDate start = insertDate(sc, true);
                            LocalDate end = insertDate(sc, false);
                            user = mostActivities(users, start, end);
                            if (user != null) {
                                System.out.println("The user with most activities between "
                                    + start + " and " + end + " is: " + user.getName() + " with "
                                    + user.getRegisters().size() + " activities.");
                            } else {
                                System.out.println("No users found.");
                            }
                            break;
                        case 3:
                            break;
                        default:
                            System.out.println("Invalid option, try again.");
                    }

                    break;
                case 3:
                    // 3. The type of activity most practiced by the users
                    String activity = mostPracticedActivityType(users);
                    System.out.println("The type of activity most practiced by the users is: " + activity);
                    break;
                case 4:
                    // 4. How many km’s were traveled by one user
                    // Select an user to get the km traveled
                    try {
                        user = User.search(sc, users);
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    displayStatsMenu2();
                    System.out.print("Option: ");
                    option2 = io.readInt(sc);

                    switch (option2) {
                        case 1:
                            double km = kmTraveled(user);
                            System.out.println("The user " + user.getName() + " has traveled " + km + " km.");
                            break;
                        case 2:
                            LocalDate start = insertDate(sc, true);
                            LocalDate end = insertDate(sc, false);
                            km = kmTraveled(user, start, end);
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
                        user = User.search(sc, users);
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    displayStatsMenu2();
                    System.out.print("Option: ");
                    option2 = io.readInt(sc);

                    switch (option2) {
                        case 1:
                            int altimetry = altimetryClimbed(user);
                            System.out.println("The user " + user.getName() + " has climbed "
                                    + altimetry + " meters.");
                            break;
                        case 2:
                            LocalDate start = insertDate(sc, true);
                            LocalDate end = insertDate(sc, false);
                            altimetry = altimetryClimbed(user, start, end);
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
                    mostCaloriesBurnedPlan(users);
                    break;
                case 7:
                    // 7. List the activities of a user
                    // Select an user to list the activities
                    try {
                        user = User.search(sc, users);
                    }
                    catch (UserNotFoundException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    ArrayList<Activity> activities = user.getActivities();
                    if (activities.size() == 0) {
                        System.out.println("The selected user has no activities.");
                        break;
                    }
                    for (Activity t : activities) System.out.println(t);
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }
}

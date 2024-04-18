package src;

import src.Activity;
import src.activities.Distance;
import src.activities.DistanceAltimetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Stats {

    // 1. The user with most calories burned
    // 1.1 All time
    public User mostCaloriesBurned(ArrayList<User> users) {

        User user = users.size() > 0 ? users.get(0) : null;
        int max = 0, tmp = 0;

        for (User u : users) {
            tmp = 0;

            // calculate the total calories burned by the user
            for (Register r : u.getRegisters().values()) {
                tmp += r.getCaloriesBurned();
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
    public User mostCaloriesBurned(ArrayList<User> users, LocalDate start, LocalDate end) {
        User user = users.size() > 0 ? users.get(0) : null;
        int max = 0, tmp = 0;

        for (User u : users) {
            tmp = 0;

            // calculate the total calories burned by the user
            // checking if the key on the map entry is between the start and end date
            for (Entry<LocalDateTime, Register> entry : u.getRegisters().entrySet()) {
                LocalDateTime date = entry.getKey();

                if (date.isAfter(start.atStartOfDay()) && date.isBefore(end.atStartOfDay())) {
                    tmp += entry.getValue().getCaloriesBurned();
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
    public User mostActivities(ArrayList<User> users) {
        User user = users.size() > 0 ? users.get(0) : null;
        int max = 0, tmp = 0;

        // for each user get the number of registered activities
        for (User u : users) {
            tmp = u.getRegisters().size();
            if (tmp > max) {
                max = tmp;
                user = u;
            }
        }

        return user;
    }

    // 2.2 In a period of time (date -> date)
    public User mostActivities(ArrayList<User> users, LocalDate start, LocalDate end) {
        User user = users.size() > 0 ? users.get(0) : null;
        int max = 0, c = 0;

        // for each user get the number of registered activities
        for (User u : users) {
            c = 0;
            for (Entry<LocalDateTime, Register> entry : u.getRegisters().entrySet()) {
                LocalDateTime date = entry.getKey();

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
    public String mostPracticedActivityType(ArrayList<User> users) {

        String result = "None";
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (User u : users) {
            for (Register r : u.getRegisters().values()) {
                Activity a = r.getActivity();
                int type = a.getType();
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
                result = a.getName((int) entry.getKey());
            }
        }

        return result;
    }

    // 4. How many km’s were traveled by one user
    // 4.1 All time
    public double kmTraveled(User user) {

        int c = 0;

        for (Register r : user.getRegisters().values()) {
            Activity a = r.getActivity();
            if (a.isDistance()) {
                Distance da = (Distance) a;
                c += da.getDistance();
            }
            else if (a.isDistanceAltimetry()) {
                DistanceAltimetry da = (DistanceAltimetry) a;
                c += da.getDistance();
            }
        }

        return c / 1000.00;
    }

    // 4.2 In a period of time (date -> date)
    public double kmTraveled(User user, LocalDate start, LocalDate end) {
        int c = 0;

        for (Entry<LocalDateTime, Register> entry : user.getRegisters().entrySet()) {
            LocalDateTime date = entry.getKey();
            if (date.isAfter(start.atStartOfDay()) && date.isBefore(end.atStartOfDay())) {
                Activity a = entry.getValue().getActivity();
                if (a.isDistance()) {
                    Distance da = (Distance) a;
                    c += da.getDistance();
                }
                else if (a.isDistanceAltimetry()) {
                    DistanceAltimetry da = (DistanceAltimetry) a;
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

        for (Register r : user.getRegisters().values()) {
            Activity a = r.getActivity();
            if (a.isDistanceAltimetry()) {
                DistanceAltimetry da = (DistanceAltimetry) a;
                c += da.getAltimetry();
            }
        }

        return c;
    }

    // 5.2 In a period of time (date -> date)
    public int altimetryClimbed(User user, LocalDate start, LocalDate end) {

        int c = 0;

        for (Entry<LocalDateTime, Register> entry : user.getRegisters().entrySet()) {
            LocalDateTime date = entry.getKey();
            if (date.isAfter(start.atStartOfDay()) && date.isBefore(end.atStartOfDay())) {
                Activity a = entry.getValue().getActivity();
                if (a.isDistanceAltimetry()) {
                    DistanceAltimetry da = (DistanceAltimetry) a;
                    c += da.getAltimetry();
                }
            }
        }

        return c;
    }

    // 6. Whats the practice plan with more calories burned
    public Plan mostCaloriesBurnedPlan(ArrayList<User> users) {

        User user = null;
        Plan result = null;
        int max = 0, tmp = 0;

        for (User u : users) {

            Plan p = u.getPlan();
            if (p == null)
                continue;
            else if (result == null) {
                result = p;
                user = u;
                for (Event e : p.getEvents()) {
                    max += e.getActivity().caloriesBurned(u);
                }
                continue;
            }

            tmp = 0;
            for (Event e : p.getEvents()) {
                tmp += e.getActivity().caloriesBurned(u);
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

    public LocalDate insertStartDate(Scanner sc) {
        LocalDate start = null;
        while (true) {
            System.out.print("Insert the start date (yyyy-mm-dd): ");
            String buffer = sc.next();
            try {
                start = LocalDate.parse(buffer);
                break;
            } catch (Exception e) {
                System.out.println("Invalid date format, try again.");
            }
        }
        return start;
    }

    public LocalDate insertEndDate(Scanner sc) {
        LocalDate end = null;
        while (true) {
            System.out.print("Insert the end date (yyyy-mm-dd): ");
            String buffer = sc.next();
            try {
                end = LocalDate.parse(buffer);
                break;
            } catch (Exception e) {
                System.out.println("Invalid date format, try again.");
            }
        }
        return end;
    }

    public void statsMenu(Scanner sc, ArrayList<User> users) {
        User user = null;
        int option = 0, option2 = 0;
        while (true) {
            displayStatsMenu();
            System.out.print("Option: ");
            try {
                option = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid option, try again.");
                sc.next();
                continue;
            }
            switch (option) {
                case 1:
                    // 1. The user with most calories burned
                    displayStatsMenu2();
                    System.out.print("Option: ");
                    try {
                        option2 = sc.nextInt();
                    } catch (Exception e) {
                        System.out.println("Invalid option, try again.");
                        sc.next();
                        continue;
                    }

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
                            LocalDate start = insertStartDate(sc);
                            LocalDate end = insertEndDate(sc);
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
                    try {
                        option2 = sc.nextInt();
                    } catch (Exception e) {
                        System.out.println("Invalid option, try again.");
                        sc.next();
                        continue;
                    }

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
                            LocalDate start = insertStartDate(sc);
                            LocalDate end = insertEndDate(sc);
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
                    user = User.search(sc, users);
                    if (user == null) {
                        System.out.println("No user selected");
                        break;
                    }

                    displayStatsMenu2();
                    System.out.print("Option: ");
                    try {
                        option2 = sc.nextInt();
                    } catch (Exception e) {
                        System.out.println("Invalid option, try again.");
                        sc.next();
                        continue;
                    }

                    switch (option2) {
                        case 1:
                            double km = kmTraveled(user);
                            System.out.println("The user " + user.getName() + " has traveled " + km + " km.");
                            break;
                        case 2:
                            LocalDate start = insertStartDate(sc);
                            LocalDate end = insertEndDate(sc);
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
                    user = User.search(sc, users);
                    if (user == null) {
                        System.out.println("No user selected");
                        break;
                    }

                    displayStatsMenu2();
                    System.out.print("Option: ");
                    try {
                        option2 = sc.nextInt();
                    } catch (Exception e) {
                        System.out.println("Invalid option, try again.");
                        sc.next();
                        continue;
                    }

                    switch (option2) {
                        case 1:
                            int altimetry = altimetryClimbed(user);
                            System.out.println("The user " + user.getName() + " has climbed "
                                    + altimetry + " meters.");
                            break;
                        case 2:
                            LocalDate start = insertStartDate(sc);
                            LocalDate end = insertEndDate(sc);
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
                    user = User.search(sc, users);
                    if (user == null) {
                        System.out.println("No user selected");
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

package src;

import src.activities.Distance;
import src.activities.DistanceAltimetry;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Stats {

    // 1. The user with most calories burned
    // 1.1 All time
    public User mostCaloriesBurned(ArrayList<User> users) {

        User user = null;
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
        User user = null;
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
        User user = null;
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
        User user = null;
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

    // TODO 3. The type of activity most practiced by the users
    public String mostPracticedActivityType(ArrayList<User> users) {
        return ""; // activity type as string
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

    // TODO 6. Whats the practice plan with more calories burned
    public Plan mostCaloriesBurnedPlan(ArrayList<User> users) {

        return null;
    }

    // TODO 7. List the activities of a user
    // (already implemented in manage user activities menu)
}

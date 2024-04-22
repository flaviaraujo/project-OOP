package src;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.DayOfWeek;

public class Simulation {

    public void run(Scanner sc, HashMap<Integer, User> users) {

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = getSimulationEndDate(sc, startDate);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");
        System.out.println("Simulation start date: " + startDate.format(dateFormatter));
        System.out.println("Simulation end date: " + endDate.format(dateFormatter));
        System.out.println("Starting simulation...");

        int days = (int) ChronoUnit.DAYS.between(startDate, endDate);
        System.out.println("Number of days: " + days);
        int currentWeekDay = (startDate.getDayOfWeek().getValue() % 7) + 1; // 1 = Sunday ... 7 = Saturday

        int usersCount = 0; // number of users with training plans

        for (Entry<Integer, User> entry : users.entrySet()) {
            User u = entry.getValue();
            if (u.getPlan() != null) {
                // System.out.println("User \"" + u.getName() + "\" has a training plan.");
                usersCount++;
            } else {
                // check for users with no training plans
                System.out.println("Skipping user \"" + u.getName() + "\" as there is no training plan.");
            }
        }

        int activitiesCount = 0;
        int registeredActivities = 0;
        int totalCalories = 0;

        Event event = new Event();
        for (int day = 0; day <= days; day++) {

            if (currentWeekDay > 7) {
                currentWeekDay = 1;
            }

            System.out.println(
                "Day " + (day + 1) + " " +
                event.convertDayToString(currentWeekDay) + " " +
                startDate.plusDays(day).format(dateFormatter)
            );

            for (Entry<Integer, User> entry : users.entrySet()) {
                User u = entry.getValue();
                Plan p = u.getPlan();
                if (p == null) {
                    continue;
                }

                for (Event e : p.getEvents()) {
                    if (e.getDay() == currentWeekDay) {

                        activitiesCount++;

                        LocalDate eventDate = startDate.plusDays(day);

                        int repetitions = e.getActivityRepetitions();

                        Activity a = e.getActivity();

                        int calories = a.calculateCalories(u); // calories burned in each repetition

                        for (int repetition = 0; repetition < repetitions; repetition++)
                        {
                            registeredActivities++;
                            totalCalories += calories;

                            LocalTime eventTime = e.getTime();
                            LocalDateTime eventDateTime = LocalDateTime.of(eventDate, eventTime);
                            // Shift the day if the event time is after midnight
                            eventDateTime = eventDateTime.plusMinutes(a.getDuration() * repetition);

                            // register the activity
                            Activity r = a.clone();
                            r.setCalories(calories);
                            u.registerActivity(eventDateTime, r);

                            System.out.println(
                                "User \"" + u.getName() + '"' +
                                " completed activity \"" + a.getName() + '"' +
                                " at " + eventDateTime.format(timeFormatter) +
                                " and burned " + calories + " calories."
                            );
                        }
                    }
                }
            }

            currentWeekDay++;
        }

        System.out.println("Simulation completed. " + days + " days simulated.");
        System.out.println("Users with training plans: " + usersCount);
        System.out.println("Activities: " + activitiesCount);
        System.out.println("Registered activities: " + registeredActivities);
        System.out.println("Total calories burned: " + totalCalories);
    }

    public LocalDate getSimulationEndDate(Scanner sc, LocalDate startDate) {

        IO io = new IO();

        LocalDate endDate = null;
        while (true) {

            System.out.print("Enter the end date of the simulation (yyyy-mm-dd): ");

            String endDateBuffer = io.readString(sc);
            try {
                endDate = LocalDate.parse(endDateBuffer);
            }
            catch (DateTimeParseException e) {
                System.out.println("Invalid date format.");
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
}

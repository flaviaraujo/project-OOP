package src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Plan
 */
public class Plan implements Serializable {

    private String name;
    private ArrayList<Event> events;

    public Plan() {
        this.name = "";
        this.events = new ArrayList<Event>();
    }

    public Plan(String name, ArrayList<Event> events) {
        this.name = name;
        this.events = new ArrayList<Event>(events);
    }

    public Plan(Plan plan) {
        this.name = plan.getName();
        this.events = plan.getEvents();
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Event> getEvents() {
        ArrayList<Event> events = new ArrayList<Event>();
        for (Event event : this.events) {
            events.add(event.clone());
        }
        return events;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = new ArrayList<Event>(events.size());
        for (Event event : events) {
            this.events.add(event.clone());
        }
    }

    public String toString() {
        Comparator<Event> eventComparator = Comparator
            .comparingInt(Event::getDay) // sort by the week day
            .thenComparing(Event::getTime); // then by time

        Collections.sort(events, eventComparator);

        StringBuilder sb = new StringBuilder();
        sb.append("Plan: " + this.name + "\n");
        sb.append("Events: \n");
        for (Event event : this.events) {
            sb.append(event + "\n");
        }
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Plan p = (Plan) o;
        return (
            this.name.equals(p.getName()) &&
            this.events.equals(p.getEvents())
        );
    }

    public Plan clone() {
        return new Plan(this);
    }

    public void addEvent(Event event) {
        this.events.add(event.clone());
    }

    public void removeEvent(Event event) {
        this.events.remove(event);
    }

    //TODO create plan based on user goals

    // create plan interactively
    public Plan create(Scanner sc, ArrayList<Activity> userActivities) {

        if (userActivities.isEmpty()) {
            System.out.println("There are no activities available.");
            System.out.println("Please add activities before creating a plan.");
            return null;
        }

        sc.nextLine(); // clear the buffer

        Event e = new Event();
        int maxActivities = e.getMAX_REPETITIONS();

        // Plan
        Plan plan = new Plan();

        // Get name of the plan
        System.out.print("Enter the name of the plan: ");
        String name = sc.nextLine();
        plan.setName(name);

        // start on sunday and ask how many activities wants to add on that day, and so on
        int counterDaysOff = 0;
        for (int i = 1; i <= 7; i++) {
            int activities;
            while (true) {
                System.out.print("How many activities do you want on " +
                    e.convertDayToString(i) + "? (0-" + maxActivities + "): ");

                try {
                    activities = sc.nextInt();
                }
                catch (Exception ex) {
                    System.out.println("Invalid input. Please enter a number.");
                    sc.nextLine();
                    continue;
                }
                if (activities < 0 || activities > maxActivities) {
                    System.out.println("Invalid number of activities. " +
                        "Please enter a number between 0 and " + maxActivities + ".");
                    continue;
                }
                break;
            }

            if (activities == 0) {
                counterDaysOff++;
                continue;
            }

            int eventCount = 0;
            for (int j = 0; j < activities;) {

                System.out.println("Event number " + (++eventCount));

                e = e.create(sc, userActivities, activities - j, i);
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
}

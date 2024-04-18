package src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Plan
 */
public class Plan implements Serializable {

    private String name;
    private ArrayList<Event> events;

    public Plan() {
        this.name = "";
        this.events = null;
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
        this.events = new ArrayList<Event>(events); //TODO clone
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Plan: ").append(this.name).append("\n");
        sb.append("Events: \n");
        for (Event event : this.events) {
            sb.append(event.toString()).append("\n");
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
        this.events.remove(event); //TODO dúvida é preciso fazer clone?
    }

    //TODO create plan based on user goals

    // create plan interactively
    public void create(Scanner sc, ArrayList<Activity> userActivities) {

        if (userActivities.isEmpty()) {
            System.out.println("There are no activities available.");
            System.out.println("Please add activities before creating a plan.");
            return;
        }

        // Get name of the plan
        System.out.print("Enter the name of the plan: ");
        this.name = sc.nextLine();

        Event e = new Event();
        int maxActivities = e.getMAX_REPETITIONS();

        // start on sunday and ask how many activities wants to add on that day, and so on
        for (int i = 1; i <= 7; i++) {
            int activities;
            while (true) {
                System.out.println("How many activities do you want on " +
                    e.convertDayToString(i) + "?");

                activities = sc.nextInt(); //TODO try catch
                if (activities < 1 || activities > maxActivities) {
                    System.out.println("Invalid number of activities. " +
                        "Please enter a number between 1 and " + maxActivities + ".");
                    continue;
                }
                break;
            }

            for (int j = 0; j < activities;) {
                System.out.println("Event number " + j);
            
                if (maxActivities - j <= 0) {
                    System.out.println("You have reached the maximum number of activities.");
                    break;
                }

                Event event = e.create(sc, userActivities, maxActivities - j);
                this.addEvent(event);
                j += event.getActivityRepetitions();
            }
        }
    }       
}
package src;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Plan
 */
public class Plan {

    private String name; // unique
    private int expectedCaloriesBurned;
    private ArrayList<Event> events;

    public Plan() {

        this.name = "";
        this.expectedCaloriesBurned = 0;
        this.events = null;
    }

    public Plan(String name, int expectedCaloriesBurned, ArrayList<Event> events) {
        this.name = name;
        this.expectedCaloriesBurned = expectedCaloriesBurned;
        this.events = new ArrayList<Event>(events);
    }

    public Plan(Plan plan) {
        this.name = plan.getName();
        this.expectedCaloriesBurned = plan.getExpectedCaloriesBurned();
        this.events = plan.getEvents();
    }

    public String getName() {
        return this.name;
    }

    public int getExpectedCaloriesBurned() {
        return this.expectedCaloriesBurned;
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

    public void setExpectedCaloriesBurned(int expectedCaloriesBurned) {
        this.expectedCaloriesBurned = expectedCaloriesBurned;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = new ArrayList<Event>(events);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Plan: ").append(this.name).append("\n");
        sb.append("Expected Calories Burned: ").append(this.expectedCaloriesBurned).append("\n");
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
            this.expectedCaloriesBurned == p.getExpectedCaloriesBurned() &&
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

   

    

    //create plan based on user goals



    //create plan based on user goals
    public void createPlan(Scanner sc, ArrayList<Activity> userActivities) {
        
       /* System.out.println("Please select an option:");
        System.out.println("(1) Create a plan based on your goals");
        System.out.println("(2) Create a plan interactively");
        System.out.println("(3) Back to main menu");
        */


    }

    


    //public class Event {
    //     private Activity activity;
    //     private int activityRepetitions; // number of times the activity should be repeated
    //     // Time or DateTime for start and end? Depends if plan is for a specific date or a range of dates
    //     private DateTime start;
    //     private DateTime end; // (start + activity.getDuration() * activityRepetitions)
    // }

    // TODO
    // Constructors
    // Getters and Setters
    // Methods - toString, clone, equals
    // Methods - create (interactive) -> N (max 3) events for a plan and then Event.create() N times
    // Methods - create (based on user goals) -> based on type of activities, recorrencia, etc
    // Methods - remove
}

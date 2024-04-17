package src;

import java.util.Date;

/**
 * Plan
 */
public class Plan {
    // private String name; // TODO is worth it? (to delete plans this must be unique)
    private User user;
    private Date date; // Is a plan for a specific date or a range of dates?
    // private ArrayList<Events> events;
    // public class Event {
    //     private Activity activity;
    //     private int activityRepetitions; // number of times the activity should be repeated
    //     // Time or DateTime for start and end? Depends if plan is for a specific date or a range of dates
    //     private DateTime start;
    //     private DateTime end; // (start + activity.getDuration() * activityRepetitions)
    //     private boolean completed; // TODO is worth it?
    // }

    // TODO
    // Constructors
    // Getters and Setters
    // Methods - toString, clone, equals
    // Methods - create (interactive) -> N (max 3) events for a plan and then Event.create() N times
    // Methods - create (based on user goals) -> based on type of activities, recorrencia, etc
    // Methods - remove
}

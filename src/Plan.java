package src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
}

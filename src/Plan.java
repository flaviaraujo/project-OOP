package src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import java.time.LocalTime;

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

    // create plan interactively
    public Plan create(Scanner sc, ArrayList<Activity> userActivities) {

        if (userActivities.isEmpty()) {
            System.out.println("There are no activities available.");
            System.out.println("Please add activities before creating a plan.");
            return null;
        }

        Event e = new Event();
        int maxActivities = e.getMAX_REPETITIONS();

        // Plan
        Plan plan = new Plan();

        IO io = new IO();

        // Get name of the plan
        System.out.print("Enter the name of the plan: ");
        String name = io.readString(sc);
        plan.setName(name);

        // start on sunday and ask how many activities wants to add on that day, and so on
        int counterDaysOff = 0;
        for (int i = 1; i <= 7; i++) {
            int activities;
            while (true) {
                System.out.print("How many activities do you want on " +
                    e.convertDayToString(i) + "? (0-" + maxActivities + "): ");

                activities = io.readInt(sc);

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

    public class ActivityRepetition {
        private Activity activity;
        private int repetitionsLeft;

        public ActivityRepetition() {
            this.activity = null;
            this.repetitionsLeft = 0;
        }

        public ActivityRepetition(Activity activity, int repetitionsLeft) {
            this.activity = activity;
            this.repetitionsLeft = repetitionsLeft;
        }

        public Activity getActivity() {
            return this.activity;
        }

        public int getRepetitionsLeft() {
            return this.repetitionsLeft;
        }

        public void setRepetitionsLeft(int repetitionsLeft) {
            this.repetitionsLeft = repetitionsLeft;
        }

        public ArrayList<ActivityRepetition> clean(ArrayList<ActivityRepetition> ars) {
            // remove activities with 0 repetitions left
            for (ActivityRepetition ar : ars) {
                if (ar.getRepetitionsLeft() == 0) {
                    ars.remove(ar);
                }
            }
            return ars;
        }

        public ArrayList<ActivityRepetition> sort(ArrayList<ActivityRepetition> ar, int maxActivitiesPerDay, User user) {

            ar = clean(ar);

            Comparator<ActivityRepetition> activityRepetitionComparator = (ar1, ar2) -> {
                int calories1 = ar1.getActivity().calculateCalories(user) * Math.min(ar1.getRepetitionsLeft(), maxActivitiesPerDay);
                int calories2 = ar2.getActivity().calculateCalories(user) * Math.min(ar2.getRepetitionsLeft(), maxActivitiesPerDay);
                return Integer.compare(calories2, calories1);
            };

            Collections.sort(ar, activityRepetitionComparator);
            return ar;
        }

        public ArrayList<ActivityRepetition> createNonHard(ArrayList<Activity> a, int maxRepetitionPerWeek, int maxActivitiesPerDay, User user) {

            ArrayList<ActivityRepetition> ar = new ArrayList<ActivityRepetition>();
            for (Activity activity : a) {
                if (!activity.getHard()) {
                    ar.add(new ActivityRepetition(activity, maxRepetitionPerWeek));
                }
            }
            ar = sort(ar, maxActivitiesPerDay, user);
            return ar;
        }

        public ArrayList<ActivityRepetition> createHard(ArrayList<Activity> a, int maxRepetitionPerWeek, int maxActivitiesPerDay, User user) {
            ArrayList<ActivityRepetition> ar = new ArrayList<ActivityRepetition>();
            for (Activity activity : a) {
                if (activity.getHard()) {
                    ar.add(new ActivityRepetition(activity, maxRepetitionPerWeek));
                }
            }
            ar = sort(ar, 1, user);
            return ar;
        }
    }

    // create plan based on user goals
    public Plan createBasedOnGoals(User user, Scanner sc) {
        // restrictions
        // hard activituies can't be in the same day and consecutive days
        // max activities per day (max 3)
        // min distinct activities per day (max 3)
        // nRepActivityPerWeek (min 1, no max)

        int caloriesGoal = 12000;
        int maxActivitiesPerDay = 3;
        int maxDisitinctActivitiesPerDay = 3;
        int nActivityRepetitionPerWeek = 4;
        ArrayList<Activity> selectedActivities = user.getActivities();

        IO io = new IO();

        while (true) {
            System.out.print("Enter caloric goal per week: ");
            caloriesGoal = io.readInt(sc);
            if (caloriesGoal < 0) {
                System.out.println("Invalid caloric goal. Please enter a positive number.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter max activities per day: ");
            maxActivitiesPerDay = io.readInt(sc);
            if (maxActivitiesPerDay < 1 || maxActivitiesPerDay > 3) {
                System.out.println("Invalid number of activities. Please enter a number between 1 and 3.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter max distinct activities per day: ");
            maxDisitinctActivitiesPerDay = io.readInt(sc);
            if (maxDisitinctActivitiesPerDay < 1 || maxDisitinctActivitiesPerDay > 3) {
                System.out.println("Invalid number of activities. Please enter a number between 1 and 3.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter number of repetitions of activities per week: ");
            nActivityRepetitionPerWeek = io.readInt(sc);
            if (nActivityRepetitionPerWeek < 1) {
                System.out.println("Invalid number of repetitions. Please enter a number greater than 0.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter number of your activities to select: ");
            int nActivities = io.readInt(sc);
            ArrayList<Activity> activities = user.getActivities();
            if (nActivities < 1 || nActivities > user.getActivities().size()) {
                System.out.println("Invalid number of hard activities. Please enter a number between 1 and " +
                    user.getActivities().size() + ".");
                continue;
            }

            for (int i = 0; i < nActivities; i++) {
                // Remove selected activities from the list
                ArrayList<Activity> toSearch = new ArrayList<Activity>();
                for (Activity a : activities) {
                    if (!selectedActivities.contains(a)) {
                        toSearch.add(a);
                    }
                }

                System.out.println("Select activity number " + (i + 1) + ".");
                Activity activity = Activity.search(sc, toSearch);
                if (activity == null) {
                    System.out.println("Activity not found.");
                    i--;
                    continue;
                }

                selectedActivities.add(activity);
            }
            break;
        }

        int daysWithHardActivities = 0;
        // boolean canBeHard = daysWithHardActivities == 0;
        // reset: daysWithoutHardActivities = 2;
        int distinctActivitiesPerDay = 0;

        // seperate activities by hard and not hard (sorted by calories desc)
        ActivityRepetition arAux = new ActivityRepetition();
        ArrayList<ActivityRepetition> activitiesHard =
            arAux.createHard(selectedActivities, nActivityRepetitionPerWeek, maxActivitiesPerDay, user);
        ArrayList<ActivityRepetition> activities =
            arAux.createNonHard(selectedActivities, nActivityRepetitionPerWeek, maxActivitiesPerDay, user);

        ArrayList<Event> events = new ArrayList<Event>();

        ActivityRepetition ar = null;
        ActivityRepetition arHard = null;
        Activity a = null;
        Activity aHard = null;
        int repetitionsLeft = 0;
        int repetitionsLeftHard = 0;

        for (int day = 1; day <= 7; day++) {

            distinctActivitiesPerDay = 0;

            if (daysWithHardActivities > 0) {
                daysWithHardActivities--;
            }

            if (activitiesHard.isEmpty() && activities.isEmpty()) {
                break;
            }

            for (int i = 0; i < maxActivitiesPerDay;) {

                if (!activities.isEmpty()) {
                    ar = activitiesHard.get(0);
                    a = ar.getActivity();
                    repetitionsLeft = ar.getRepetitionsLeft();
                }
                else {
                    ar = null;
                }

                if (!activitiesHard.isEmpty()) {
                    arHard = activitiesHard.get(0);
                    aHard = arHard.getActivity();
                    repetitionsLeftHard = arHard.getRepetitionsLeft();
                }
                else {
                    arHard = null;
                }

                int nActivityOfThisType = Math.min(repetitionsLeft, maxActivitiesPerDay - i);

                if (ar == null
                    || (aHard.calculateCalories(user) >= a.calculateCalories(user) * nActivityOfThisType
                    && daysWithHardActivities == 0))
                {
                    // add hard activity (only one can be added)
                    Event event = new Event(aHard, 1, day, LocalTime.of(0, 0));
                    events.add(event);

                    // update repetitions
                    repetitionsLeftHard--;
                    arHard.setRepetitionsLeft(repetitionsLeftHard);

                    // re-sort
                    activitiesHard = arAux.sort(activitiesHard, 1, user);

                    // hard activities logic
                    daysWithHardActivities = 2;

                    // increment activity iterator
                    i++;
                }
                else if (ar != null) {
                    // add normal activity
                    Event event = new Event(a, nActivityOfThisType, day, LocalTime.of(0, 0));
                    events.add(event);

                    // update repetitions
                    repetitionsLeft -= nActivityOfThisType;
                    ar.setRepetitionsLeft(repetitionsLeft);

                    // increment activity iterator
                    i += nActivityOfThisType;

                    // re-sort
                    activities = arAux.sort(activities, maxActivitiesPerDay - i, user);
                }

                // increment distinct activities counter
                distinctActivitiesPerDay++;
                if (distinctActivitiesPerDay >= maxDisitinctActivitiesPerDay) {
                    break;
                }
            }
        }

        // Check if plan meets the calories goal
        int calories = 0;
        for (Event e : events) {
            calories += e.getActivity().calculateCalories(user) * e.getActivityRepetitions();
        }
        if (calories < caloriesGoal) {
            System.out.println("Based on your goals, it's impossible to create a plan.");
            System.out.println("Calories: " + calories + " / " + caloriesGoal);
            return null;
        }

        System.out.println("Plan created successfully.");
        return new Plan("Plan based on goals", events);
    }

    /* // 1. get user goals
        // 1.1 get user activities from user ArrayList of activities (check for hard activities)
        ArrayList<Activity> activities = user.getActivities();
        // 1.2 the user cant have hard activities in the same day and previous day (and next day)
        // 1.3 max number of activities per day (max 3)
        int maxActivitiesPerDay = 2;
        // 1.4 min number of distinct activities per day (max 3)
        int maxDisitinctActivitiesPerDay = 1;
        // 1.5 for each activity, get the number of repetitions per week
        int nActivityRepetitionPerWeek = 3;
        // 1.6 get calories goal for the week
        int caloriesGoal = 10000;

        // sort activities by calories (desc) a1 - a2
        Comparator<Activity> activityComparator = (activity1, activity2) -> {
            int calories1 = activity1.calculateCalories(user);
            int calories2 = activity2.calculateCalories(user);
            return Integer.compare(calories2, calories1);
        };

        Collections.sort(activities, activityComparator);

        int maxActivities = 7 * maxActivitiesPerDay;
        int hardActivities = 3; // 3 hard activities per week (example: sunday, tuesday, thursday)

        int calories = 0;
        int nActivities = 0;
        for (Activity a : activities) {

            // stop cases
            if (nActivities >= maxActivities) {
                // not possible to add more activities
                // this plan is impossible
                return null;
            }
            if (calories >= caloriesGoal) {
                // goal reached
                break;
            }

            if (a.getHard()) {
                if (hardActivities <= 0) {
                    // not possible to add more hard activities
                    continue;
                }
                int maxOfThisActivity = Math.min(hardActivities, nActivityRepetitionPerWeek);
                hardActivities -= maxOfThisActivity;
                calories += a.calculateCalories(user) * maxOfThisActivity;
                nActivities += maxOfThisActivity;
            }
            else {
                int maxOfThisActivity = Math.min(nActivityRepetitionPerWeek, maxActivities - nActivities);
                calories += a.calculateCalories(user) * maxOfThisActivity;
                nActivities += maxOfThisActivity;
            }
        }

        // TODO rearrange activities in the plan respecting the constraints and maximizing the calories
        // 1.2 the user cant have hard activities in the same day and previous day (and next day)
        // but the user can have hard activities with other ones if maxDisitinctActivitiesPerDay is not reached
        // 1.3 max number of activities per day (max 3)
        // 1.4 max number of distinct activities per day (max 3)
        ArrayList<Event> events = new ArrayList<Event>();

        // Tuple with activities and repetitions
        /* ArrayList<Pair<Activity, Integer>> activitiesRepetitions = new ArrayList<Pair<Activity, Integer>>();
        for (Activity a : activities) {
            activitiesRepetitions.add(new Pair<Activity, Integer>(a, nActivityRepetitionPerWeek));
        }

        // sort activities by calories (desc) a1 - a2
        Comparator<Pair<Activity, Integer>> activityRepetitionComparator = (pair1, pair2) -> {
            int calories1 = pair1.getFirst().calculateCalories(user) * pair1.getSecond();
            int calories2 = pair2.getFirst().calculateCalories(user) * pair2.getSecond();
            return Integer.compare(calories2, calories1);
        };

        calories = 0;
        for (int day = 1; day <= 7; day++) {
            // try to fit the most caloric activities
            // if not possible, try the next most caloric activities
            Collections.sort(activitiesRepetitions, activityRepetitionComparator);
            Activity a = activitiesRepetitions.get(0).getFirst();
            int repetitions = activitiesRepetitions.get(0).getSecond();

            // Random time
            int hour = (int) (Math.random() * 24);
            int minute = (int) (Math.random() * 60);
            LocalTime time = LocalTime.of(hour, minute);

            int maxRepetitions = Math.min(repetitions, maxActivitiesPerDay);

            Event e = new Event(a, maxRepetitions, day, time);
            events.add(e);

            // calories
            calories += a.calculateCalories(user) * maxRepetitions;

            // update repetitions
            activitiesRepetitions.get(0).setSecond(maxRepetitions);

            // keep adding if possible (distinct activities)
            for (int i = 1; i < activitiesRepetitions.size(); i++) {
                if (activitiesRepetitions.get(i).getFirst().getHard() == a.getHard()) {
                    continue;
                }
                a = activitiesRepetitions.get(i).getFirst();
                repetitions = activitiesRepetitions.get(i).getSecond();

                // Random time
                hour = (int) (Math.random() * 24);
                minute = (int) (Math.random() * 60);
                time = LocalTime.of(hour, minute);

                maxRepetitions = Math.min(repetitions, maxActivitiesPerDay);

                e = new Event(a, maxRepetitions, day, time);
                events.add(e);

                // calories
                calories += a.calculateCalories(user) * maxRepetitions;

                // update repetitions
                activitiesRepetitions.get(i).setSecond(maxRepetitions);
            }
        }
        Plan plan = new Plan("Plan based on goals", events);
        return plan.clone(); */
}

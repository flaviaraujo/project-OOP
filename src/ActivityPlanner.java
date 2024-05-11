package src;

import src.exceptions.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

// exception handling imports
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

public class ActivityPlanner implements Serializable {

    /* Attributes */
    private HashMap<Integer, User> users; // currently loaded users
    private boolean updatedState; // flag to check if state has been updated
    private String stateFilepath; // state filepath

    /* Constructors */
    public ActivityPlanner() {
        this.users = new HashMap<Integer, User>(); // initialize users
        this.updatedState = false; // by default, state is not updated
        this.stateFilepath = "data/state.ser"; // default state filepath
    }

    /* Setters */
    public void setStateFilepath(String stateFilepath) {
        this.stateFilepath = stateFilepath;
    }

    /* Getters */
    public String getStateFilepath() {
        return this.stateFilepath;
    }

    public boolean getUpdatedState() {
        return this.updatedState;
    }

    public HashMap<Integer, User> getUsers() {
        HashMap<Integer, User> users = new HashMap<Integer, User>();
        for (Entry<Integer, User> entry : this.users.entrySet()) {
            users.put(entry.getKey(), entry.getValue().clone());
        }
        return users;
    }

    /* User management methods */

    public int getUsersSize() {
        return this.users.size();
    }

    public User getUserById(int id) throws UserNotFoundException {
        if (!this.users.containsKey(id)) {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }
        return this.users.get(id).clone();
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        for (User u : this.users.values()) {
            if (u.getEmail().equals(email)) {
                return u.clone();
            }
        }
        throw new UserNotFoundException("User with email " + email + " not found.");
    }

    public int getNextUserId() {
        // increment the biggest id by 1 and set it as the new user id
        return users.size() == 0 ? 1 : users.keySet().stream().max(Integer::compare).get() + 1;
    }

    public ArrayList<Integer> getUsersIds() {
        return new ArrayList<Integer>(this.users.keySet());
    }

    public ArrayList<String> getUsersEmails() {
        ArrayList<String> emails = new ArrayList<String>();
        for (User u : this.users.values()) {
            emails.add(u.getEmail());
        }
        return emails;
    }

    public void addUser(User user) {
        this.users.put(user.getId(), user.clone());
        this.updatedState = true;
    }

    public void removeUser(User user) {
        this.users.remove(user.getId());
        this.updatedState = true;
    }

    public void updateUser(User user) {
        this.users.put(user.getId(), user.clone());
        this.updatedState = true;
    }

    public User searchUser(int id) {
        return this.users.get(id);
    }

    public User searchUser(String email) {
        return this.users.values().stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst()
            .orElse(null);
    }

    public ArrayList<String> getUserTypes() {
        ArrayList<String> types = new ArrayList<String>();

        // List the classes that are in the users package
        for (Class<?> c : getClasses("src.users")) {
            types.add(c.getSimpleName());
        }

        return types;
    }

    /* Activity management methods */

    public ArrayList<String> getActivitiesNames(ArrayList<Activity> activities) {
        ArrayList<String> names = new ArrayList<String>();

        for (Activity a : activities) {
            names.add(a.getName());
        }

        return names;
    }

    public ArrayList<String> getActivitiesNames(User u) {
        ArrayList<Activity> activities = u.getActivities();
        return getActivitiesNames(activities);
    }

    public ArrayList<String> getActivities() {
        ArrayList<String> activities = new ArrayList<String>();

        // List the classes that are in the activities package
        for (Class<?> c : getClasses("src.activities")) {
            activities.add(c.getSimpleName());
        }

        return activities;
    }

    public Activity getUserActivity(ArrayList<Activity> activities, String activityName) throws ActivityNotFoundException {
        Activity a = activities.stream()
            .filter(activity -> activity.getName().equals(activityName))
            .findFirst()
            .orElse(null);
        if (a == null) {
            throw new ActivityNotFoundException("Activity " + activityName + " not found.");
        }
        return a;
    }

    /* Utility methods */

    private List<Class<?>> getClasses(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            java.net.URL resource = classLoader.getResource(path);
            java.io.File directory = new java.io.File(resource.toURI());
            for (java.io.File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    // skip subpackages
                    // classes.addAll(getClasses(packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    classes.add(Class.forName(packageName + "." + className));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    /* Plan method */

    public String createPlanBasedOnGoals(
        User u,
        int caloriesGoal,
        int maxActivitiesPerDay,
        int maxDisitinctActivitiesPerDay,
        int nActivityRepetitionPerWeek,
        ArrayList<Activity> selectedActivities
    ) {
        // restrictions
        // hard activituies can't be in the same day and consecutive days
        // max activities per day (max 3)
        // min distinct activities per day (max 3)
        // nRepActivityPerWeek (min 1, no max)

        // encapsulate user to avoid changes
        User user = u.clone();

        // days until the next hard activity is possible
        int daysUntilHard = 0;
        // boolean canBeHard: (daysUntilHard == 0);
        // reset: daysUntilHard = 2;

        // number of disitinct activities added in one day
        int distinctCount = 0;

        // total number of activities (for display purposes only)
        int totalActivities = 0;

        // seperate activities by hard and not hard (sorted by calories desc)
        ActivityRepetition arAux = new ActivityRepetition();

        ArrayList<ActivityRepetition> activitiesHard =
            arAux.create(selectedActivities, nActivityRepetitionPerWeek, maxActivitiesPerDay, user, true);

        ArrayList<ActivityRepetition> activities =
            arAux.create(selectedActivities, nActivityRepetitionPerWeek, maxActivitiesPerDay, user, false);

        ActivityRepetition ar = null;
        Activity a = null;
        int repetitionsLeft = 0;

        ActivityRepetition arHard = null;
        Activity aHard = null;
        int repetitionsLeftHard = 0;

        boolean saturdayCanBeHard = true;

        ArrayList<Event> events = new ArrayList<Event>();

        for (int day = 1; day <= 7; day++) {

            distinctCount = 0;

            if (daysUntilHard > 0) {
                daysUntilHard--;
            }

            if (activitiesHard.isEmpty() && activities.isEmpty()) {
                break;
            }

            for (int i = 0; i < maxActivitiesPerDay;) {

                if (activitiesHard.isEmpty() && activities.isEmpty()) {
                    break;
                }

                if (!activities.isEmpty()) {
                    ar = activities.get(0);
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

                if (ar == null ||
                    (arHard != null &&
                    aHard.calculateCalories(user) >= a.calculateCalories(user) * nActivityOfThisType &&
                    daysUntilHard == 0 &&
                    day != 7) ||
                    (day == 7 && saturdayCanBeHard &&
                    (arHard != null &&
                    aHard.calculateCalories(user) >= a.calculateCalories(user) * nActivityOfThisType &&
                    daysUntilHard == 0))
                ) {
                    // add hard activity (only one can be added)
                    Event event = new Event(aHard, 1, day, LocalTime.of(8 + 4 * i, 0));
                    events.add(event);

                    // update repetitions
                    repetitionsLeftHard--;
                    arHard.setRepetitionsLeft(repetitionsLeftHard);

                    // hard activities logic (reset daysUntilHard)
                    daysUntilHard = 2;

                    // total activities counter
                    totalActivities++;

                    // increment activity iterator
                    i++;

                    // re-sort
                    activitiesHard = arAux.sort(activitiesHard, 1, user);

                    // if it's sunday then saturday can't be hard activity day
                    if (day == 1) {
                        saturdayCanBeHard = false;
                    }
                }
                else if (ar != null) {
                    // add normal activity
                    Event event = new Event(a, nActivityOfThisType, day, LocalTime.of(8 + 4 * i, 0));
                    events.add(event);

                    // update repetitions
                    repetitionsLeft -= nActivityOfThisType;
                    ar.setRepetitionsLeft(repetitionsLeft);

                    // total activities counter
                    totalActivities += nActivityOfThisType;

                    // increment activity iterator
                    i += nActivityOfThisType;

                    // re-sort
                    activities = arAux.sort(activities, maxActivitiesPerDay - i, user);
                }

                // increment distinct activities counter
                distinctCount++;
                if (distinctCount >= maxDisitinctActivitiesPerDay) {
                    break;
                }
            }
        }

        StringBuilder output = new StringBuilder();
        int possibleActivitiesPerWeek = Math.min(maxActivitiesPerDay * 7, nActivityRepetitionPerWeek * selectedActivities.size());

        // Check if plan meets the calories goal
        int calories = 0;
        for (Event e : events) {
            calories += e.getActivity().calculateCalories(user) * e.getActivityRepetitions();
        }
        if (calories < caloriesGoal) {
            output.append("Based on your goals, it's impossible to create a plan.\n");
            output.append("Total activities: " + totalActivities + " / " + possibleActivitiesPerWeek + "\n");
            output.append("Calories: " + calories + " / " + caloriesGoal + "\n");
            return output.toString();
        }

        // create plan
        Plan plan = new Plan("Plan based on goals", events);

        // display plan and relevant info
        output.append(plan.getName() + "\n");
        for (Event e : plan.getEvents()) {
            output.append(e + "\n");
            output.append("Expected calories: " +
                e.getActivity().calculateCalories(user) * e.getActivityRepetitions() + "\n");
            output.append("\n");
        }
        output.append("Total activities: " + totalActivities + " / " + possibleActivitiesPerWeek + "\n");
        output.append("Calories: " + calories + " / " + caloriesGoal + "\n");
        output.append("Plan generated successfully.\n");

        // add plan to user
        user.setPlan(plan);

        // update user
        this.updateUser(user);

        return output.toString();
    }

    /* Simulation method */

    public String runSimulation(LocalDate startDate, LocalDate endDate) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");

        StringBuilder output = new StringBuilder();

        output.append("Simulation start date: " + startDate.format(dateFormatter) + "\n");
        output.append("Simulation end date: " + endDate.format(dateFormatter) + "\n");
        output.append("Starting simulation...\n");

        int days = (int) ChronoUnit.DAYS.between(startDate, endDate);
        output.append("Number of days: " + days + "\n");
        int currentWeekDay = (startDate.getDayOfWeek().getValue() % 7) + 1; // 1 = Sunday ... 7 = Saturday

        int usersCount = 0; // number of users with training plans

        for (Entry<Integer, User> entry : this.users.entrySet()) {
            User u = entry.getValue();
            if (u.getPlan() != null) {
                // output.append("User \"" + u.getName() + "\" has a training plan.\n");
                usersCount++;
            } else {
                // check for users with no training plans
                output.append("Skipping user \"" + u.getName() + "\" as there is no training plan.\n");
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

            output.append(
                "Day " + (day + 1) + " " +
                Controller.convertDayToString(currentWeekDay) + " " +
                startDate.plusDays(day).format(dateFormatter) + "\n"
            );

            for (Entry<Integer, User> entry : this.users.entrySet()) {
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
                            u.register(eventDateTime, r);

                            output.append(
                                "User \"" + u.getName() + '"' +
                                " completed activity \"" + a.getName() + '"' +
                                " at " + eventDateTime.format(timeFormatter) +
                                " and burned " + calories + " calories." + "\n"
                            );
                        }
                    }
                }
            }

            currentWeekDay++;
        }

        output.append("Simulation completed. " + days + " days simulated.\n");
        output.append("Users with training plans: " + usersCount + "\n");
        output.append("Activities: " + activitiesCount + "\n");
        output.append("Registered activities: " + registeredActivities + "\n");
        output.append("Total calories burned: " + totalCalories + "\n");
        return output.toString();
    }

    /* Statistics methods */

    // 1. The user with most calories burned
    // 1.1 All time
    public User mostCaloriesBurned() {

        User user = null;
        if (!this.users.isEmpty()) {
            user = this.users.entrySet().iterator().next().getValue();
        }
        int max = 0;

        for (User u : this.users.values()) {

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
    public User mostCaloriesBurned(LocalDate start, LocalDate end) {

        User user = null;
        if (!this.users.isEmpty()) {
            user = this.users.entrySet().iterator().next().getValue();
        }
        int max = 0;

        for (User u : this.users.values()) {

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
    public User mostActivities() {

        User user = null;
        if (!this.users.isEmpty()) {
            user = this.users.entrySet().iterator().next().getValue();
        }
        int max = 0;

        // for each user get the number of registered activities
        for (User u : this.users.values()) {

            int tmp = u.getRegisters().size();
            if (tmp > max) {
                max = tmp;
                user = u;
            }
        }

        return user;
    }

    // 2.2 In a period of time (date -> date)
    public User mostActivities(LocalDate start, LocalDate end) {

        User user = null;
        if (!this.users.isEmpty()) {
            user = this.users.entrySet().iterator().next().getValue();
        }
        int max = 0;

        // for each user get the number of registered activities
        for (User u : this.users.values()) {

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
    public String mostPracticedActivityType() {

        String result = "None";
        HashMap<String, Integer> map = new HashMap<>();

        for (User u : this.users.values()) {

            for (Activity r : u.getRegisters().values()) {
                String type = r.getClass().getSimpleName();
                if (map.containsKey(type)) {
                    map.put(type, map.get(type) + 1);
                }
                else {
                    map.put(type, 1);
                }
            }
        }

        int max = 0;
        for (Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                result = entry.getKey();
            }
        }

        return result;
    }

    // 4. How many km’s were traveled by one user
    // 4.1 All time
    public double kmTraveled(User user) {

        int c = 0;

        for (Activity r : user.getRegisters().values()) {

            if (r.isDistanceBased()) {
                c += r.getDistance();
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

                if (r.isDistanceBased()) {
                    c += r.getDistance();
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

            if (r.isAltimetryBased()) {
                c += r.getAltimetry();
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

                if (r.isAltimetryBased()) {
                    c += r.getAltimetry();
                }
            }
        }

        return c;
    }

    // 6. Whats the practice plan with more calories burned
    public String mostCaloriesBurnedPlan() {

        User user = null;
        Plan plan = null;
        int max = 0;

        for (User u : this.users.values()) {

            Plan p = u.getPlan();
            if (p == null)
                continue;
            else if (plan == null) {
                plan = p;
                user = u;
                for (Event e : p.getEvents()) {
                    max += e.getActivity().calculateCalories(u) * e.getActivityRepetitions();
                }
                continue;
            }

            int tmp = 0;
            for (Event e : p.getEvents()) {
                tmp += e.getActivity().calculateCalories(u) * e.getActivityRepetitions();
            }
            if (tmp > max) {
                max = tmp;
                user = u;
                plan = p;
            }
        }

        String result;
        if (plan == null) {
            result = "No plans found.";
        }
        else {
            result = "The plan with the most calories burned is \""
                + plan.getName() + "\" with " + max + " calories burned, by "
                + "the user: " + user.getName();
        }

        return result;
    }

    // 7. List the practiced activities of a user
    public String listActivities(User user) {
        StringBuilder output = new StringBuilder();
        ArrayList<Activity> registers = new ArrayList<Activity>(user.getRegisters().values());
        ArrayList<Activity> result = new ArrayList<Activity>();

        // List unique activities of the user
        for (int i = 0; i < registers.size(); i++) {

            // check if the activity is already in the result list
            if (!result.contains(registers.get(i))) {
                result.add(registers.get(i));
                output.append(registers.get(i));
            }
        }

        return output.toString();
    }

    /* State management methods */

    public void saveState() throws StateNotSavedException {

        if (!this.updatedState) {
            throw new StateNotSavedException("No data to save");
        }

        try {
            FileOutputStream fileOut = new FileOutputStream(this.stateFilepath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(this);
            out.flush();
            out.close();
            fileOut.close();

            this.updatedState = false;
        }
        catch (FileNotFoundException e) {
            throw new StateNotSavedException("File not found: \"" + this.stateFilepath + "\"");
        }
        catch (InvalidClassException e) {
            throw new StateNotSavedException("Invalid class: " + e.getMessage());
        }
        catch (NotSerializableException e) {
            throw new StateNotSavedException("Object not serializable: " + e.getMessage());
        }
        catch (IOException e) {
            throw new StateNotSavedException("IOException. Error: " + e.getMessage());
        }
        catch (Exception e) {
            throw new StateNotSavedException("Error saving state: " + e.getMessage() + ".\n");
        }
    }

    public void loadState() throws StateNotLoadedException {

        try {
            FileInputStream fileIn = new FileInputStream(this.stateFilepath);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            ActivityPlanner planner = (ActivityPlanner) in.readObject();
            in.close();
            fileIn.close();
            this.users = planner.users;
            this.updatedState = false;
            this.stateFilepath = planner.stateFilepath;
        }
        catch (FileNotFoundException e) {
            throw new StateNotLoadedException("File not found: \"" + this.stateFilepath + "\"");
        }
        catch (ClassNotFoundException e) {
            throw new StateNotLoadedException("Couldn't find class: " + e.getMessage());
        }
        catch (InvalidClassException e) {
            throw new StateNotLoadedException("Invalid class: " + e.getMessage());
        }
        catch (StreamCorruptedException e) {
            throw new StateNotLoadedException("Stream corrupted: " + e.getMessage());
        }
        catch (OptionalDataException e) {
            throw new StateNotLoadedException("Optional data found: " + e.getMessage());
        }
        catch (ClassCastException e) {
            throw new StateNotLoadedException("Couldn't cast object: " + e.getMessage());
        }
        catch (NotSerializableException e) {
            throw new StateNotLoadedException("Object not serializable: " + e.getMessage());
        }
        catch (IOException e) {
            throw new StateNotLoadedException("IOException: " + e.getMessage());
        }
        catch (Exception e) {
            throw new StateNotLoadedException("Error loading state: " + e.getMessage());
        }
    }
}

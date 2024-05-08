package src;

import src.activityTypes.*;

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

// custom exceptions
import src.exceptions.UserNotFoundException;
import src.exceptions.StateNotSavedException;
import src.exceptions.StateNotLoadedException;

public class ActivityPlanner {

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

    /* Getters and Setters */
    public String getStateFilepath() {
        return this.stateFilepath;
    }

    public boolean getUpdatedState() {
        return this.updatedState;
    }

    public void setStateFilepath(String stateFilepath) {
        this.stateFilepath = stateFilepath;
    }

    public HashMap<Integer, User> getUsers() {
        HashMap<Integer, User> users = new HashMap<Integer, User>();
        for (Entry<Integer, User> entry : this.users.entrySet()) {
            users.put(entry.getKey(), entry.getValue().clone());
        }
        return users;
    }

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

    /* simulation method */
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
                event.convertDayToString(currentWeekDay) + " " +
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

    // 3. The type of activity most practiced by the users TODO new activities
    public String mostPracticedActivityType() {

        String result = "None";
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (User u : this.users.values()) {

            for (Activity r : u.getRegisters().values()) {
                int type = r.getType();
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
                result = a.getTypeString((int) entry.getKey());
            }
        }

        return result;
    }

    // 4. How many km’s were traveled by one user
    // 4.1 All time
    public double kmTraveled(User user) {

        int c = 0;

        for (Activity r : user.getRegisters().values()) {

            if (r.isDistance()) {
                Distance da = (Distance) r;
                c += da.getDistance();
            }
            else if (r.isDistanceAltimetry()) {
                DistanceAltimetry da = (DistanceAltimetry) r;
                c += da.getDistance();
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

                if (r.isDistance()) {
                    Distance da = (Distance) r;
                    c += da.getDistance();
                }
                else if (r.isDistanceAltimetry()) {
                    DistanceAltimetry da = (DistanceAltimetry) r;
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

        for (Activity r : user.getRegisters().values()) {

            if (r.isDistanceAltimetry()) {
                DistanceAltimetry da = (DistanceAltimetry) r;
                c += da.getAltimetry();
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

                if (r.isDistanceAltimetry()) {
                    DistanceAltimetry da = (DistanceAltimetry) r;
                    c += da.getAltimetry();
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
                    max += e.getActivity().calculateCalories(u);
                }
                continue;
            }

            int tmp = 0;
            for (Event e : p.getEvents()) {
                tmp += e.getActivity().calculateCalories(u);
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

    /* state management methods */
    public void saveState() throws StateNotSavedException {

        if (!this.updatedState) {
            throw new StateNotSavedException("No data to save");
        }

        try {
            FileOutputStream fileOut = new FileOutputStream(this.stateFilepath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(this.users);

            out.flush();
            out.close();
            fileOut.close();

            this.updatedState = false;
        }
        catch (NotSerializableException e) {
            throw new StateNotSavedException("Object not serializable: " + e.getMessage());
        }
        catch (FileNotFoundException e) {
            throw new StateNotSavedException("File not found: \"" + this.stateFilepath + "\"");
        }
        catch (IOException e) {
            throw new StateNotSavedException("IOException. Error: " + e.getMessage());
        }
        catch (Exception e) {
            throw new StateNotSavedException("Error saving state: " + e.getMessage() + ".\n");
            // e.printStackTrace();
        }
    }

    public void loadState() throws StateNotLoadedException {

        try {
            FileInputStream fileIn = new FileInputStream(this.stateFilepath);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            HashMap<?, ?> tmpUsers = (HashMap<?, ?>) in.readObject();

            this.users = new HashMap<Integer, User>(tmpUsers.size());
            for (Object obj : tmpUsers.values()) {
                if (obj instanceof User) {
                    User u = (User) obj;
                    this.users.put(u.getId(), u);
                }
            }

            in.close();
            fileIn.close();
            this.updatedState = false;

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
            // e.printStackTrace();
        }
    }
}

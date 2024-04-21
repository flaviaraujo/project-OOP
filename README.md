# POO

## Requirements

- (10 points) The app allows registering users, activities, and plans;
- (12 points) Registering the activities done by the users, with respective calories consumption;
- (16 points) Statistics on the program state
  1. The user with the most calories burned in a period of time or ever
  2. The user with the most activities in a period of time or ever
  3. The type of activity most practiced by the users
  4. How many km's were traveled by one user in a period of time or ever
  5. How many meters of altimetry were climbed by one user in a period of time or ever
  6. Whats the practice plan with more calories burned
  7. List the activities of a user
- (18 points) Creating the notion of a *Hard* activity, with the possibility of adding new activities of this type by adding some lines of code (*Hard* activities have special properties when selected for a practice plan)
- (20 points) Generating a training plan for each user, according to the specified user's goals:
  - **The type of activity**: If a user selects a *Hard* activity, it can't have more than 1 activity of this type per day, and they can't be in consecutive days;
  - **Max number of activities per day**: should never be more than 3;
  - **Max number of distinct activities per day**: no restrictions;
  - **Expected amount of calories burned**: the plan should be generated to burn at least this amount of calories;

## Further Objectives

- Allowing saving and loading the state of the program
- Simulating the user's progress by skipping time
- Allow each user to register the activities they have done (with indicators of the activity like distance, altimetry, time, calories, etc)

## TODO list

- [x] Remove class Register (Notes - 1)
- [x] Activity name problem with spaces when searching
- [x] Change the users from ArrayList to a HashMap (Notes - 2)
- [x] Change user to include weight, height and age
- [ ] fix activity name input in create plan interactively
- [ ] review functions to get calories burned
- [x] Command line arguments
- [x] Add user perspective
- [ ] Encapsulate app; add clone's
- [ ] Add more exception handling - nextInt, next and nextLine
- [ ] Add throw to functions that can return null (search, get, etc)
- [ ] searchActivity function exception handling + rename this method
- [ ] When calling User.deleteActivity() check in the registers if the activity is being used and if so throw exception.
- [ ] Add option to create activity when registering a register, if this one is unique (cpm registers) add it to the user activities
- [x] Sort events in a plan by date time
- [x] Add hard notion to activities
- [ ] Add create plan based on user objectives
- [ ] MVC in Main

## Notes

### 1.

What will change when replacing the Register with activities?

```java
private HashMap<LocalDateTime, Register> registers;
// to
private HashMap<LocalDateTime, Activity> registers;
```

Classes that will be affected:
- [x] User
- [x] Activity
- [x] Main (Menu 3)
- [x] Stats
- [x] Simulation (Simulation.run())

Activity rename caloriesBurned method to calculateCaloriesBurned
Find out if you should put the calories burned at 0 when creating the activity.

### 2.

```java
HashMap<Integer, User> users;
```

Where the key is the user id.

When changing the users from ArrayList to a HashMap, the following will change:

- [x] User.create()
- [x] User.delete()
- [x] User.view()
- [x] Case 4: user manage menu: for (User u : m.users) System.out.println(u);
- [x] User.search()
- [x] Simulation.run()
- [x] Stats.statsMenu()
- [x] Main.loadState()

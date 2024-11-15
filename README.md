# Activity Planner

A Fitness program developed in Java for the Object Oriented Programming class during the 2023/2024 academic year.

- [Description and Requirements](project.pdf)

- [Report](report/output/main.pdf)

- [Class Diagram (pdf)](diagram/diagram.pdf) or [Class Diagram (png)](diagram/diagram.png)

- [Binary Program State](data/state.ser)

## Description

Activity Planner is a program that allows users to register their activities
and generate a training plan according to their goals,
as well as providing various statistics on the program state,
and running simulations.

The program state can be saved and loaded from a file,
and the user can select a specific user to view the program from his perspective.

It implements a version of the MVC (Model-View-Controller) architecture,
where the model is represented by the class `ActivityPlanner`,
and the view and controller components are represented by the class `Controller`.

The application uses aggregation to assure data encapsulation, and it was designed to be easily extensible,
allowing the addition of new types of users and activities by adding the respective subclasses,
not requiring changes in the existing code.

## Compilation and Execution

To compile the program, run the following command:

```
make
```

As for executing the program, run the following command:

```
java src.Controller
```

## Usage

The program has the following options:

```
Usage: java src.Controller [--load <file>] [--user <--id|--email> <id|email>]
Options:
  -h --help: show this help message
  -l --load: load a program state from file
  -u --user: 
select an user by id or email to view his perspective
  -e --email: select an user by email (use with --user option after --load option)
  -i --id: select an user by ID (use with --user option after --load option)
```

## Requirements

All of the following requirements were implemented:

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
- (18 points) Creating the notion of a *hard* activity, with the possibility of adding new activities of this type by adding some lines of code (*Hard* activities have special properties when selected for a practice plan)
- (20 points) Generating a training plan for each user, according to the specified user's goals:
  - **The type of activity**: If a user selects a *hard* activity, he can't have more than 1 activity of this type per day, and they can't be in consecutive days;
  - **Max number of activities per day**: should never be more than 3;
  - **Max number of distinct activities per day**: no restrictions;
  - **Expected amount of calories burned**: the plan should be generated to burn at least this amount of calories;

## Authors

- Flávia Araújo - [@flaviaraujo](https://github.com/flaviaraujo)
- Miguel Carvalho - [@migueltc13](https://github.com/migueltc13)

#### Final Grade: 17

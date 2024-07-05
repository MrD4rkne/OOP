# Simulation of Public Transport

This app was developed as an assigment during OOP couse as a part of the Computer Science Bachelor's Studies at the University of Warsaw. <br>
In this project the main goal is simulating days in a public transport system. There are two main actors: Trams and Passengers.

## Usage

- download whole folder
- compile with Java compilation tools (or with IDE like Intelij).
- run program and enter simulation data.

### Input

- Program reads simulation data from System.in and runs simulation.

Input template:
\*comment\*
\<data\>
```
<number of days>
<stops capacity>
<number of stops>
*now for each stop x number above*
<name of stop>
<trams capacity>
<number of lines>
*now for each line*
<number of trams> <number of stops>
*now for each stop*
<stop's name> <interval>
```
- last interval is time during which tram waits on loop (on both sides).
- stop's inside line must be declared before in stops' section

### Logic

- Each line has it's list of stops and intervals of time between them. 
- For each line trams start from both sides at 6:00 and then (for each side) next tram waiting before starting it's day.
- When tram arrives on stop, passengers who wants to get out on this stops try to leave (limited by space on stop). Then passengers from stop enter only if there is space left inside vehicle.
- Trams drive their lines in loop.
- Trams starts full route (both ways) only if it is before 23:00. It ends day at the stop where they originaly started.

### Output

- Each event is printed out to the System.out.
- There are local and global statistics.

## Original task

[Task in Polish](./task.pdf)

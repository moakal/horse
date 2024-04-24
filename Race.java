/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author Akbar Ali <230097939>
 * @version 18 Apr 2024
 */

import java.util.Arrays;
import java.util.Scanner;
import java.lang.Math;

public class Race {
    private int raceLength;
    private Horse[] horses = new Horse[3];

    /* Constructor */
    public Race(int distance)
    {
        raceLength = distance;
    }

    public void addHorse(Horse theHorse, int laneNumber)
    {
        if (laneNumber >= 1 && laneNumber <= horses.length) {
            horses[laneNumber - 1] = theHorse;
        } else {
            System.out.println("Cannot add horse to lane " + laneNumber + " because there is no such lane");
        }
    }

    public void startRace()
    {
        for (Horse horse : horses) {
            horse.goBackToStart();
        }

        boolean raceWon = false;
        boolean allHorsesFallen = false;
        
        while (!raceWon && !allHorsesFallen) {
            for (Horse horse : horses) {
                moveHorse(horse);
            }

            printRace();

            raceWon = raceWon();
            allHorsesFallen = allHorsesFallen();

            if (!raceWon && !allHorsesFallen) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (allHorsesFallen) {
            System.out.println("\nAll horses have fallen!");
            return;
        }

        for (Horse horse : horses) {
                if (raceWonBy(horse)) {
                    System.out.println("\n" + horse.getName() + " has won!");
                    break; /* Exit the loop once a winner is found */
                }
            }
        }


    private boolean raceWon()
    {
        return Arrays.stream(horses).anyMatch(this::raceWonBy);
    }

    private boolean raceWonBy(Horse theHorse)
    {
        return theHorse.getDistanceTravelled() == raceLength;
    }

    private void moveHorse(Horse theHorse)
    {
        if (!theHorse.hasFallen()) {
            if (Math.random() < theHorse.getConfidence()) {
                theHorse.moveForward();
            }

            if (Math.random() < (0.1 * theHorse.getConfidence() * theHorse.getConfidence())) {
                theHorse.fall();
            }
        }
    }

    private void printRace()
    {
        System.out.println("\n".repeat(50)); /* Clear the terminal */

        printTrackEdge();
        for (Horse horse : horses) {
            printLane(horse);
        }
        printTrackEdge();
    }

    private void printTrackEdge()
    {
        System.out.println("=".repeat(raceLength + 3));
    }

    private void printLane(Horse theHorse)
    {
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();

        StringBuilder lane = new StringBuilder("|");
        lane.append(" ".repeat(spacesBefore));
        lane.append(theHorse.hasFallen() ? "\u2322" : theHorse.getSymbol());
        lane.append(" ".repeat(spacesAfter));
        lane.append("| ");
        lane.append(theHorse.getName()).append(" (CONFIDENCE: ").append(theHorse.getConfidence()).append(")");
        if (theHorse.hasFallen()) {
            lane.append(" (FALLEN)");
        }
        System.out.println(lane);
    }

    private boolean allHorsesFallen()
    {
        for (Horse horse : horses) {
            if (!horse.hasFallen()) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args)
    {
        Horse[] horses = new Horse[3];
        String name;

        try (Scanner scanner = new Scanner(System.in)) {
            for (int i = 0; i < horses.length; i++) {
                System.out.println("Enter the name of horse " + (i + 1) + ": ");
                name = scanner.nextLine();
                horses[i] = new Horse('\u265E', name, Math.round(Math.random() * 100.0) / 100.0);
            }
        }

        Race race = new Race(10);
        race.addHorse(horses[0], 1);
        race.addHorse(horses[1], 2);
        race.addHorse(horses[2], 3);

        race.startRace();
    }
}

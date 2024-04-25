/**
 * Horse class represents a horse in the simulation.
 * 
 * @author Akbar Ali <230097939>
 * @version 18 Apr 2024
 */

import java.awt.*;

public class Horse
{
    private String name;
    private Color color;
    private int distanceTravelled;
    private int wins;
    private int races;
    private double confidence;

    public Horse(String name, Color color)
    {
        this.name = name;
        this.color = color;
        this.confidence = Math.random();
        this.distanceTravelled = 0;
        this.wins = 0;
        this.races = 0;
    }

    public String getName()
    {
        return name;
    }

    public Color getColor()
    {
        return color;
    }

    public int getDistanceTravelled()
    {
        return distanceTravelled;
    }

    public int getWins()
    {
        return wins;
    }

    public int getRaces()
    {
        return races;
    }

    public double getConfidence()
    {
        return confidence;
    }

    public boolean hasFallen()
    {
        return confidence == 0;
    }

    public void move(int distance)
    {
        double speedMultiplier = 1 + confidence; // Confidence adds to the base speed
        distanceTravelled += (int) (distance * speedMultiplier);

        if (Math.random() < confidence) {
            fall();
        }
    }

    public void resetDistance()
    {
        distanceTravelled = 0;
    }

    public void addWin()
    {
        wins++;
        confidence += 0.1;
        if (confidence > 1) {
            confidence = 1;
        }
    }

    public void addRace()
    {
        races++;
    }

    public void moveForward(int distance)
    {
        move(distance);
    }

    public void fall()
    {
        confidence -= 0.1;
        if (confidence < 0) {
            confidence = 0;
        }
    }

    public void goBackToStart()
    {
        distanceTravelled = 0;
    }
    
}
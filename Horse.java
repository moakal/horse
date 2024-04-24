/**
 * Horse class represents a horse in the simulation.
 * 
 * @author Akbar Ali <230097939>
 * @version 18 Apr 2024
 */

public class Horse
{
    private char horseSymbol;
    private String horseName;
    private int distanceTravelled;
    private boolean hasFallen;
    private double horseConfidence;
    
    /* Constructor */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
        this.horseSymbol = horseSymbol;
        this.horseName = horseName;
        this.horseConfidence = horseConfidence;
        this.distanceTravelled = 0;
        this.hasFallen = false;
    }
    
    public void fall()
    {
        hasFallen = true;
    }
    
    public double getConfidence()
    {
        return horseConfidence;
    }
    
    public int getDistanceTravelled()
    {
        return distanceTravelled;
    }
    
    public String getName()
    {
        return horseName;
    }
    
    public char getSymbol()
    {
        return horseSymbol;
    }
    
    public void goBackToStart()
    {
        distanceTravelled = 0;
        hasFallen = false;
    }
    
    public boolean hasFallen()
    {
        return hasFallen;
    }

    public void moveForward()
    {
        distanceTravelled++;
    }

    public void setConfidence(double newConfidence)
    {
        horseConfidence = newConfidence;
    }
    
    public void setSymbol(char newSymbol)
    {
        horseSymbol = newSymbol;
    }
}

/**
 * This class is used to create a Track object which has a name and length.
 * @author Akbar Ali <230097939>
 * @version 19 Apr 2024
 */

public class Track
{
    private String name;
    private int length;

    public Track(String name, int length)
    {
        this.name = name;
        this.length = length;
    }

    public String getName()
    {
        return name;
    }

    public int getLength()
    {
        return length;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setLength(int length)
    {
        this.length = length;
    }
}

/**
 * Horse track represents a horse track in the simulation.
 * 
 * @author Akbar Ali <230097939>
 * @version 18 Apr 2024
 */


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HorseTrack extends JPanel
{
    private List<Horse> horses;

    public HorseTrack() {
        this.horses = new ArrayList<>();
        horses.add(new Horse(50, 50, Color.RED, this));
        horses.add(new Horse(50, 100, Color.GREEN, this));
        horses.add(new Horse(50, 150, Color.BLUE, this));

        for (Horse horse : horses) {
            new Thread(horse).start();
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.WHITE);
        g.fillRect(getWidth() - 20, 0, 20, getHeight());

        for (Horse horse : horses) {
            g.setColor(horse.getColor());
            g.fillRect(horse.getX(), horse.getY(), 20, 20);
        }
    }

    private static class Horse implements Runnable
    {
        private int x;
        private int y;
        private Color color;
        private HorseTrack horseTrack;

        public Horse(int x, int y, Color color, HorseTrack horseTrack)
        {
            this.x = x;
            this.y = y;
            this.color = color;
            this.horseTrack = horseTrack;
        }

        public int getX()
        {
            return x;
        }

        public int getY()
        {
            return y;
        }

        public Color getColor()
        {
            return color;
        }

        @Override
        public void run() 
        {
            while (x < 700) {
                x += (int) (Math.random() * 10);
                horseTrack.repaint();
                try {
                    Thread.sleep(100); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(color + " horse finished the race!");
        }
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Horse Race");
        HorseTrack horseTrack = new HorseTrack();
        frame.add(horseTrack);
        frame.setSize(800, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

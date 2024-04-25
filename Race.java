import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Race
{
    private int raceLength;
    private ArrayList<Horse> horses = new ArrayList<>();
    private JPanel raceTrackPanel;
    private Timer raceTimer;
    private final int updateInterval = 50;
    private Random random = new Random();

    private static Horse betOnHorse = null;
    private static double betAmount = 0;
    private static Horse winner = null;  


    public Race(int distance)
    {
        raceLength = distance;
    }

    public void addHorse(Horse theHorse)
    {
        horses.add(theHorse);
        theHorse.addRace();
    }

    public void startRace(JPanel trackPanel)
    {
        raceTrackPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int trackHeight = getHeight();
                int laneHeight = trackHeight / (horses.size() + 1);

                for (Horse horse : horses) {
                    int yPos = laneHeight * (horses.indexOf(horse) + 1);
                    Color color = horse.hasFallen() ? Color.BLACK : horse.getColor();
                    g.setColor(color);
                    int xPos = (int) ((double) horse.getDistanceTravelled() / raceLength * getWidth());
                    g.fillRect(xPos, yPos - 10, 20, 20);
                    g.drawString(horse.getName() + " - " + horse.getDistanceTravelled() + "m", xPos + 25, yPos + 5);
                }
            }
        };
        raceTrackPanel.setPreferredSize(new Dimension(800, 600));  // Set preferred size to ensure visibility
        trackPanel.add(raceTrackPanel);
        trackPanel.revalidate();
        trackPanel.repaint();

        for (Horse horse : horses) {
            horse.goBackToStart();
        }

        raceTimer = new Timer(updateInterval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveHorses();
                raceTrackPanel.repaint();
                if (raceWon()) {
                    stopRace();
                }
            }
        });
        raceTimer.start();
    }

    private void moveHorses()
    {
        for (Horse horse : horses) {
            if (!horse.hasFallen() && horse.getDistanceTravelled() < raceLength) {
                horse.moveForward(random.nextInt(5) + 1);
                if (random.nextDouble() < 0.05) {  // Simulate falling with a 5% chance
                    horse.fall();
                }
            }
        }
    }

    private boolean raceWon()
    {
        return horses.stream().anyMatch(h -> h.getDistanceTravelled() >= raceLength);
    }

    private void stopRace()
    {
        raceTimer.stop();
        Horse winner = horses.stream().max(Comparator.comparingInt(Horse::getDistanceTravelled)).orElse(null);
        double winnings = 0;
        if (winner != null && winner == betOnHorse) {
            winnings = betAmount * 2; // Assuming a fixed 2x return on winning bets
            JOptionPane.showMessageDialog(raceTrackPanel, winner.getName() + " wins the race! You won $" + String.format("%.2f", winnings), "Race Finished", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(raceTrackPanel, "No winner or your horse didn't win.", "Race Finished", JOptionPane.INFORMATION_MESSAGE);
        }

        betOnHorse = null;
        betAmount = 0;
    }

        public static double placeBet(Horse horse, double amount)
        {
        betOnHorse = horse;
        betAmount = amount;
        return checkWinnings();
    }

    private static double checkWinnings()
    {
        // This method should be called after the race is finished to check if the bet horse won
        if (betOnHorse != null && winner != null && betOnHorse == winner) {
            return betAmount * 2; // e.g., Simple 2x return on the bet
        }
        return 0;
    }
}
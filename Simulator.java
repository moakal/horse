/**
 * This is the main class for the Horse Racing Simulator.
 * It is responsible for the GUI.
 * @author Akbar Ali <230097939>
 * @version 19 Apr 2024
 * 
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;


public class Simulator
{
    private static JFrame frame;

    private static JPanel cardPanel;


    public static void main(String[] args)
    {
        runSimulator();
    }

    private static void runSimulator()
    {
        frame = new JFrame("Horse Racing Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setSize(720, 540);

        /* Set up cards for the menu settings */
        /* N: title
         * C: cards
         * S: menu
         */
        cardPanel = new JPanel(new CardLayout());

        frame.add(createMainTitle(), BorderLayout.NORTH);
        frame.add(cardPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static JPanel createMainTitle()
    {
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("Horse Racing Simulator");
        title.setFont(new Font("Helvetica", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        titlePanel.setBackground(Color.BLACK);
        titlePanel.setBorder(new EmptyBorder(5, 10, 0, 10));
        titlePanel.add(title);
        return titlePanel;
    }


}
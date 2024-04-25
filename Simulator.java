/**
 * This is the main class for the Horse Racing Simulator.
 * It is responsible for the GUI.
 * @author Akbar Ali <230097939>
 * @version 19 Apr 2024
 * 
 */

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;


public class Simulator
{
    private static JFrame frame;
    private static final String[] MENU_OPTIONS = {"Design track", "Customise horses", "Race", "Statistics", "Casino"};
    
    private static final int MIN_LENGTH = 50;
    private static final int MAX_LENGTH = 100;

    private static ArrayList<Track> tracks = new ArrayList<>();

    private static JPanel cardPanel;
    private static JPanel trackListPanel;
    private static JTable trackTable;

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
        cardPanel.add(createDesignTrack(), MENU_OPTIONS[0]);

        frame.add(createMainTitle(), BorderLayout.NORTH);
        frame.add(cardPanel, BorderLayout.CENTER);
        frame.add(createMenu(), BorderLayout.SOUTH);
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

    private static JPanel createMenu()
    {
        JPanel menu = new JPanel(new GridLayout(1, MENU_OPTIONS.length));
        for (String option : MENU_OPTIONS) {
            JButton button = new JButton(option);
            /* Special handling for cards with dynamic functions */
            if ("Race".equals(option)) {
                button.addActionListener(e -> {
                    updateComboBoxes();
                    ((CardLayout) cardPanel.getLayout()).show(cardPanel, option);
                });
            } else if ("Statistics".equals(option)) {
                button.addActionListener(e -> {
                    JPanel statsPanel = (JPanel) cardPanel.getComponent(3);
                    JScrollPane scrollPane = (JScrollPane) statsPanel.getComponent(1); 
                    JTable statsTable = (JTable) scrollPane.getViewport().getView();
                    refreshStatistics((DefaultTableModel) statsTable.getModel());
                    ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Statistics");
                });
            } else if ("Casino".equals(option)) {
                button.addActionListener(e -> {
                    updateCasinoHorseList(horseSelectionComboBox);
                    ((CardLayout) cardPanel.getLayout()).show(cardPanel, option);
                });
            } else {
                button.addActionListener(e -> ((CardLayout) cardPanel.getLayout()).show(cardPanel, option));
            }
            menu.add(button);
        }
        return menu;
    }

    /*
     * DESIGN TRACK
     */
    private static JPanel createDesignTrack()
    {
        JPanel designTrackPanel = new JPanel(new BorderLayout());
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        JLabel title = new JLabel("Design Track", JLabel.CENTER);
        title.setFont(new Font("Helvetica", Font.BOLD, 15));
        titlePanel.add(title, BorderLayout.CENTER);
        designTrackPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField nameField = new JTextField(20);

        JSlider trackLengthSlider = new JSlider(JSlider.HORIZONTAL, MIN_LENGTH, MAX_LENGTH, MIN_LENGTH);
        trackLengthSlider.setMajorTickSpacing(10);
        trackLengthSlider.setPaintTicks(true);
        trackLengthSlider.setPaintLabels(true);

        JButton addTrack = new JButton("Add Track");
        addTrack.addActionListener(e -> {
            String trackName = nameField.getText().trim();
            if (!trackName.isEmpty()) {
                Track newTrack = new Track(trackName, trackLengthSlider.getValue());
                tracks.add(newTrack);
                refreshTrackList();
                nameField.setText("");
                trackLengthSlider.setValue((MIN_LENGTH + MAX_LENGTH) / 2);
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a track name.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton removeTrack = new JButton("Remove Track");
        removeTrack.addActionListener(e -> removeSelectedTrack());

        inputPanel.add(new JLabel("Track Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Track Length:"));
        inputPanel.add(trackLengthSlider);
        inputPanel.add(addTrack);
        inputPanel.add(removeTrack);

        designTrackPanel.add(inputPanel, BorderLayout.CENTER); 
        designTrackPanel.add(createTrackListPanel(), BorderLayout.EAST);

        return designTrackPanel;
    }

    private static void removeSelectedTrack()
    {
        int selectedRow = trackTable.getSelectedRow();
        if (selectedRow != -1) {
            String trackName = (String) tableModel.getValueAt(selectedRow, 0);
            tracks.removeIf(track -> track.getName().equals(trackName));
            refreshTrackList();
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a track to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static JPanel createTrackListPanel()
    {
        trackListPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"Track Name", "Length"}, 0);
        trackTable = new JTable(tableModel);
        trackTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        trackListPanel.add(new JScrollPane(trackTable), BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete Track");
        deleteButton.addActionListener(e -> removeSelectedTrack());
        trackListPanel.add(deleteButton, BorderLayout.SOUTH);

        refreshTrackList();
        return trackListPanel;
    }

    private static void refreshTrackList()
    {
        tableModel.setRowCount(0);
        for (Track track : tracks) {
            tableModel.addRow(new String[]{track.getName(), String.valueOf(track.getLength())});
        }
    }
}
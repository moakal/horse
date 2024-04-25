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

    private static ArrayList<Horse> horses = new ArrayList<>();
    private static ArrayList<Track> tracks = new ArrayList<>();

    private static JPanel cardPanel;
    private static JPanel trackListPanel;
    private static JTable trackTable;
    private static DefaultTableModel tableModel;
    private static DefaultTableModel horseTableModel;

    private static JComboBox<String> horse1ComboBox = new JComboBox<>();
    private static JComboBox<String> horse2ComboBox = new JComboBox<>();
    private static JComboBox<String> horse3ComboBox = new JComboBox<>();
    private static JComboBox<String> trackComboBox = new JComboBox<>();
    private static JComboBox<String> horseSelectionComboBox = new JComboBox<>();


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
        cardPanel.add(createCustomiseHorses(), MENU_OPTIONS[1]);
        cardPanel.add(createRacePanel(), MENU_OPTIONS[2]);
        cardPanel.add(createStatisticsPanel(), MENU_OPTIONS[3]);
        cardPanel.add(createCasinoPanel(), MENU_OPTIONS[4]); 

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

    /*
     * CUSTOMISE HORSES
     */
    private static JPanel createCustomiseHorses()
    {
        JPanel customiseHorsesPanel = new JPanel(new BorderLayout());
                
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        JLabel title = new JLabel("Customise Horses", JLabel.CENTER);
        title.setFont(new Font("Helvetica", Font.BOLD, 15));
        titlePanel.add(title, BorderLayout.CENTER);
        customiseHorsesPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField nameField = new JTextField(15);
        JButton addHorse = new JButton("Add Horse");
        addHorse.addActionListener(e -> {
            String horseName = nameField.getText().trim();
            if (!horseName.isEmpty()) {
                Color horseColor = JColorChooser.showDialog(null, "Choose Horse Color", Color.BLACK);
                if (horseColor != null) {
                    Horse newHorse = new Horse(horseName, horseColor);
                    horses.add(newHorse);
                    nameField.setText(""); 
                    refreshHorseList();
                } else {
                    JOptionPane.showMessageDialog(null, "No color selected, using default black.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    Horse newHorse = new Horse(horseName, Color.BLACK); 
                    horses.add(newHorse);
                    nameField.setText("");
                    refreshHorseList(); 
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a horse name.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        inputPanel.add(new JLabel("Horse Name:"));
        inputPanel.add(nameField);
        inputPanel.add(addHorse);

        customiseHorsesPanel.add(inputPanel, BorderLayout.CENTER);
        customiseHorsesPanel.add(createHorseListPanel(), BorderLayout.EAST);

        return customiseHorsesPanel;
    }

    private static JPanel createHorseListPanel()
    {
        JPanel horseListPanel = new JPanel(new BorderLayout());
        horseTableModel = new DefaultTableModel(new String[]{"Horse Name", "Color"}, 0);
        JTable horseTable = new JTable(horseTableModel);
        horseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        horseListPanel.add(new JScrollPane(horseTable), BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete Horse");
        deleteButton.addActionListener(e -> deleteSelectedHorse(horseTable));
        horseListPanel.add(deleteButton, BorderLayout.SOUTH);

        return horseListPanel;
    }

    private static void deleteSelectedHorse(JTable horseTable)
    {
        int selectedRow = horseTable.getSelectedRow();
        if (selectedRow != -1) {
            horses.remove(selectedRow);
            refreshHorseList();
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a horse to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void refreshHorseList()
    {
        horseTableModel.setRowCount(0);
        for (Horse horse : horses) {
            horseTableModel.addRow(new Object[]{horse.getName(), getColorString(horse.getColor())});
        }
    }

    private static String getColorString (Color color)
    {
        return "(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
    }

    /*
     * RACE
     */
    private static JPanel createRacePanel()
    {
        JPanel racePanel = new JPanel(new BorderLayout());
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        JLabel title = new JLabel("Race", JLabel.CENTER);
        title.setFont(new Font("Helvetica", Font.BOLD, 15));
        titlePanel.add(title, BorderLayout.CENTER);
        racePanel.add(titlePanel, BorderLayout.NORTH);

        JPanel selectionPanel = new JPanel(new GridLayout(0, 1));
        selectionPanel.add(new JLabel("Select Horse 1:"));
        selectionPanel.add(horse1ComboBox);
        selectionPanel.add(new JLabel("Select Horse 2:"));
        selectionPanel.add(horse2ComboBox);
        selectionPanel.add(new JLabel("Select Horse 3:"));
        selectionPanel.add(horse3ComboBox);
        selectionPanel.add(new JLabel("Select Track:"));
        selectionPanel.add(trackComboBox);

        JButton startRaceButton = new JButton("Start Race");
        startRaceButton.addActionListener(e -> {
            int trackIndex = trackComboBox.getSelectedIndex();
            if (trackIndex >= 0 && horse1ComboBox.getSelectedIndex() >= 0 && horse2ComboBox.getSelectedIndex() >= 0 && horse3ComboBox.getSelectedIndex() >= 0) {
                Track selectedTrack = tracks.get(trackIndex);
                ArrayList<Horse> selectedHorses = new ArrayList<>();
                selectedHorses.add(horses.get(horse1ComboBox.getSelectedIndex()));
                selectedHorses.add(horses.get(horse2ComboBox.getSelectedIndex()));
                selectedHorses.add(horses.get(horse3ComboBox.getSelectedIndex()));

                JFrame raceWindow = new JFrame("Race");
                raceWindow.setSize(800, 600);
                JPanel trackPanel = new JPanel();
                trackPanel.setBackground(Color.LIGHT_GRAY);
                raceWindow.add(trackPanel);
                raceWindow.setVisible(true);

                Race race = new Race(selectedTrack.getLength());
                for (Horse horse : selectedHorses) {
                    race.addHorse(horse);
                }
                race.startRace(trackPanel);
            } else {
                JOptionPane.showMessageDialog(null, "Please select valid options for all fields.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        racePanel.add(selectionPanel, BorderLayout.CENTER);
        racePanel.add(startRaceButton, BorderLayout.SOUTH);

        return racePanel;
    }

    private static void updateComboBoxes()
    {
        horse1ComboBox.removeAllItems();
        horse2ComboBox.removeAllItems();
        horse3ComboBox.removeAllItems();
        trackComboBox.removeAllItems();

        for (Horse horse : horses) {
            String horseName = horse.getName();
            horse1ComboBox.addItem(horseName);
            horse2ComboBox.addItem(horseName);
            horse3ComboBox.addItem(horseName);
        }
        for (Track track : tracks) {
            trackComboBox.addItem(track.getName());
        }
        if (!horses.isEmpty()) {
            horse1ComboBox.setSelectedIndex(0);
            horse2ComboBox.setSelectedIndex(0);
            horse3ComboBox.setSelectedIndex(0);
        }
        if (!tracks.isEmpty()) {
            trackComboBox.setSelectedIndex(0);
        }
    }

    /*
     * STATISTICS
     */
    private static JPanel createStatisticsPanel()
    {
        JPanel statisticsPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Statistics", JLabel.CENTER);
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));

        String[] columnNames = {"Horse Name", "Races Participated", "Races Won", "Confidence Level"};
        DefaultTableModel statsTableModel = new DefaultTableModel(columnNames, 0);
        JTable statsTable = new JTable(statsTableModel);
        statsTable.setEnabled(false);  // Disable editing of the table


        JScrollPane scrollPane = new JScrollPane(statsTable);
        statisticsPanel.add(titleLabel, BorderLayout.NORTH);
        statisticsPanel.add(scrollPane, BorderLayout.CENTER);
        refreshStatistics(statsTableModel); // Make sure to populate initially
        return statisticsPanel;
    }

    public static void refreshStatistics(DefaultTableModel model)
    {
        model.setRowCount(0);
        System.out.println("Refreshing statistics");
        for (Horse horse : horses) {
            model.addRow(new Object[]{
                horse.getName(),
                horse.getRaces(),
                horse.getWins(),
                String.format("%.2f", horse.getConfidence())
            });
        }
    }

    /*
     * CASINO
     */
    private static JPanel createCasinoPanel()
    {
        JPanel casinoPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Casino", JLabel.CENTER);
        title.setFont(new Font("Helvetica", Font.BOLD, 20));
        title.setBorder(new EmptyBorder(10, 0, 10, 0));

        updateCasinoHorseList(horseSelectionComboBox);

        JTextField betAmountField = new JTextField(10);
        betAmountField.setHorizontalAlignment(JTextField.RIGHT);

        JButton placeBetButton = new JButton("Place Bet");
        JLabel resultLabel = new JLabel("Place your bet!", JLabel.CENTER);

        placeBetButton.addActionListener(e -> {
            int selectedIndex = horseSelectionComboBox.getSelectedIndex();
            if (selectedIndex != -1) {
                double betAmount = 0;
                try {
                    betAmount = Double.parseDouble(betAmountField.getText());
                    double winnings = Race.placeBet(horses.get(selectedIndex), betAmount);
                    if (winnings > 0) {
                        resultLabel.setText("You won: $" + String.format("%.2f", winnings));
                    } else {
                        resultLabel.setText("You lost your bet!");
                    }
                } catch (NumberFormatException ex) {
                    resultLabel.setText("Invalid bet amount!");
                }
            } else {
                resultLabel.setText("Please select a horse!");
            }
        });

        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("Select a Horse:"));
        formPanel.add(horseSelectionComboBox);
        formPanel.add(new JLabel("Bet Amount:"));
        formPanel.add(betAmountField);
        formPanel.add(placeBetButton);

        casinoPanel.add(title, BorderLayout.NORTH);
        casinoPanel.add(formPanel, BorderLayout.CENTER);
        casinoPanel.add(resultLabel, BorderLayout.SOUTH);

        return casinoPanel;
    }

    private static void updateCasinoHorseList(JComboBox<String> comboBox)
    {
        comboBox.removeAllItems();
        for (Horse horse : horses) {
            comboBox.addItem(horse.getName());
        }
    }

}
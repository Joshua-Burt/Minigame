package net.publicvoid.Minigame.src;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Minigame extends JFrame {
    private MainMenu mainMenu;
    private JFrame frame;

    public Minigame() {
        log("Creating Frame...");
        setSize(1024 + (1024 - 1008),550 + (550 - 511));
        setLayout(new GridBagLayout());

        frame = this;

        log("Creating MainMenu...");
        //Passes a reference to "this" to allow it to close the frame when the quit button is pressed
        mainMenu = new MainMenu(this);
        add(mainMenu, new GridBagConstraints());

        addListener();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        //Set the look and feel to be the user's system's default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        log("Game starting...");
        Minigame minigame = new Minigame();
        log("Game started successfully.");

        minigame.setTitle("Minigame");

        final long duration = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime);
        log("Started in " + duration + " second(s)");
    }

    private void addListener() {
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Component comp = frame.getContentPane().getComponent(0);

                //Checks if the current panel in the frame is the game, and changes the question that is asked depending
                // on what it is
                if (comp instanceof GamePlayer) {
                    if (JOptionPane.showConfirmDialog(frame,
                            "Do you want to save your game?", "Save your game?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        try {
                            mainMenu.gamePlayer.save();
                            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                            System.exit(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                        System.exit(0);
                    }
                } else {
                    if (JOptionPane.showConfirmDialog(frame,
                            "Do you want to quit your game?", "Quit the game?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                        System.exit(0);
                    }
                    else {
                        frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                    }
                }
            }
        });
    }

    private static void log(String input) {
        Date now = new Date();
        //"substring(4,19)" includes only the current date and time and ignores the rest of the data (day of week, year, etc.)
        System.out.println("[" + now.toString().substring(4, 19) + "] " + input);
    }
}

package net.publicvoid.Minigame.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

class MainMenu extends JPanel {
    private JButton play, create, options, quit;
    private JFrame frame;
    private JTextField worldTextBox;
    GamePlayer gamePlayer;

    MainMenu(JFrame frame) {
        this.frame = frame;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        ButtonListener listener = new ButtonListener();

        worldTextBox = new JTextField();
        play = new JButton("Play");
        create = new JButton("Create World");
        options = new JButton("Options");
        quit = new JButton("Quit");

        play.addActionListener(listener);
        create.addActionListener(listener);
        options.addActionListener(listener);
        quit.addActionListener(listener);

        add(worldTextBox);
        add(play);
        add(create);
        add(options);
        add(quit);
    }

    public class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();

            String worldName;
            if (src == play) {
                if (!worldTextBox.getText().equals("")) {
                    worldName = worldTextBox.getText() + ".wrld";

                    URL url = getClass().getResource("/net/publicvoid/Minigame/worlds");
                    File[] worldList = new File(url.getPath()).listFiles();

                    assert worldList != null;
                    for (int i = 0; i < worldList.length; i++) {
                        File file = worldList[i];
                        if (worldName.equals(file.getName())) {
                            log("Loading " + worldName + "...");

                            try {
                                gamePlayer = new GamePlayer(worldName, false);

                                frame.getContentPane().removeAll();
                                frame.getContentPane().setLayout(new BorderLayout());
                                frame.getContentPane().add(gamePlayer, BorderLayout.CENTER);
                                frame.revalidate();
                                frame.repaint();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        } else if(i == worldList.length - 1) {
                                log("Can't find world!");
                        }
                    }
                }
            } else if (src == create) {
                if (!worldTextBox.getText().equals("")) {
                    worldName = worldTextBox.getText() + ".wrld";

                    URL url = getClass().getResource("/net/publicvoid/Minigame/worlds");
                    File[] worldList = new File(url.getPath()).listFiles();


                    boolean worldExists = false;
                    assert worldList != null;
                    for(File file : worldList) {
                        if (worldName.equals(file.getName())) {
                            worldExists = true;
                        }
                    }
                    if (!worldExists) {
                        try {
                            gamePlayer = new GamePlayer(worldName, true);

                            frame.getContentPane().removeAll();
                            frame.getContentPane().setLayout(new BorderLayout());
                            frame.getContentPane().add(gamePlayer, BorderLayout.CENTER);
                            frame.revalidate();
                            frame.repaint();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                      log("World Already Exists!");
                    }
                }
            } else if (src == options) {
                OptionMenu optMenu = new OptionMenu(frame);
                frame.getContentPane().removeAll();
                frame.getContentPane().add(optMenu, new GridBagConstraints());
                frame.revalidate();
                frame.repaint();
            } else if (src == quit) {
                frame.setVisible(false); //You can't see me!
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); //Destroy the JFrame object
            }
        }
    }
    private static void log(String input) {
        Date now = new Date();
        //"substring(4,19)" includes only the current date and time and ignores the rest of the data (day of week, year, etc.)
        System.out.println("[" + now.toString().substring(4, 19) + "] " + input);
    }
}

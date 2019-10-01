package net.publicvoid.Minigame.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class OptionMenu extends JPanel {
    private JButton fullscreen, back;
    private String fullscreenState;
    private JFrame frame;

    OptionMenu(JFrame frame) {
        this.frame = frame;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        OptionButtonListener listener = new OptionButtonListener();

        fullscreenState = "Off";
        fullscreen = new JButton("Fullscreen: " + fullscreenState);
        back = new JButton("Main Menu");

        fullscreen.addActionListener(listener);
        back.addActionListener(listener);

        add(fullscreen);
        add(back);
    }

    public class OptionButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton)e.getSource();

            if(src == fullscreen) {
                switch (fullscreenState) {
                    case "On":
                        fullscreenState = "Borderless";
                        frame.setResizable(false);
                        break;
                    case "Borderless":
                        fullscreenState = "Off";
                        frame.setResizable(true);
                        frame.setExtendedState(JFrame.NORMAL);
                        break;
                    case "Off":
                        fullscreenState = "On";
                        frame.setResizable(false);
                        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        break;
                }
                fullscreen.setText("Fullscreen: " + fullscreenState);
            }
            else if(src == back) {
                MainMenu mainMenu = new MainMenu(frame);
                frame.getContentPane().removeAll();
                frame.getContentPane().add(mainMenu, new GridBagConstraints());
                frame.revalidate();
                frame.repaint();
            }
        }
    }
}

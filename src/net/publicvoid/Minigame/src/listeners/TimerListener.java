package net.publicvoid.Minigame.src.listeners;

import net.publicvoid.Minigame.src.GamePlayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class TimerListener implements ActionListener {
    private GamePlayer gp;
    public TimerListener(GamePlayer gp) {
        this.gp = gp;
    }

    public void actionPerformed(ActionEvent event) {
        gp.requestFocusInWindow();

        if(gp.getPlayer() != null) {
        try {
            gp.calcSurroundingBlocks(gp.getPlayer());
            gp.useSurroundingBlocks();
        } catch (IOException e) {
            e.printStackTrace();
        }

            gp.movement();
            gp.highlight();
            gp.setView();
            gp.repaint();
        }
    }
}

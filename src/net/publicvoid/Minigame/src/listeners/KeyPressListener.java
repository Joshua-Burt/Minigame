package net.publicvoid.Minigame.src.listeners;

import net.publicvoid.Minigame.src.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyPressListener implements KeyListener {
    private boolean leftMoving, rightMoving, jumping, isInvOpen;
    private Player player;

    public KeyPressListener(Player player) {
        this.player = player;
        this.jumping = false;
        this.leftMoving = false;
        this.rightMoving = false;
    }

    public boolean getLeft() {
        return leftMoving;
    }

    public boolean getRight() {
        return rightMoving;
    }

    public boolean getJumping() {
        return jumping;
    }

    public boolean getIsInvOpen() {
        return isInvOpen;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
            case KeyEvent.VK_SPACE:
                jumping = true;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                //TODO: Implement feature for pressing down?
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftMoving = true;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightMoving = true;
                break;
            case KeyEvent.VK_E:
                isInvOpen = !isInvOpen;
                break;
        }
        //Hot bar selection
        switch(keyCode) {
            case KeyEvent.VK_1:
                player.getInventory().setSelectedSlotItem(0);
                break;
            case KeyEvent.VK_2:
                player.getInventory().setSelectedSlotItem(1);
                break;
            case KeyEvent.VK_3:
                player.getInventory().setSelectedSlotItem(2);
                break;
            case KeyEvent.VK_4:
                player.getInventory().setSelectedSlotItem(3);
                break;
            case KeyEvent.VK_5:
                player.getInventory().setSelectedSlotItem(4);
                break;
            case KeyEvent.VK_6:
                player.getInventory().setSelectedSlotItem(5);
                break;
            case KeyEvent.VK_7:
                player.getInventory().setSelectedSlotItem(6);
                break;
            case KeyEvent.VK_8:
                player.getInventory().setSelectedSlotItem(7);
                break;
            case KeyEvent.VK_9:
                player.getInventory().setSelectedSlotItem(8);
                break;
            case KeyEvent.VK_0:
                player.getInventory().setSelectedSlotItem(9);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch(keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
            case KeyEvent.VK_SPACE:
                jumping = false;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftMoving = false;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                rightMoving = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //No code necessary
    }
}
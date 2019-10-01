package net.publicvoid.Minigame.src.listeners;

import net.publicvoid.Minigame.src.GamePlayer;
import net.publicvoid.Minigame.src.Player;
import net.publicvoid.Minigame.src.inventory.tools.Pickaxe;
import net.publicvoid.Minigame.src.world.Block;
import net.publicvoid.Minigame.src.world.World;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

public class MousePressListener implements MouseListener {
    private Player player;
    private GamePlayer gp;
    private int blockSize;
    private World world;

    public MousePressListener(GamePlayer gp, int blockSize, Player player, World world) {
        this.gp = gp;
        this.blockSize = blockSize;
        this.player = player;
        this.world = world;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        double shortestDist = 100000;
        Block shortestBlock = null;

        //TODO: Play sound when block breaks

        foundClosestBlock:
        for (ArrayList<Block> visibleBlockRow : gp.getVisibleBlockRows()) {
            for (Block block : visibleBlockRow) {
                //Location of where the cursor was clicked
                int cursorX = e.getPoint().x;
                int cursorY = e.getPoint().y;

                //Center point of the block
                int blockX = (block.getScreenPos().x) + (blockSize / 2);
                int blockY = (block.getScreenPos().y) + (blockSize / 2);

                //Get the distance between the two above points
                double workingDist = distance(cursorX, cursorY, blockX, blockY);

                //Compare the found distance, and if it is shorter than any previous ones, that is the shortest
                if (workingDist < shortestDist) {
                    shortestDist = workingDist;
                    shortestBlock = block;
                    if(shortestDist < blockSize >> 1) {
                        break foundClosestBlock;
                    }
                }
            }
        }
        //Distance between the character and where the mouse was clicked
        double playerToClick = distance(player.getScreenPosX(), player.getScreenPosY(), e.getX(), e.getY());

        //If the mouse is inside of the player's reach
        if (playerToClick <= player.getReach()) {
            //Right click
            if (SwingUtilities.isRightMouseButton(e)) {
                //Set the given block in the position of the block being hovered over
                assert shortestBlock != null;

                //Makes it so you can't place blocks where blocks already are and not where the player is standing
                if(shortestBlock.getType().equals("air") &&
                        ((player.getXPos() < shortestBlock.getPos().x || player.getXPos() > shortestBlock.getPos().x + 1)
                                || ((player.getYPos() < shortestBlock.getPos().y || player.getYPos() > shortestBlock.getPos().y + 1)
                                && (player.getYPos() - 1 < shortestBlock.getPos().y || player.getYPos() - 1 > shortestBlock.getPos().y + 1)))) {
                    try {
                        if (player.getInventory().getSelectedSlotItem() instanceof Block) {

                            //Check all surrounding blocks to make sure that there is a connecting block next to it
                            Block aboveBlock, belowBlock, rightBlock, leftBlock;
                            aboveBlock = world.getBlock((int)shortestBlock.getPos().getX(), (int)shortestBlock.getPos().getY() + 1);
                            belowBlock = world.getBlock((int)shortestBlock.getPos().getX(), (int)shortestBlock.getPos().getY() - 1);
                            rightBlock = world.getBlock((int)shortestBlock.getPos().getX() + 1, (int)shortestBlock.getPos().getY());
                            leftBlock = world.getBlock((int)shortestBlock.getPos().getX() - 1, (int)shortestBlock.getPos().getY());

                            if(!aboveBlock.getType().equals("air") || !belowBlock.getType().equals("air") || !rightBlock.getType().equals("air") || !leftBlock.getType().equals("air")) {
                                int index = -1;

                                for (int i = 0; i < 10; i++) {
                                    if (player.getInventory().get(i).getItem() == player.getInventory().getSelectedSlotItem()) {
                                        index = i;
                                        break;
                                    }
                                }

                                if (index >= 0) {
                                    if (player.getInventory().get(index).getNumHeld() > 0) {
                                        //When a block is placed, reduce the held number by 1
                                        world.setBlock(shortestBlock.getPos(), ((Block) player.getInventory().getSelectedSlotItem()).getID());
                                        player.getInventory().get(index).setNumHeld(player.getInventory().get(index).getNumHeld() - 1);

                                        //The last block in a inventory slot is placed, change it to null
                                        if (player.getInventory().get(index).getNumHeld() <= 0) {
                                            player.getInventory().get(index).setItem(null);
                                        }
                                    } else if (player.getInventory().get(index).getNumHeld() <= 0) {
                                        player.getInventory().get(index).setItem(null);
                                    }
                                }
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                //Left click
            } else if (SwingUtilities.isLeftMouseButton(e)) {
                assert shortestBlock != null;
                try {
                    //If the selected item is a pickaxe
                    if(player.getInventory().getSelectedSlotItem() instanceof Pickaxe) {
                        if(shortestBlock.getHardness() <= ((Pickaxe) player.getInventory().getSelectedSlotItem()).getHardness()) {
                            int index = -1;

                            //First search if there is a slot that already has the specific block then if none are found put it in a empty slot
                            for(int i = 0; i < player.getInventorySpace(); i++) {
                                if(player.getInventory().get(i).getItem() instanceof Block){
                                    if(((Block) player.getInventory().get(i).getItem()).getID() == shortestBlock.getID() && player.getInventory().get(i).getNumHeld() < 64) {
                                        index = i;
                                        break;
                                    }
                                }
                            }

                            if(index < 0) {
                                for(int i = 0; i < player.getInventorySpace(); i++) {
                                    if(player.getInventory().get(i).getItem() == null) {
                                        index = i;
                                        break;
                                    }
                                }
                            }

                            if(player.getInventory().get(index).getItem() == null) {
                                player.getInventory().addItem(index, shortestBlock);
                                player.getInventory().get(index).setNumHeld(1);
                            } else {
                                player.getInventory().get(index).setNumHeld(player.getInventory().get(index).getNumHeld() + 1);
                            }
                            //Set the block to air in the position being hovered over
                            world.setBlock(shortestBlock.getPos(), 0);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //No code necessary
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //No code necessary
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //No code necessary
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //No code necessary
    }

    //Method to find the distance between two points
    private double distance(double x1, double y1, int x2, int y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }
}

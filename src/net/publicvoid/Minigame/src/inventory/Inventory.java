package net.publicvoid.Minigame.src.inventory;

import net.publicvoid.Minigame.src.Player;
import net.publicvoid.Minigame.src.inventory.tools.Pickaxe;
import net.publicvoid.Minigame.src.inventory.tools.Sword;
import net.publicvoid.Minigame.src.inventory.tools.ToolTypes;
import net.publicvoid.Minigame.src.world.Block;
import net.publicvoid.Minigame.src.world.BlockTypes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Inventory extends ArrayList<InventorySlot> {
    private Object selectedSlotItem; //Currently selected item
    private int selectedSlotNum;
    private int invSpace; //Number of slots in inventory
    private Player player;

    public Inventory(Player player) {
        this.invSpace = player.getInventorySpace();
        this.player = player;

        createInventorySlots();
    }

    private void createInventorySlots() {
        for (int i = 0; i < invSpace; i++) {
            InventorySlot slot = new InventorySlot(null);
            this.add(slot);
        }
    }

    public void addItem(int slot, Object obj) {
        this.get(slot).setItem(obj);
    }

    public Object getItem(int slot) {

        return this.get(slot);
    }

    public void removeItem(int pos) {
        this.remove(pos);
    }

    public void setSelectedSlotItem(int slot) {
        selectedSlotItem = this.get(slot).getItem();
        setSelectedSlotNum(slot);
    }

    public Object getSelectedSlotItem() {
        return selectedSlotItem;
    }

    private String getCD() {
        Path currentRelativePath = Paths.get("");

        return currentRelativePath.toAbsolutePath().toString();
    }

    //When the player quits, this will save the inventory for next time
    public void saveInventory(String worldName, ToolTypes toolTypes, BlockTypes blockTypes) throws IOException {
        worldName = worldName.substring(0, worldName.length() - 5);

        File playerFile = new File(getCD() + "/src/net/publicvoid/Minigame/worlds/players/" + worldName + "/player.pi");
        FileWriter fileWriter = new FileWriter(playerFile);

        Inventory inv = player.getInventory();
        StringBuilder together = new StringBuilder();

        for (int i = 0; i < player.getInventorySpace(); i++) {
            if (inv.get(i).getItem() instanceof Pickaxe) {
                String firstDigit = "P";
                String secondDigit = String.valueOf(((Pickaxe) inv.get(i).getItem()).getID());
                together.append(firstDigit).append(secondDigit);
            } else if (inv.get(i).getItem() instanceof Sword) {
                String firstDigit = "S";
                String secondDigit = String.valueOf(((Sword) inv.get(i).getItem()).getID());
                together.append(firstDigit).append(secondDigit);
            } else if (inv.get(i).getItem() instanceof Block) {
                String heldItemDigit = String.valueOf(inv.get(i).getNumHeld());
                String secondDigit = "B";
                String thirdDigit = String.valueOf(((Block) inv.get(i).getItem()).getID());
                together.append(heldItemDigit).append(secondDigit).append(thirdDigit);
            } else if (inv.get(i).getItem() == null) {
                together.append("00");
            }

            if (i < player.getInventorySpace() - 1) {
                together.append(",");
            }
        }

        fileWriter.write(together.toString());
        fileWriter.close();
    }

    //Loads in the players inventory from the player.pi file
    public void retrieveInventory(String worldName, ToolTypes toolTypes, BlockTypes blockTypes) throws IOException {
        //Truncates the ".wrld" part of the world name
        worldName = worldName.substring(0, worldName.length() - 5);

        //Create an array of all the different types of Pickaxes to reference from
        File playerFile = new File(getCD() + "/src/net/publicvoid/Minigame/worlds/players/" + worldName + "/player.pi");

        //This creates the file if the player does not have one
        if(!playerFile.exists()) {
            if(new File(getCD() + "/src/net/publicvoid/Minigame/worlds/players/" + worldName).mkdir()) {
                FileWriter fileWriter = new FileWriter(playerFile);

                fileWriter.write("P0,");
                fileWriter.write("S0,");

                for (int i = 2; i < player.getInventorySpace(); i++) {
                    fileWriter.write("00");

                    if (i < player.getInventorySpace() - 1) {
                        fileWriter.write(",");
                    }
                }
                fileWriter.close();
            }
        }

        //After the file has been created, read the file into the game
        Scanner in = new Scanner(playerFile);

        String[] inv = in.next().split(",");

        for (int i = 0; i < inv.length; i++) {
            switch (inv[i].substring(0, 1)) {
                case "P":
                    player.getInventory().addItem(i, toolTypes.getPickaxeTypes().get(Integer.parseInt(inv[i].substring(1, 2))));
                    break;
                case "S":
                    player.getInventory().addItem(i, toolTypes.getSwordTypes().get(Integer.parseInt(inv[i].substring(1, 2))));
                    break;
                case "B":
                    player.getInventory().addItem(i, blockTypes.getBlock(Integer.parseInt(inv[i].substring(1, 2))));
                    break;
                default:
                    if (inv[i].equals("00")) {
                        player.getInventory().addItem(i, null);
                    } else if (inv[i].substring(1, 2).equals("B")) {
                        player.getInventory().addItem(i, blockTypes.getBlock(Integer.parseInt(inv[i].substring(2, 3))));
                        player.getInventory().get(i).setNumHeld(Integer.parseInt(inv[i].substring(0, 1)));
                    } else if (inv[i].substring(2, 3).equals("B")) {
                        player.getInventory().addItem(i, blockTypes.getBlock(Integer.parseInt(inv[i].substring(3, 4))));
                        player.getInventory().get(i).setNumHeld(Integer.parseInt(inv[i].substring(0, 2)));
                    }
            }
        }
        in.close();
    }



    public int getSelectedSlotNum() {
        return selectedSlotNum;
    }

    private void setSelectedSlotNum(int selectedSlotNum) {
        this.selectedSlotNum = selectedSlotNum;
    }
}

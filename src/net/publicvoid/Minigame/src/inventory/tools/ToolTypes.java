package net.publicvoid.Minigame.src.inventory.tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class ToolTypes {
    private ArrayList<Pickaxe> pickaxeTypes;
    private ArrayList<Sword> swordTypes;
    private Scanner in;

    public ToolTypes() {
        pickaxeTypes = new ArrayList<>();
        swordTypes = new ArrayList<>();

        try {
            readPickFile();
            readSwordFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readPickFile() throws IOException {
        //Create an array of all the different types of Pickaxes to reference from
        File pickaxeTypeFile = new File(getCD() + "/src/net/publicvoid/Minigame/src/inventory/tools/files/Pickaxes.pvs");

        in = new Scanner(pickaxeTypeFile);
        in.useDelimiter(",|\\r\\n");

        //Each pick type is read from the Pickaxes.pvs file and is added to a Pickaxe object and added to an array
        while (in.hasNext()) {
            int ID = in.nextInt();
            String type = in.next();
            int damage = in.nextInt();
            int hardness = in.nextInt();
            int durability = in.nextInt();

            BufferedImage sprite = ImageIO.read(new File(getCD() + "/src/net/publicvoid/Minigame/sprites/tools", type + ".png"));

            Pickaxe pick = new Pickaxe(ID, type, damage, hardness, durability, sprite);
            pickaxeTypes.add(pick);
        }
    }

    private void readSwordFile() throws IOException {
        //Create an array of all the different types of Swords to reference from
        File swordTypeFile = new File(getCD() + "/src/net/publicvoid/Minigame/src/inventory/tools/files/Swords.pvs");

        in = new Scanner(swordTypeFile);
        in.useDelimiter(",|\\r\\n");

        //Each sword type is read from the Swords.pvs file and is added to a Sword object and added to an array
        while (in.hasNext()) {

            int ID = in.nextInt();
            String type = in.next();
            int damage = in.nextInt();
            int durability = in.nextInt();

            BufferedImage sprite = ImageIO.read(new File(getCD() + "/src/net/publicvoid/Minigame/sprites/tools", type + ".png"));

            Sword sword = new Sword(ID, type, damage, durability, sprite);
            swordTypes.add(sword);
        }
    }

    public ArrayList<Sword> getSwordTypes() {
        return swordTypes;
    }

    public ArrayList<Pickaxe> getPickaxeTypes() {
        return pickaxeTypes;
    }


    private String getCD() {
        Path currentRelativePath = Paths.get("");

        return currentRelativePath.toAbsolutePath().toString();
    }
}
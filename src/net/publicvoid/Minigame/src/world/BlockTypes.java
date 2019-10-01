package net.publicvoid.Minigame.src.world;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class BlockTypes {
    private ArrayList<Block> blockTypes;
    private Scanner in;

    public BlockTypes() throws IOException {
        //Create an array of all the different types of blocks to reference from
        File blockTypeFile = new File(getCD() + "/src/net/publicvoid/Minigame/src/world/Blocks.pvs");
        blockTypes = new ArrayList<>();

        in = new Scanner(blockTypeFile);
        in.useDelimiter(",|\\r\\n");

        //Each block type is read from the blocks.pvs file and is added to a Block object and added to an array
        while(in.hasNext()) {
            int ID = in.nextInt();
            String type = in.next();
            int hardness = in.nextInt();

            Block block;

            if(!type.equals("door")) {
                BufferedImage sprite = ImageIO.read(new File(getCD() + "/src/net/publicvoid/Minigame/sprites/blocks", type + ".png"));
                block = new Block(ID, type, sprite, null, hardness);
            }
            else {
                BufferedImage sprite = ImageIO.read(new File(getCD() + "/src/net/publicvoid/Minigame/sprites/blocks/door_closed_1.png"));
                BufferedImage topSprite = ImageIO.read(new File(getCD() + "/src/net/publicvoid/Minigame/sprites/blocks/door_closed_2.png"));
                block = new Block(ID, type, sprite, topSprite, null, hardness);
            }


            blockTypes.add(block);
        }
    }

    public Block getBlock(int ID) {
        Block templateBlock = blockTypes.get(ID);

        //Create a new block so it does not just pass the array's block by reference
        return new Block(templateBlock.getID(), templateBlock.getType(), templateBlock.getSprite(), null, templateBlock.getHardness());
    }

    private String getCD() {
        Path currentRelativePath = Paths.get("");

        return currentRelativePath.toAbsolutePath().toString();
    }
}

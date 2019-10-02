package net.publicvoid.Minigame.src.world;

import net.publicvoid.Minigame.src.world.worldgen.Generator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class World {
    private String worldName;
    private BlockTypes blockTypes;
    private ArrayList<ArrayList<Block>> blockRows;
    private File worldPath;

    public World(String worldName, BlockTypes blockTypes, boolean isNewWorld) throws IOException {
        this.worldName = worldName;
        this.blockTypes = blockTypes;

        blockRows = new ArrayList<>();

        if (isNewWorld) {
            Generator gen = new Generator(worldName, blockTypes);

            log("Creating World " + worldName + "...");
            gen.buildNewWorld();
            log(worldName + " Created Successfully.");

            log("Preparing World " + worldName + "...");
            createWorld();
            log(worldName + " Loaded Successfully.");
        } else {
            log("Preparing World " + worldName + "...");
            createWorld();
            log(worldName + " Loaded Successfully.");
        }
    }

    private void createWorld() throws FileNotFoundException {
        worldPath = new File(getCD() + "/src/net/publicvoid/Minigame/worlds/", worldName);

        Scanner worldBuilder = new Scanner(worldPath);
        worldBuilder.useDelimiter(",|\\r\\n");

        //TODO: Change to read the size of the world, such as reading the whole line at once, then finding out how many blocks there
        // are based on that.
        for(int i = 0; i < 128; i++) {
            blockRows.add(new ArrayList<>());

            for (int o = 0; o < 1280; o++) {
                int ID = worldBuilder.nextInt();
                Block block = null;

                if (ID != 7) {
                    Block tempBlock = blockTypes.getBlock(ID);

                    block = new Block(ID, tempBlock.getType(), tempBlock.getSprite(), new Point(o, i), tempBlock.getHardness());
                }
                else {
                    Block tempBlock = blockTypes.getBlock(ID);
                    block = new Block(ID, tempBlock.getType(), tempBlock.getSprite(), tempBlock.getTopSprite(), new Point(o, i), tempBlock.getHardness());
                }

                //Set the location in the world
                blockRows.get(i).add(block);
            }
        }
    }

    public Block getBlock(int posX, int posY) {
        return blockRows.get(posY).get(posX);
    }

    public int getSizeX() {
        return blockRows.get(0).size();
    }

    public int getSizeY() {
        return blockRows.size();
    }

    public void setBlock(Point pos, int ID) throws IOException {
        //Template Block
        Block tempBlock = blockTypes.getBlock(ID);

        BufferedImage sprite = tempBlock.getSprite();
        Block block = new Block(ID, tempBlock.getType(), sprite, pos, tempBlock.getHardness());
        blockRows.get(pos.y).set(pos.x, block);
    }

    public void saveWorld() throws IOException {
        FileWriter fileWriter = new FileWriter(worldPath);

        for(int i = 0; i < getSizeY(); i++) {
            for(int o = 0; o < getSizeX(); o++) {
                Block blk = getBlock(o,i);

                String ID = String.valueOf(blk.getID());

                fileWriter.write(ID);

                if(o < getSizeX() - 1) {
                    fileWriter.write(",");
                } else {
                    fileWriter.write(System.getProperty("line.separator"));
                }
            }
        }
        fileWriter.close();
    }

    public ArrayList<ArrayList<Block>> getBlockRows() {
        return blockRows;
    }

    private String getCD() {
        Path currentRelativePath = Paths.get("");

        return currentRelativePath.toAbsolutePath().toString();
    }

    private static void log(String input) {
        Date now = new Date();
        //"substring(4,19)" includes only the current date and time and ignores the rest of the data (day of week, year, etc.)
        System.out.println("[" + now.toString().substring(4, 19) + "] " + input);
    }
}

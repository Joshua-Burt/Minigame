package net.publicvoid.Minigame.src.world.worldgen;

import net.publicvoid.Minigame.src.world.BlockTypes;
import net.publicvoid.Minigame.src.world.World;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Random;

public class Generator {
    private String worldName;
    private StructureTypes structureTypes;
    private BlockTypes blockTypes;
    private World worldBuild;

    public Generator(String worldName, BlockTypes blockTypes) throws IOException {
        this.worldName = worldName;
        this.blockTypes = blockTypes;

        log("Loading StructureTypes...");
        structureTypes = new StructureTypes(blockTypes);
    }

    public void buildNewWorld() throws IOException {
        worldBuild = new World("default.wd", blockTypes, false);

        for (int i = 0; i < 128; i++) {
            for (int o = 0; o < 1280; o++) {
                if (o < 1280 - 3 && o > 2) {
                    if (worldBuild.getBlock(o, i).getID() == (2)) {
                        Random rnd = new Random();

                        //Trees have a 10% chance to spawn on any grass block
                        int chance = rnd.nextInt(10);
                        if (chance == 0) {
                            placeStructure(structureTypes.get(0), o, i);
                        }
                    }

                    //Generating ore
                    if (worldBuild.getBlock(o, i).getID() == (3)) {
                        Random rnd = new Random();

                        //Checks surrounding blocks
                        if (o < worldBuild.getSizeX() - 4 && i < worldBuild.getSizeY() - 4 && o > 4 && i > 4) {
                            int top = worldBuild.getBlock(o, i - 1).getID();
                            int right = worldBuild.getBlock(o + 1, i).getID();
                            int bottom = worldBuild.getBlock(o, i + 1).getID();
                            int left = worldBuild.getBlock(o - 1, i).getID();

                            //Ore has a 45% chance to spawn if there is an adjacent ore
                            if (top == 4 || right == 4 || bottom == 4 || left == 4) {
                                int chance = rnd.nextInt(100);
                                if (chance > 65) {
                                    worldBuild.setBlock(new Point(o, i), 4);
                                }
                            } else {
                                //Ore has a 1.3% chance to spawn on its own
                                int chance = rnd.nextInt(75);
                                if (chance == 0) {
                                    worldBuild.setBlock(new Point(o, i), 4);
                                }
                            }
                        }
                    }

                    if (worldBuild.getBlock(o, i).getID() == (3)) {
                        Random rnd = new Random();

                        //Checks surrounding blocks
                        if (o < worldBuild.getSizeX() - 4 && i < worldBuild.getSizeY() - 4 && o > 4 && i > 4) {
                            int top = worldBuild.getBlock(o, i - 1).getID();
                            int right = worldBuild.getBlock(o + 1, i).getID();
                            int bottom = worldBuild.getBlock(o, i + 1).getID();
                            int left = worldBuild.getBlock(o - 1, i).getID();

                            //Ore has a 45% chance to spawn if there is an adjacent ore
                            if (top == 8 || right == 8 || bottom == 8 || left == 8) {
                                int chance = rnd.nextInt(100);
                                if (chance > 65) {
                                    worldBuild.setBlock(new Point(o, i), 8);
                                }
                            } else {
                                //Ore has a ___% chance to spawn on its own
                                int chance = rnd.nextInt(125);
                                if (chance == 0) {
                                    worldBuild.setBlock(new Point(o, i), 8);
                                }
                            }
                        }
                    }
                }
            }
        }


        StringBuilder worldStr = new StringBuilder();

        for(int i = 0; i < 128; i++) {
            for (int o = 0; o < 1280; o++) {
                worldStr.append(worldBuild.getBlock(o, i).getID());
                if (o < 1280 - 1) {
                    worldStr.append(",");
                } else {
                    worldStr.append(System.getProperty("line.separator"));
                }
            }
        }
        writeToFile(worldStr);
    }


    private void placeStructure (Structure structure,int o, int i) throws IOException {
        //Where within the world the structure is begun to be built
        int startX = o - structure.getWidth() / 2;
        int startY = i - structure.getWidth() - 1;
        Point startPoint = new Point(startX, startY);

        for (int j = 0; j < structure.getHeight(); j++) {
            for (int k = 0; k < structure.getWidth(); k++) {
                if (structure.getBlock(new Point(k, j)).getID() != 0) {
                    worldBuild.setBlock(new Point(startPoint.x + k, startPoint.y + j), structure.getBlock(new Point(k, j)).getID());
                }
            }
        }
    }

    private void writeToFile (StringBuilder str) throws IOException {
        File worldPath = new File(getCD() + "/src/net/publicvoid/Minigame/worlds/", worldName);
        Writer fileWriter = new FileWriter(worldPath);
        fileWriter.write(str.toString());
        fileWriter.close();
    }

    private String getCD () {
        Path currentRelativePath = Paths.get("");

        return currentRelativePath.toAbsolutePath().toString();
    }

    private static void log (String input){
        Date now = new Date();
        //"substring(4,19)" includes only the current date and time and ignores the rest of the data (day of week, year, etc.)
        System.out.println("[" + now.toString().substring(4, 19) + "] " + input);
    }
}

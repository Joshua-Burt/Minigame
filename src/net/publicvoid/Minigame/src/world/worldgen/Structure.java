package net.publicvoid.Minigame.src.world.worldgen;

import net.publicvoid.Minigame.src.world.Block;
import net.publicvoid.Minigame.src.world.BlockTypes;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Structure extends ArrayList<ArrayList<Block>> {
    private Scanner in;
    private String structureType;
    private Structure structure;
    private BlockTypes blockTypes;

    Structure(File file, BlockTypes blockTypes) throws FileNotFoundException {
        this.blockTypes = blockTypes;

        in = new Scanner(file);
        in.useDelimiter("\\r\\n");

        structure = this;

        structureType = in.next();
        try {
            createStructureArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        in.close();
    }

    private void createStructureArray() throws IOException {
        while (in.hasNext()) {
            String[] blocks = in.next().split(",");

            ArrayList<Block> line = new ArrayList<>();

            for (String block : blocks) {
                Block tempBlock = blockTypes.getBlock(Integer.parseInt(block));
                Block blk = new Block(Integer.parseInt(block), tempBlock.getType(), tempBlock.getSprite(), null, tempBlock.getHardness());

                line.add(blk);
            }
            structure.add(line);
        }
    }

    Block getBlock(Point pos) {
        return this.get(pos.y).get(pos.x);
    }

    int getWidth() {
        return this.get(0).size();
    }

    int getHeight() {
        return this.size();
    }

    public String getStructureType() {
        return structureType;
    }
}

package net.publicvoid.Minigame.src.world.worldgen;

import net.publicvoid.Minigame.src.world.BlockTypes;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class StructureTypes extends ArrayList<Structure> {
    private ArrayList<Structure> structureTypes;
    private BlockTypes blockTypes;

    StructureTypes(BlockTypes blockTypes) throws IOException {
        this.blockTypes = blockTypes;
        structureTypes = this;

        createStructureList();
    }

    private void createStructureList() throws IOException {
        //Get the number of files in the folder of structures
        URL url = getClass().getResource("/net/publicvoid/Minigame/src/world/worldgen/files");
        File[] structureFileList = new File(url.getPath()).listFiles();

        //This will create a Structure object using the file and will add the object to the structureType array
        assert structureFileList != null;
        for (File file : structureFileList) {
            Structure structure = new Structure(file, blockTypes);
            structureTypes.add(structure);
        }
    }

    private String getCD() {
        Path currentRelativePath = Paths.get("");

        return currentRelativePath.toAbsolutePath().toString();
    }
}


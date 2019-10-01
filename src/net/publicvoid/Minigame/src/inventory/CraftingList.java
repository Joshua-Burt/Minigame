package net.publicvoid.Minigame.src.inventory;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class CraftingList extends ArrayList<CraftingRecipe> {
    public CraftingList() throws FileNotFoundException {

        File recipeFile = new File(getCD() + "/src/net/publicvoid/Minigame/src/inventory/recipes.pvs");
        Scanner in = new Scanner(recipeFile);
        in.useDelimiter("\\r\\n");

        while(in.hasNext()) {
            String[] str = in.next().split(",");

            ArrayList<String> itemType = new ArrayList<>();
            ArrayList<String> numOfItems = new ArrayList<>();
            ArrayList<String> typeOfItems = new ArrayList<>();

            itemType.add(str[0]);

            for(int i = 1; i < str.length; i += 2) {
                numOfItems.add(str[i]);
                typeOfItems.add(str[i + 1]);
            }

            CraftingRecipe recipe = new CraftingRecipe(itemType, numOfItems, typeOfItems);
            this.add(recipe);
        }
    }

    private String getCD() {
        return Paths.get("").toAbsolutePath().toString();
    }
}

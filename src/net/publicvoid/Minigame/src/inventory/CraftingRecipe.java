package net.publicvoid.Minigame.src.inventory;

import java.util.ArrayList;

public class CraftingRecipe {
    private ArrayList<ArrayList<String>> recipe;

    CraftingRecipe(ArrayList<String> itemName, ArrayList<String> numOfItems, ArrayList<String> typeOfItems) {
        recipe = new ArrayList<>();

        recipe.add(itemName);
        recipe.add(numOfItems);
        recipe.add(typeOfItems);
    }

    public String getItemName() {
        return recipe.get(0).get(0);
    }

    public ArrayList<String> getNumOfItems() {
        return recipe.get(1);
    }

    public ArrayList<String> getTypeOfItems() {
        return recipe.get(2);
    }
}

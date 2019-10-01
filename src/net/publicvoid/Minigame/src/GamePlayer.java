package net.publicvoid.Minigame.src;

import net.publicvoid.Minigame.src.inventory.CraftingList;
import net.publicvoid.Minigame.src.inventory.CraftingRecipe;
import net.publicvoid.Minigame.src.inventory.tools.Pickaxe;
import net.publicvoid.Minigame.src.inventory.tools.Sword;
import net.publicvoid.Minigame.src.inventory.tools.ToolTypes;
import net.publicvoid.Minigame.src.listeners.KeyPressListener;
import net.publicvoid.Minigame.src.listeners.MousePressListener;
import net.publicvoid.Minigame.src.listeners.TimerListener;
import net.publicvoid.Minigame.src.world.Block;
import net.publicvoid.Minigame.src.world.BlockTypes;
import net.publicvoid.Minigame.src.world.World;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import static java.awt.MouseInfo.getPointerInfo;

public class GamePlayer extends JPanel {
    private BlockTypes blockTypes;
    private ToolTypes toolTypes;
    private CraftingList craftingList;
    private ArrayList<ArrayList<Block>> visibleBlockRows;
    private Player player;
    private boolean isCraftBench;
    private final JPanel panel;
    private double worldWidth, worldHeight;
    private final int blockSize;
    private int playerScreenX, playerScreenY, playerHeight;
    private KeyPressListener kl;
    private Block highlightedBlock;
    private World world;
    private String worldName;

    GamePlayer(String worldName, boolean isNewWorld) throws IOException {
        setFocusable(true);
        setLayout(new BorderLayout());
        setBackground(new Color(70, 119, 167));

        this.worldName = worldName;

        panel = this;

        //The number of pixels each block takes up
        blockSize = 24;

        log("Loading BlockTypes...");
        blockTypes = new BlockTypes();

        log("Loading ToolTypes...");
        toolTypes = new ToolTypes();

        log("Loading CraftingList...");
        craftingList = new CraftingList();

        //Load the world
        world = new World(worldName, blockTypes, isNewWorld);

        startTimer();

        //Get the total size of the world in number of blocks
        worldWidth = world.getSizeX();
        worldHeight = world.getSizeY();
        createPlayer(worldName);

        kl = new KeyPressListener(player);
        MousePressListener mp = new MousePressListener(this, blockSize, player, world);
        addKeyListener(kl);
        addMouseListener(mp);
    }

    //Creates the player, retrieves their inventory if needed, and chooses the spawn point
    private void createPlayer(String worldName) throws IOException {
        //TODO: Implement multiple character sprites

        File playerSprite = new File(getCD() + "/src/net/publicvoid/Minigame/sprites/characters/henry.png");

        //This searches from the top of the map to the bottom to find a block that isn't air to start the player on
        //It will start the player on the topmost block in the middle x
        int playerStartY = 0;
        for(int i = 0; i < worldHeight; i++) {
            Block block = world.getBlock((int)worldWidth / 2, i);

            if(block.getID() != 0) {
                playerStartY = i;
                break;
            }
        }

        //The player will start in the center of the world
        player = new Player(worldWidth / 2, playerStartY, playerSprite, 0.20);
        player.setReach(6 * blockSize);
        player.getInventory().retrieveInventory(worldName, toolTypes, blockTypes);

        //Set the default selected slot to the first slot
        player.getInventory().setSelectedSlotItem(0);

        playerHeight = (int) (blockSize * 1.75);
    }

    //Takes the boolean movements and translates it into method calls to the shiftWorld method
    public void movement() {
        boolean jumping = kl.getJumping();
        boolean rightMoving = kl.getRight();
        boolean leftMoving = kl.getLeft();

        //Jumping and moving right
        if(jumping && rightMoving && !leftMoving) {
            shiftWorld("up");
            shiftWorld("right");
        }

        //Jumping and moving left
        else if(jumping && leftMoving && !rightMoving) {
            shiftWorld("up");
            shiftWorld("left");
        }

        //Just jumping. If there is extra motion from moving, dissipate it
        else if(jumping) {
            shiftWorld("up");

            if(player.getXSpeed() != 0) {
                player.halt();

                double minSpeed = 0.01;
                if(player.getXSpeed() < minSpeed && player.getXSpeed() > 0) {
                    player.setXSpeed(0);
                }
                else if(player.getXSpeed() > -minSpeed && player.getXSpeed() < 0) {
                    player.setXSpeed(0);
                }
            }
        }
        //Moving right
        else if(rightMoving && !leftMoving) {
            shiftWorld("right");
        }
        //Moving left
        else if(leftMoving && !rightMoving) {
            shiftWorld("left");
        }
        //No controls being pushed
        //Slows the player down when they are not moving left or right
        else {
            if(player.getXSpeed() != 0) {
                player.halt();

                double minSpeed = 0.01;
                if(player.getXSpeed() < minSpeed && player.getXSpeed() > 0) {
                    player.setXSpeed(0);
                }
                else if(player.getXSpeed() > -minSpeed && player.getXSpeed() < 0) {
                    player.setXSpeed(0);
                }
            }
        }
    }

    // Takes in input and passes it to the player to move in the respective direction
    private void shiftWorld(@org.jetbrains.annotations.NotNull String dir) {
        switch(dir) {
            case "up":  player.jump();
                break;
            case "right": player.moveRight(worldWidth);
                          player.setFacingDir("right");
                break;
            case "left": player.moveLeft();
                         player.setFacingDir("left");
                break;
        }
    }

    //If you have to deal with this method, I recommend whiskey.
    //Calculates and creates the visibleBlockRows ArrayList
    public void setView() {
        //Review player's location and panel size to determine the visible area
        double panelWidth = panel.getWidth();
        double panelHeight = panel.getHeight();

        //The position of the player in the world, not the screen
        double playerXPos = player.getXPos();
        double playerYPos = player.getYPos();

        //An array for only the visible blocks, which is what will be drawn
        visibleBlockRows = new ArrayList<>();

        //Number of blocks visible in each axis
        int numBlockWidth = (int)(panelWidth / blockSize);
        int numBlockHeight = (int)(panelHeight / blockSize);

        //Add an extra block on each edge to make sure there's no spots where no blocks are rendered
        double leftMostBlockColumn = (playerXPos - (numBlockWidth >> 1)) - 2;
        double rightMostBlockColumn = (playerXPos + (numBlockWidth >> 1)) + 2;
        double topMostBlockRow = (playerYPos - (numBlockHeight >> 1)) - 2;
        double bottomMostBlockRow = (playerYPos + (numBlockHeight >> 1)) + 2;

        /*
          The player is locked in the middle of the screen. Every tick cycle they are moved to the middle of the screen.
          If the player is to go the edge of the world, they will deviate from the center towards that edge.
          Essentially the player is placed in the middle of the screen regardless, and their position is added/subtracted
               to if required for "edge" cases.
        */
        playerScreenX = (int)panelWidth / 2;
        playerScreenY = (int)panelHeight / 2;

        player.setScreenPosX(playerScreenX);
        player.setScreenPosY(playerScreenY);

        //If any edge of the world is nearby, the camera will stop scrolling in that direction but the character sprite
        //  will continue to move. This method will also allow two edges to affect the player at the same time

        /* Pseudocode of what each edge does:
            if(display of edge is over boundary)
                opposite edge -= amount over boundary
                playerDisplayPoint += amount over boundary * blockSize
                amount over boundary = 0
         */

        //Close to left edge
        if (leftMostBlockColumn < 0) {
            rightMostBlockColumn -= leftMostBlockColumn;
            playerScreenX += leftMostBlockColumn * blockSize;
            player.setScreenPosX(playerScreenX);
            leftMostBlockColumn = 0;
        }

        //Close to right edge
        if (rightMostBlockColumn > worldWidth) {
            leftMostBlockColumn -= rightMostBlockColumn - worldWidth;
            playerScreenX += (rightMostBlockColumn - worldWidth) * blockSize;
            player.setScreenPosX(playerScreenX);
            rightMostBlockColumn = (int) worldWidth;
        }

        //Close to top edge
        if (topMostBlockRow < 0) {
            bottomMostBlockRow -= topMostBlockRow;
            playerScreenY += topMostBlockRow * blockSize;
            player.setScreenPosY(playerScreenY);
            topMostBlockRow = 0;
        }

        //Close to bottom edge
        if (bottomMostBlockRow > worldHeight) {
            topMostBlockRow -= bottomMostBlockRow - worldHeight;
            playerScreenY += (bottomMostBlockRow - worldHeight) * blockSize;
            player.setScreenPosY(playerScreenY);
            bottomMostBlockRow = (int) worldHeight;
        }

        isCraftBench = false;

        //Each for loop will iterate through top to bottom, left to right
        for (int i = (int)Math.floor(topMostBlockRow); i < (int)Math.ceil(bottomMostBlockRow); i++) {
            //Working array
            ArrayList<Block> arr = new ArrayList<>();
            for (int o = (int)Math.floor(leftMostBlockColumn); o < (int)Math.ceil(rightMostBlockColumn); o++) {
                //Add the block at the given location to the working array
                arr.add(world.getBlockRows().get(i).get(o));

                if(world.getBlockRows().get(i).get(o).getID() == 9) isCraftBench = true;
            }
            //Add working array to visible blocks
            visibleBlockRows.add(arr);
        }
    }

    //------------ Graphics -------------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            drawWorld(g);
            drawPlayer(g);
            drawToolBar(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.setColor(new Color(0, 0, 0));
    }

    private void drawWorld(Graphics g) throws IOException {
        if (visibleBlockRows != null) {
            int y = 0;

            ArrayList<Integer> moveDifs = calcPaintDif();
            for (ArrayList<Block> visibleBlockRow : visibleBlockRows) {
                int x = 0;
                for (Block block : visibleBlockRow) {
                    if(block.getType().equals("door")) {
                        g.drawImage(block.getSprite(), x + moveDifs.get(0) - moveDifs.get(1) + (blockSize / 2), y + moveDifs.get(2) - moveDifs.get(3), blockSize, blockSize, null);
                        g.drawImage(blockTypes.getBlock(7).getTopSprite(), x + moveDifs.get(0) - moveDifs.get(1) + (blockSize / 2), y + moveDifs.get(2) - moveDifs.get(3) - 1, blockSize, blockSize, null);
                    }

                    g.drawImage(block.getSprite(), x + moveDifs.get(0) - moveDifs.get(1) + (blockSize / 2), y + moveDifs.get(2) - moveDifs.get(3), blockSize, blockSize, null);
                    block.setScreenPos(new Point(x + moveDifs.get(0) - moveDifs.get(1) + (blockSize / 2), y + moveDifs.get(2) - moveDifs.get(3)));
                    x += blockSize;
                }
                y += blockSize;
            }
        }
    }

    private void drawPlayer(Graphics g) {
        File playerSprite;
        if (player.getFacingDir().equals("left")) {
            playerSprite = new File(getCD() + "/src/net/publicvoid/Minigame/sprites/characters/henry.png");
        } else {
            playerSprite = new File(getCD() + "/src/net/publicvoid/Minigame/sprites/characters/henry_r.png");
        }

        try {
            player.setSprite(playerSprite);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Character is the same width as the blocks, but 1.75 times the height
        g.drawImage(player.getSprite(), playerScreenX, playerScreenY - playerHeight, blockSize, playerHeight, null);

        if (highlightedBlock != null) {
            g.drawRect(highlightedBlock.getScreenPos().x, highlightedBlock.getScreenPos().y, blockSize, blockSize);
        }
    }

    private void drawToolBar(Graphics g) {
        int toolbarX = panel.getWidth() / 2 - (blockSize * 10);
        int toolbarY = panel.getHeight() - blockSize * 2;

        //Draw background
        g.setColor(new Color(57, 56, 18, 164));
        g.fillRect(toolbarX, toolbarY, blockSize * 20, blockSize * 2);

        int numOfHotbarItems = 10;

        //This loop creates the borders around the hotbar items when they are not selected
        g.setColor(new Color(192, 192, 192, 87));
        for (int i = 0; i < numOfHotbarItems; i++) {
            g.drawRect(toolbarX + blockSize * (i * 2), panel.getHeight() - blockSize * 2, blockSize * 2, blockSize * 2);
        }
        g.setColor(new Color(0, 0, 0));

        /*
        This loop creates the black border around an item when it is selected.
        This and the previous loop are separate as if they are in one loop, graphical issues arise
        */
        for (int i = 0; i < numOfHotbarItems; i++) {
            if (player.getInventory().get(i).getItem() instanceof Block) {
                g.drawImage(((Block) player.getInventory().get(i).getItem()).getSprite(), toolbarX + blockSize * i * 2 + (blockSize / 2) + 1, (int) (panel.getHeight() - blockSize * 1.5), blockSize, blockSize, null);

                //Displays the number of held blocks
                g.setColor(new Color(255, 255, 255));
                g.drawString(String.valueOf(player.getInventory().get(i).getNumHeld()), toolbarX + blockSize * i * 2 + blockSize, panel.getHeight());
                g.setColor(new Color(0, 0, 0));
            } else if (player.getInventory().get(i).getItem() instanceof Pickaxe) {
                g.drawImage(((Pickaxe) player.getInventory().get(i).getItem()).getSprite(), toolbarX + blockSize * i * 2 + (blockSize / 2) + 1, (int) (panel.getHeight() - blockSize * 1.5), blockSize, blockSize, null);
            } else if (player.getInventory().get(i).getItem() instanceof Sword) {
                g.drawImage(((Sword) player.getInventory().get(i).getItem()).getSprite(), toolbarX + blockSize * i * 2 + (blockSize / 2) + 1, (int) (panel.getHeight() - blockSize * 1.5), blockSize, blockSize, null);
            }
            //Highlight the currently selected item slot
            if (player.getInventory().getSelectedSlotNum() == i) {
                g.drawRect(toolbarX + blockSize * i * 2, panel.getHeight() - blockSize * 2, blockSize * 2, blockSize * 2);
            }
        }

        if (kl.getIsInvOpen()) {
            //Draw background
            g.setColor(new Color(57, 56, 18, 164));
            g.fillRect(toolbarX, toolbarY - blockSize * 8, blockSize * 20, blockSize * 2 * 3);

            int index = 10;
            //This loop creates the borders around the inventory items when they are not selected
            for (int i = 0; i < 3; i++) {
                for (int o = 0; o < 10; o++) {
                    g.setColor(new Color(192, 192, 192, 87));
                    g.drawRect(toolbarX + blockSize * (o * 2), panel.getHeight() - blockSize * 10 + (blockSize * 2 * i), blockSize * 2, blockSize * 2);

                    if (player.getInventory().get(index).getItem() instanceof Block) {
                        g.drawImage(((Block) player.getInventory().get(index).getItem()).getSprite(), toolbarX + blockSize * i * 2 + (blockSize / 2) + 1, panel.getHeight() - blockSize * 10 + (blockSize * 2 * i) + (blockSize / 2), blockSize, blockSize, null);

                        //Displays the number of held blocks
                        g.setColor(new Color(255, 255, 255));
                        g.drawString(String.valueOf(player.getInventory().get(index).getNumHeld()), toolbarX + blockSize * i * 2 + blockSize, panel.getHeight() - blockSize * 8 + (blockSize * 2 * i) - 2);
                    } else if (player.getInventory().get(index).getItem() instanceof Pickaxe) {
                        g.drawImage(((Pickaxe) player.getInventory().get(index).getItem()).getSprite(), toolbarX + blockSize * i * 2 + (blockSize / 2) + 1, panel.getHeight() - blockSize * 10 + (blockSize * 2 * i) + (blockSize / 2), blockSize, blockSize, null);
                    } else if (player.getInventory().get(index).getItem() instanceof Sword) {
                        g.drawImage(((Sword) player.getInventory().get(index).getItem()).getSprite(), toolbarX + blockSize * i * 2 + (blockSize / 2) + 1, panel.getHeight() - blockSize * 10 + (blockSize * 2 * i) + (blockSize / 2), blockSize, blockSize, null);
                    }
                    index++;
                }
            }

            g.setColor(new Color(0,0,0));
            if(isCraftBench) {
                //getCraftable();
                g.drawRect(10,10,50,50);
            }
        }
    }

    //Unless you have to change this, look away
    private ArrayList<Integer> calcPaintDif() {
        ArrayList<Integer> arr = new ArrayList<>();
        int blockMiddleX = (visibleBlockRows.get(visibleBlockRows.size() / 2).size() / 2) * blockSize;
        int blockMiddleY = (visibleBlockRows.size() / 2) * blockSize;

        int yMoveDif;
        int xMoveDif;

        this.playerScreenX = player.getScreenPosX();
        this.playerScreenY = player.getScreenPosY();

        //Left and Right edges
        if(playerScreenX < panel.getWidth() / 2) {
            xMoveDif = 0;
        }
        else if(playerScreenX > panel.getWidth() / 2) {
            xMoveDif = 0;
        } else {
            xMoveDif = (int)((player.getXPos() - Math.floor(player.getXPos())) * blockSize);
        }

        //Top and Bottom edges
        if(playerScreenY < panel.getHeight() / 2) {
            yMoveDif = 0;
        }
        else if(playerScreenY > panel.getHeight() / 2) {
            yMoveDif = 0;
        } else {
            yMoveDif = (int)((player.getYPos() - Math.floor(player.getYPos())) * blockSize);
        }

        int xDif = (panel.getWidth() / 2) - blockMiddleX;
        int yDif = (panel.getHeight() / 2) - blockMiddleY;

        arr.add(xDif);
        arr.add(xMoveDif);
        arr.add(yDif);
        arr.add(yMoveDif);

        return arr;
    }

    //Highlights the hovered over block
    public void highlight() {
        //Mouse position on the panel
        Point mouseHoverPos = new Point(MouseInfo.getPointerInfo().getLocation().x - panel.getLocationOnScreen().x, getPointerInfo().getLocation().y - panel.getLocationOnScreen().y);

        //Defaults as a large number, one that the distance between the mouse and any block would never be higher.
        // There's nothing actually special about the number 10000
        double shortestDist = 10000;

        //The block that is the shortest distance from the cursor
        Block shortestBlock = null;

        //Calculates the distance between the player and the mouse to verify that the mouse is in the player's reach
        double playerToMouse = distance(player.getScreenPosX(), player.getScreenPosY(), mouseHoverPos.x, mouseHoverPos.y);

        //This will check each block in the visibleBlock array until it finds the shortest distance
        if(visibleBlockRows != null) {
            if(playerToMouse <= player.getReach()) {
                //Breaks out of loop here if a block with a distance of less than 1 block radius is found, which is mathematically
                // impossible not to be the hovered over block
                foundClosestBlock:
                for (ArrayList<Block> visibleBlockRow : visibleBlockRows) {
                    for (Block block : visibleBlockRow) {
                        if (block.getScreenPos() != null) {
                            //Center point of the block
                            int blockX = (block.getScreenPos().x) + (blockSize / 2);
                            int blockY = (block.getScreenPos().y) + (blockSize / 2);

                            //Get the distance between the two above points
                            double workingDist = distance(mouseHoverPos.x, mouseHoverPos.y, blockX, blockY);

                            //Compare the found distance, and if it is shorter than any previous ones, that is the shortest
                            if (workingDist < shortestDist) {
                                shortestDist = workingDist;
                                shortestBlock = block;

                                //Shortest distance found
                                if (shortestDist < blockSize >> 1) {
                                    break foundClosestBlock;
                                }
                            }
                        }
                    }
                }
                highlightedBlock = shortestBlock;
            } else {
                highlightedBlock = null;
            }
        }
    }
    // ------------------


    //-------------- Getters ----------------
    public Player getPlayer() {
        return player;
    }

    public ArrayList<ArrayList<Block>> getVisibleBlockRows() {
        return visibleBlockRows;
    }
    //-----------------------


    //------- Helper Methods ---------
    //Takes a string input and logs it to the console
    private static void log(String input) {
        Date now = new Date();
        //"substring(4,19)" includes only the current date and time and ignores the rest of the data (day of week, year, etc.)
        System.out.println("[" + now.toString().substring(4, 19) + "] " + input);
    }

    //Get current directory
    private String getCD() {
        Path currentRelativePath = Paths.get("");

        return currentRelativePath.toAbsolutePath().toString();
    }

    //Method to find the distance between two points
    private double distance(double x1, double y1, int x2, int y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    private void startTimer() {
        //Start the tick that runs the game, roughly 60fps
        final int DELAY = 15; // Milliseconds between timer ticks
        TimerListener timerListener = new TimerListener(this);
        Timer timer = new Timer(DELAY, timerListener);
        timer.start();
    }

    //Calls for the world and inventory to save
    void save() throws IOException {
        log("Saving World...");
        player.getInventory().saveInventory(worldName, toolTypes, blockTypes);
        world.saveWorld();
        log("Save Complete.");
    }

    //------------------------


    public void calcSurroundingBlocks(Player player) throws IOException {
        int roundedX = (int)Math.floor(player.getXPos());
        int roundedY = (int)Math.floor(player.getYPos());

        ArrayList<Block> surroundBlocks = new ArrayList<>();
        Block aboveBlock;
        Block rightTopBlock;
        Block rightBottomBlock;
        Block belowBlock;
        Block leftBottomBlock;
        Block leftTopBlock;

        if(!(player.getXPos() <= 0 || player.getXPos() >= 1280 || player.getYPos() >= 128 || player.getYPos() - 3 < 0)) {
            aboveBlock = world.getBlockRows().get(roundedY - 3).get(roundedX);
            rightTopBlock = world.getBlockRows().get(roundedY - 2).get(roundedX + 1);
            rightBottomBlock = world.getBlockRows().get(roundedY - 1).get(roundedX + 1);
            belowBlock = world.getBlockRows().get(roundedY).get(roundedX);
            leftBottomBlock = world.getBlockRows().get(roundedY - 1).get(roundedX - 1);
            leftTopBlock = world.getBlockRows().get(roundedY - 2).get(roundedX - 1);
        }

        //If the player falls out of bounds
        else {
            //Air
            aboveBlock = blockTypes.getBlock(0);
            rightTopBlock = blockTypes.getBlock(0);
            rightBottomBlock = blockTypes.getBlock(0);
            belowBlock = blockTypes.getBlock(0);
            leftBottomBlock = blockTypes.getBlock(0);
            leftTopBlock = blockTypes.getBlock(0);
        }

        surroundBlocks.add(aboveBlock);
        surroundBlocks.add(rightTopBlock);
        surroundBlocks.add(rightBottomBlock);
        surroundBlocks.add(belowBlock);
        surroundBlocks.add(leftBottomBlock);
        surroundBlocks.add(leftTopBlock);

        player.setSurroundingBlocks(surroundBlocks);
    }

    public void useSurroundingBlocks() {
        ArrayList<Block> blocks = player.getSurroundBlocks();

        // Index 0 is the top of the player
        if(!(blocks.get(0).getType().equals("air"))) {
            if(player.getYPos() - 3.15 <= Math.floor(player.getYPos() - 3)) {
                player.setYSpeed(0);
            }
        }

        // Index 1 is the top right of the player
        if(!(blocks.get(1).getType().equals("air"))) {
            if(player.getXPos() + 0.2 >= Math.ceil(player.getXPos())) {
                player.setXSpeed(0);
                player.setXPos(player.getXPos() - 0.0035);
            }
        }

        // Index 2 is the bottom right of the player
        if(!(blocks.get(2).getType().equals("air"))) {
            if(player.getXPos() + 0.2 >= Math.ceil(player.getXPos())) {
                player.setXSpeed(0);
                player.setXPos(player.getXPos() - 0.0035);
            }
        }

        // Index 3 is the block below the player
        if(blocks.get(3).getID() == 0) {
            player.fall();
        } else {
            //TODO: If I want to add water, I will need to change this
            //If the player lands on anything other than air they will stop
            player.setYSpeed(0);
            player.setYPos(Math.floor(player.getYPos()));
        }

        // Index 4 is the bottom left of the player
        if(!(blocks.get(4).getType().equals("air"))) {
            if(player.getXPos() - 0.2 <= Math.floor(player.getXPos())) {
                player.setXSpeed(0);
                player.setXPos(player.getXPos() + 0.0035);
            }
        }

        // Index 5 is the top left of the player
        if(!(blocks.get(5).getType().equals("air"))) {
            if(player.getXPos() - 0.2 <= Math.floor(player.getXPos())) {
                player.setXSpeed(0);
                player.setXPos(player.getXPos() + 0.0035);
            }
        }
    }

    private ArrayList<CraftingRecipe> getCraftable() {
        ArrayList<CraftingRecipe> craftable = new ArrayList<>();


        return craftable;
    }
}
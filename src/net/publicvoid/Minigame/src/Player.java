package net.publicvoid.Minigame.src;

import net.publicvoid.Minigame.src.inventory.Inventory;
import net.publicvoid.Minigame.src.world.Block;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
    private double maxSpeed, yMaxSpeed, xSpeed, ySpeed;
    private double xPos, yPos;
    private int screenPosX, screenPosY;
    private String facingDir;
    private BufferedImage sprite;
    private int inventorySpace;
    private Inventory inventory;
    private double reach;
    private ArrayList<Block> surroundBlocks;

    Player(double xPos, double yPos, File sprite, double maxSpeed) throws IOException {
        //These refer to the players location in the world, not the screen
        this.xPos = xPos;
        this.yPos = yPos;
        this.maxSpeed = maxSpeed;
        this.facingDir = "left"; //Default facing direction
        this.inventorySpace = 40;

        this.yMaxSpeed = 1;

        inventory = new Inventory(this);

        this.sprite = ImageIO.read(sprite.getAbsoluteFile());
    }

    void setXPos(double xPos) {
        this.xPos = xPos;
    }

    void setYPos(double yPos) {
        this.yPos = yPos;
    }

    void setSurroundingBlocks(ArrayList<Block> surroundBlocks) {
        this.surroundBlocks = surroundBlocks;
    }

    //TODO: If I want to implement items to increase speed
    void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    void setSprite(File sprite) throws IOException {
        this.sprite = ImageIO.read(sprite.getAbsoluteFile());
    }

    void moveLeft() {
        if(!(Math.floor(xPos) <= 0)) {
            if(!(xSpeed <= (-maxSpeed))) {
                //This creates the "speeding up" effect taking ~1 second, or 60 ticks, to get to full speed
                xSpeed -= maxSpeed / 60;
                xPos += xSpeed;
            } else {
                xSpeed = (-1 * maxSpeed);
                xPos += xSpeed;
            }
        }
        else if(xPos <= 0) xPos = 0;
    }

    void moveRight(double worldWidth) {
        if(!(Math.ceil(xPos) >= worldWidth)) {
            if(!(xSpeed >= maxSpeed)) {
               xSpeed += (maxSpeed / 60);
                xPos += xSpeed;
            }
            else {
                xSpeed = maxSpeed;
                xPos += xSpeed;
            }
        }
        else if(xPos >= worldWidth) xPos = worldWidth;
    }

    void jump() {
        if(!(yPos <= 0)) {
            if(!surroundBlocks.get(3).getType().equals("air")) {
                ySpeed -= 0.65;
                yPos += ySpeed;
            }
        }
        else if(yPos <= 0) yPos = 0;
    }

    void halt() {
        if(!(Math.floor(xPos) <= 0) && !(Math.ceil(xPos) >= 1280)) {
            xSpeed -= xSpeed / 15;
            xPos += xSpeed;
        }
        else {
            xSpeed = 0;
        }
    }

    void fall() {
        if(ySpeed < yMaxSpeed) {
            ySpeed = ySpeed + 0.05;
            yPos += ySpeed;
        }
        //Only reached if the player is at max speed
        else {
            ySpeed = yMaxSpeed;
            yPos += ySpeed;
        }
    }

    ArrayList<Block> getSurroundBlocks() {
        return surroundBlocks;
    }

    String getFacingDir() {
        return facingDir;
    }

    void setFacingDir(String facingDir) {
        this.facingDir = facingDir;
    }

    void setXSpeed(double xSpeed) {
        this.xSpeed = xSpeed;
    }

    void setYSpeed(double ySpeed) {
        this.ySpeed = ySpeed;
    }

    double getXSpeed() {
        return xSpeed;
    }

    double getYSpeed() {
        return ySpeed;
    }

    double getMaxSpeed() {
        return maxSpeed;
    }

    public double getXPos(){
        return xPos;
    }

    public double getYPos(){
        return yPos;
    }

    BufferedImage getSprite() {
        return sprite;
    }

    public double getReach() {
        return reach;
    }

    void setReach(double reach) {
        this.reach = reach;
    }

    public int getInventorySpace() {
        return inventorySpace;
    }

    //TODO: If I want to implement a growing inventory
    public void setInventorySpace(int inventorySpace) {
        this.inventorySpace = inventorySpace;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getScreenPosY() {
        return screenPosY;
    }

    void setScreenPosY(int screenPosY) {
        this.screenPosY = screenPosY;
    }

    public int getScreenPosX() {
        return screenPosX;
    }

    void setScreenPosX(int screenPosX) {
        this.screenPosX = screenPosX;
    }
}
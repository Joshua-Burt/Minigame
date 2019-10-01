package net.publicvoid.Minigame.src.world;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Block {
    private int ID;
    private String type;
    private BufferedImage sprite;
    private BufferedImage topSprite;
    private Point pos;
    private Point screenPos;
    private int hardness;

    public Block(int ID, String type, BufferedImage sprite, Point pos, int hardness) {
        this.ID = ID;
        this.type = type;
        this.pos = pos;
        this.hardness = hardness;

        if(sprite != null) {
            this.sprite = sprite;
        }
    }

    Block(int ID, String type, BufferedImage sprite, BufferedImage topSprite, Point pos, int hardness) {
        this.ID = ID;
        this.type = type;
        this.pos = pos;
        this.hardness = hardness;

        if(sprite != null) {
            this.sprite = sprite;
            this.topSprite = topSprite;
        }
    }

    public void setScreenPos(Point screenPos) {
        this.screenPos = screenPos;
    }

    public Point getScreenPos() {
        return screenPos;
    }

    public Point getPos() {
        return pos;
    }

    public int getID() {
        return ID;
    }

    public String getType() {
        return type;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    //For multi-block blocks, such as doors
    public BufferedImage getTopSprite() {
        return topSprite;
    }

    public int getHardness() {
        return hardness;
    }
}

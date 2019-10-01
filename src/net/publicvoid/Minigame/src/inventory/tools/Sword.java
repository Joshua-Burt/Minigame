package net.publicvoid.Minigame.src.inventory.tools;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sword implements Tool {
    private int ID;
    private String type;
    private int damage;
    private int durability;
    private int hardness;
    private Point invPos;
    private BufferedImage sprite;

    Sword(int ID, String type, int damage, int durability, BufferedImage sprite) {
        this.ID = ID;
        this.type = type;
        this.damage = damage;
        this.durability = durability;
        this.sprite = sprite;
    }

    public int getID() {
        return ID;
    }

    @Override
    public Point getInvPos() {
        return invPos;
    }

    @Override
    public void setInvPos(Point invPos) {
        this.invPos = invPos;
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public BufferedImage getSprite() {
        return sprite;
    }

    @Override
    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getHardness() {
        return hardness;
    }

    @Override
    public void setHardness(int hardness) {
        this.hardness = hardness;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public void setDamage(int damage) {
        this.damage = damage;
    }
}

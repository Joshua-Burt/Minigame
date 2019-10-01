package net.publicvoid.Minigame.src.inventory.tools;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Pickaxe implements Tool {
    private int ID;
    private String type;
    private int damage;
    private int hardness;
    private int durability;
    private Point invPos;
    private BufferedImage sprite;

    Pickaxe(int ID, String type, int damage, int hardness, int durability, BufferedImage sprite) {
        this.ID = ID;
        this.type = type;
        this.damage = damage;
        this.hardness = hardness;
        this.durability = durability;
        this.sprite = sprite;
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

    public int getID() {
        return ID;
    }
}

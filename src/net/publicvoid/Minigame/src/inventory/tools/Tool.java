package net.publicvoid.Minigame.src.inventory.tools;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Tool {
    Point getInvPos();
    void setInvPos(Point invPos);
    int getDurability();
    void setDurability(int durability);
    BufferedImage getSprite();
    void setSprite(BufferedImage sprite);
    String getType();
    int getHardness();
    void setHardness(int hardness);
    int getDamage();
    void setDamage(int damage);


}

package de.erethon.lectern.menu.elements;


import net.minecraft.world.item.ItemStack;

import java.awt.Image;
import java.util.List;

public interface Changing {
    List<Image> getTextures();

    ItemStack getContentItem();
}

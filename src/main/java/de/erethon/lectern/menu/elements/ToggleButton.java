package de.erethon.lectern.menu.elements;

import net.kyori.adventure.text.Component;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Material;
import java.awt.*;
import java.util.List;

public class ToggleButton extends Button implements Changing {

    private final Image toggledTexture;
    private boolean toggled;

    public ToggleButton(int x, int y, int width, int height, Image texture, Image toggled, Material material, Component name, List<Component> lore) {
        super(x, y, width, height, texture, material, name, lore);
        this.toggledTexture = toggled;
    }

    @Override
    public Image getTexture() {
        return super.getTexture();
    }

    @Override
    public List<Image> getTextures() {
        return List.of(getTexture(), toggledTexture);
    }

    @Override
    public ItemStack getContentItem() {
        return asItem();
    }
}

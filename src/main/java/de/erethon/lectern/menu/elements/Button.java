package de.erethon.lectern.menu.elements;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.awt.Image;
import java.util.List;

public class Button extends UIElement implements Texturable {

    private final Image texture;
    private final Component name;
    private final List<Component> lore;
    private final Material material;

    public Button(int x, int y, int width, int height, Image texture, Material material, Component name, List<Component> lore) {
        super(x, y, width, height);
        this.texture = texture;
        this.material = material;
        this.name = name;
        this.lore = lore;
    }

    @Override
    public Image getTexture() {
        return texture;
    }

    public net.minecraft.world.item.ItemStack asItem() {
        org.bukkit.inventory.ItemStack stack = new ItemStack(material);
        stack.editMeta(meta -> {
            meta.displayName(name);
            meta.lore(lore);
        });
        return CraftItemStack.asNMSCopy(stack);
    }

}

package de.erethon.lectern.menu.elements;

import de.erethon.lectern.interaction.ClickHandler;
import net.minecraft.world.item.Items;

import java.util.HashSet;
import java.util.Set;

public class UIElement {

    private int x;
    private int y;
    private int width;
    private int height;
    private Set<ClickHandler> handlers = new HashSet<>();

    public UIElement() {
    }

    public UIElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int x() {
        return x;
    }

    public void x(int x) {
        this.x = x;
    }

    public int y() {
        return y;
    }

    public void y(int y) {
        this.y = y;
    }

    public int width() {
        return width;
    }

    public void width(int width) {
        this.width = width;
    }

    public int height() {
        return height;
    }

    public void height(int height) {
        this.height = height;
    }

    public void addHandler(ClickHandler handler) {
        handlers.add(handler);
    }

    public void removeHandler(ClickHandler handler) {
        handlers.remove(handler);
    }

    public void clearHandlers() {
        handlers.clear();
    }

    public net.minecraft.world.item.ItemStack asItem() {
        return Items.AIR.getDefaultInstance();
    }
}


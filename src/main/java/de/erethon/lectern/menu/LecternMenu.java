package de.erethon.lectern.menu;

import de.erethon.lectern.Lectern;
import de.erethon.lectern.interaction.ClickHandler;
import de.erethon.lectern.menu.elements.Changing;
import de.erethon.lectern.menu.elements.UIElement;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LecternMenu {

    private final Lectern lectern;
    private final Player player;
    private final Map<Integer, Set<ClickHandler>> handlers = new HashMap<>();
    private final Set<UIElement> elements = new HashSet<>();
    private int size = 54;

    public LecternMenu(Player player) {
        this.lectern = Lectern.get();
        this.player = player;
    }

    public void addElement(UIElement element) {
        elements.add(element);
        if (element.y() + element.height() > size / 9) {
            size = (element.y() + element.height()) * 9;
        }
    }

    public void removeElement(UIElement element) {
        elements.remove(element);
    }

    public void clearElements() {
        elements.clear();
    }

    public Set<UIElement> getElements() {
        return elements;
    }

    public void addHandler(int slot, ClickHandler handler) {
        if (!handlers.containsKey(slot)) {
            handlers.put(slot, Set.of(handler));
        } else {
            handlers.get(slot).add(handler);
        }
    }

    public void removeHandler(int slot, ClickHandler handler) {
        if (handlers.containsKey(slot)) {
            handlers.get(slot).remove(handler);
        }
    }

    public void clearHandlers(int slot) {
        handlers.remove(slot);
    }

    public void clearAllHandlers() {
        handlers.clear();
    }

    public void onLeftClick(int slot) {
        if (handlers.containsKey(slot)) {
            handlers.get(slot).forEach(ClickHandler::onLeftClick);
        }
    }

    public void onRightClick(int slot) {
        if (handlers.containsKey(slot)) {
            handlers.get(slot).forEach(ClickHandler::onRightClick);
        }
    }

    public void onMiddleClick(int slot) {
        if (handlers.containsKey(slot)) {
            handlers.get(slot).forEach(ClickHandler::onMiddleClick);
        }
    }

    public void open() {
        lectern.addOpenMenu(player, this);
    }

    // We need to create an entirely new texture for every possible state combination. Let's not do that for now.
    public boolean hasChangingElements() {
        for (UIElement element : elements) {
            if (element instanceof Changing) {
                return true;
            }
        }
        return false;
    }

    public ClientboundContainerSetContentPacket getContentPacket(int id) {
        HashMap<Integer, ItemStack> stacks = new HashMap<>();
        int slotIndex = 0;
        for (UIElement element : elements) {
            slotIndex = Lectern.xyToSlot(element.x(), element.y());
            if (element instanceof Changing ch) {
                stacks.put(slotIndex, ch.getContentItem());
            } else {
                stacks.put(slotIndex, element.asItem());
            }
            if (element.width() > 1) {
                for (int i = 1; i < element.width(); i++) {
                    stacks.put(slotIndex + i, ItemStack.EMPTY);
                }
            }
            if (element.height() > 1) {
                for (int i = 1; i < element.height(); i++) {
                    stacks.put(slotIndex + i * 9, ItemStack.EMPTY);
                }
            }
        }
        ArrayList<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < 54; i++) {
            list.add(stacks.getOrDefault(i, ItemStack.EMPTY));
        }
        NonNullList<ItemStack> nonNullList = NonNullList.of(ItemStack.EMPTY, list.toArray(new ItemStack[0]));
        return new ClientboundContainerSetContentPacket(id, 0, nonNullList, ItemStack.EMPTY);
    }

    public ClientboundContainerSetContentPacket getLowerContentPacket() {
        int y;
        HashMap<Integer, ItemStack> stacks = new HashMap<>();
        int slotIndex = 0;
        for (UIElement element : elements) {
            if (element.y() <= 6) {
                continue;
            }
            y = element.y() - 6;
            slotIndex = Lectern.xyToSlot(element.x(), element.y());
            if (element instanceof Changing ch) {
                stacks.put(slotIndex, ch.getContentItem());
            } else {
                stacks.put(slotIndex, element.asItem());
            }
            if (element.width() > 1) {
                for (int i = 1; i < element.width(); i++) {
                    stacks.put(slotIndex + i, ItemStack.EMPTY);
                }
            }
            if (element.height() > 1) {
                for (int i = 1; i < element.height(); i++) {
                    stacks.put(slotIndex + i * 9, ItemStack.EMPTY);
                }
            }
        }
        ArrayList<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < 54; i++) {
            list.add(stacks.getOrDefault(i, ItemStack.EMPTY));
        }
        NonNullList<ItemStack> nonNullList = NonNullList.of(ItemStack.EMPTY, list.toArray(new ItemStack[0]));
        return new ClientboundContainerSetContentPacket(0, 0, nonNullList, ItemStack.EMPTY);
    }


}

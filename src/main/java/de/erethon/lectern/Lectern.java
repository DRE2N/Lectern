package de.erethon.lectern;

import de.erethon.PacketReceiveEvent;
import de.erethon.PacketSendEvent;
import de.erethon.lectern.menu.LecternMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;

public class Lectern implements Listener {

    private HashMap<Player, Integer> openMenuID = new HashMap<>();
    private HashMap<Integer, LecternMenu> openMenus = new HashMap<>();
    private static Lectern instance;
    private int currentMenuID = 1;

    public Lectern(Plugin implementingPlugin) {
        Bukkit.getPluginManager().registerEvents(this, implementingPlugin);
        System.out.println("Lectern initialized");
        instance = this;
    }

    public void addOpenMenu(Player player, LecternMenu menu) {
        int containerId = currentMenuID++;
        if (currentMenuID > 100) { // Prevent overflow
            currentMenuID = 1;
        }
        openMenus.put(containerId, menu);
        openMenuID.put(player, containerId);
        ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(containerId, MenuType.GENERIC_9x6, Component.literal(""));
        getConnection(player).send(packet);
        getConnection(player).send(menu.getContentPacket(containerId));
        getConnection(player).send(menu.getLowerContentPacket());
    }

    public void removeOpenMenu(Player player, boolean playerClosed) {
        openMenus.remove(openMenuID.get(player));
        openMenuID.remove(player);
        if (!playerClosed) {
            getConnection(player).send(new ClientboundContainerClosePacket(openMenuID.get(player)));
        }
        player.updateInventory(); // Resend the actual inventory to the client
    }

    @EventHandler
    private void onPacketSend(PacketSendEvent event) {
        Player player = event.getConnection().getPlayer().getBukkitEntity();
        // Prevent all other menu updates
        if (event.getPacket() instanceof ClientboundContainerSetContentPacket packet) {
            if (openMenuID.containsKey(player) && packet.getContainerId() != openMenuID.get(player)) {
                event.setCancelled(true);
            }
        }
        if (event.getPacket() instanceof ClientboundContainerSetDataPacket packet) {
            if (openMenuID.containsKey(player) && packet.getContainerId() != openMenuID.get(player)) {
                event.setCancelled(true);
            }
        }
        if (event.getPacket() instanceof ClientboundContainerSetSlotPacket packet) {
            if (openMenuID.containsKey(player) && packet.getContainerId() != openMenuID.get(player)) {
                event.setCancelled(true);
            }
        }
        if (event.getPacket() instanceof ClientboundOpenScreenPacket  packet) {
            if (openMenuID.containsKey(player) && packet.getContainerId() != openMenuID.get(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onPacketReceive(PacketReceiveEvent event) {
        Player player = event.getConnection().getPlayer().getBukkitEntity();
        if (event.getPacket() instanceof ServerboundContainerClickPacket packet) {
            if (packet.getContainerId() == openMenuID.get(player) || packet.getContainerId() == 0) {
                LecternMenu menu = openMenus.get(openMenuID.get(player));
                event.setCancelled(true);
                int buttonId = packet.getButtonNum();
                if (buttonId == 0) {
                    menu.onLeftClick(packet.getSlotNum());
                } else if (buttonId == 1) {
                    menu.onRightClick(packet.getSlotNum());
                }
                getConnection(player).send(menu.getContentPacket(openMenuID.get(player)));
                getConnection(player).send(menu.getLowerContentPacket());
            }
        }
        if (event.getPacket() instanceof ServerboundContainerClosePacket packet) {
            if (packet.getContainerId() == openMenuID.get(player)) {
                removeOpenMenu(player, true);
            }
        }
    }

    public static Lectern get() {
        return instance;
    }

    private Connection getConnection(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        return ((CraftPlayer) player).getHandle().connection.connection;
    }

    // Gets the XY for a 9x6 grid
    public static Vec2 slotToXY(int slot) {
        int x = slot % 9;
        int y = slot / 6;
        return new Vec2(x, y);
    }

    public static int xyToSlot(int x, int y) {
        return y * 9 + x;
    }
}

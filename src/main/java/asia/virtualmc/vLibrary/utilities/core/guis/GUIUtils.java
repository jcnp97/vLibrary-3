package asia.virtualmc.vLibrary.utilities.core.guis;

import asia.virtualmc.vLibrary.utilities.messages.AdventureUtils;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class GUIUtils {
    private static final Set<UUID> respondedPlayers = ConcurrentHashMap.newKeySet();

    public static ItemStack createButton(Material material, String displayName, int modelData) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            Component name = AdventureUtils.convertToComponent("<!i>" + displayName);
            meta.displayName(name);
            meta.setCustomModelData(modelData);

            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createButton(ItemStack item, String displayName) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            Component name = AdventureUtils.convertToComponent("<!i>" + displayName);
            meta.displayName(name);

            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createButton(Material material, String displayName, int modelData, List<String> list) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            Component name = AdventureUtils.convertToComponent("<!i>" + displayName);
            List<Component> lore = AdventureUtils.convertToComponent(list);

            meta.displayName(name);
            meta.lore(lore);
            meta.setCustomModelData(modelData);

            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createButton(ItemStack item, String displayName, List<String> list) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            Component name = AdventureUtils.convertToComponent("<!i>" + displayName);
            List<Component> lore = AdventureUtils.convertToComponent(list);

            meta.displayName(name);
            meta.lore(lore);

            item.setItemMeta(meta);
        }
        return item;
    }

    public static ChestGui getDisplay(String displayName, List<ItemStack> items) {
        ChestGui gui = new ChestGui(6, displayName);
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        OutlinePane pane = new OutlinePane(9, 6);

        for (ItemStack item : items) {
            GuiItem guiItem = new GuiItem(item);
            pane.addItem(guiItem);
        }

        gui.addPane(pane);
        return gui;
    }

    public static void getConfirmation(Player player, Consumer<Boolean> callback) {
        UUID uuid = player.getUniqueId();
        respondedPlayers.remove(uuid);

        ChestGui gui = new ChestGui(3, GUIConfig.get("confirm-gui-title"));
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane pane = new StaticPane(0, 0, 9, 3);

        // Confirm buttons
        for (int i = 1; i <= 3; i++) {
            ItemStack confirmButton = createButton(GUIConfig.getItem(), "<green>ᴄᴏɴғɪʀᴍ ᴀᴄᴛɪᴏɴ");
            GuiItem confirm = new GuiItem(confirmButton, event -> {
                callback.accept(true);
                event.getWhoClicked().closeInventory();
            });
            pane.addItem(confirm, i, 1);
        }

        // Cancel buttons
        for (int i = 5; i <= 7; i++) {
            ItemStack cancelButton = createButton(GUIConfig.getItem(), "<red>ᴄᴀɴᴄᴇʟ ᴀᴄᴛɪᴏɴ");
            GuiItem cancel = new GuiItem(cancelButton, event -> {
                callback.accept(false);
                event.getWhoClicked().closeInventory();
            });
            pane.addItem(cancel, i, 1);
        }

        gui.setOnClose(event -> {
            if (respondedPlayers.add(uuid)) {
                callback.accept(false); // Treat GUI close as cancel
            }
        });

        gui.addPane(pane);
        gui.show(player);
    }

    // Sample Usage
//    GUIUtils.getConfirmation(player, confirmed -> {
//        if (confirmed) {
//            player.sendMessage("You confirmed the action.");
//            // Run your confirmed logic here
//        } else {
//            player.sendMessage("You cancelled the action.");
//            // Run your cancel logic here
//        }
//    });
}

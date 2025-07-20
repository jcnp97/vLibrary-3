package asia.virtualmc.vLibrary.utilities.core.guis.skills_core;

import asia.virtualmc.vLibrary.utilities.core.guis.GUIConfig;
import asia.virtualmc.vLibrary.utilities.core.guis.GUIUtils;
import asia.virtualmc.vLibrary.utilities.messages.MessageUtils;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class RankGUIUtils {

    public static class PlayerData {
        public int numericalRank;
        public String currentRank;
        public String nextRank;
        public double previous;
        public double current;
        public double next;

        public PlayerData(int numericalRank, String currentRank, String nextRank,
                          double previous, double current, double next) {
            this.numericalRank = numericalRank;
            this.currentRank = currentRank;
            this.nextRank = nextRank;
            this.previous = previous;
            this.current = current;
            this.next = next;
        }
    }

    public static ChestGui get(Player player, PlayerData data, ItemStack progressLore) {
        ChestGui gui = new ChestGui(3, GUIConfig.get("rank-gui-title"));
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        StaticPane staticPane = RankGUIUtils.getProgressBar(data.current, data.next,
                progressLore);

        if (data.current >= data.next) {
            for (int x = 3; x <= 5; x++) {
                ItemStack confirmButton = RankGUIUtils.allow();
                staticPane.addItem(new GuiItem(confirmButton, event ->
                        GUIUtils.getConfirmation(player, confirmed -> {
                            if (confirmed) {
                                process(player, data);
                            } else {
                                event.getWhoClicked().closeInventory();
                            }
                        })), x, 2);
            }
        } else {
            for (int x = 3; x <= 5; x++) {
                staticPane.addItem(new GuiItem(RankGUIUtils.deny()), x, 2);
            }
        }

        // Add stats lore
        staticPane.addItem(new GuiItem(progressLore), 8, 0);
        gui.addPane(staticPane);
        return gui;
    }

    public static void process(Player player, PlayerData data) {
        MessageUtils.sendPlayerMessage(player, "Rank-up success!");
        player.closeInventory();
    }

    public static ItemStack allow() {
        return GUIUtils.createButton(GUIConfig.getItem(), "<green>Process rank-up");
    }

    public static ItemStack deny() {
        return GUIUtils.createButton(GUIConfig.getItem(), "<red>Not enough points to rank-up!");
    }

    public static StaticPane getProgressBar(double current, double next, ItemStack lore) {
        Map<Integer, GuiItem> progressBar = new HashMap<>();
        StaticPane staticPane = new StaticPane(0, 0, 9, 4);

        for (int x = 1; x <= 7; x++) {
            GuiItem guiItem = new GuiItem(lore);
            staticPane.addItem(guiItem, x, 1);
            progressBar.put(x, guiItem);
        }

        // modifications
        double progress = Math.min(100, (current/next) * 100);
        int progressChunk = (int) Math.floor(progress / 15);

        switch (progressChunk) {
            case 0 -> modifyStatButton0(progressBar, progress);
            case 1 -> modifyStatButton1(progressBar, progress - 15);
            case 2 -> modifyStatButton2(progressBar, progress - 30);
            case 3 -> modifyStatButton3(progressBar, progress - 45);
            case 4 -> modifyStatButton4(progressBar, progress - 60);
            case 5 -> modifyStatButton5(progressBar, progress - 75);
            case 6 -> modifyStatButton6(progressBar, progress - 90);
        }

        return staticPane;
    }

    // Modifications
    private static void modifyStatButton0(Map<Integer, GuiItem> buttons, double progress) {
        int progressChunk = (int) progress / 3;
        buttons.get(1).setItem(setCustomModelData(buttons.get(1).getItem(), 100000 + progressChunk));

        for (int i = 2; i < 7; i++) {
            buttons.get(i).setItem(setCustomModelData(buttons.get(i).getItem(), 100005));
        }
        buttons.get(7).setItem(setCustomModelData(buttons.get(7).getItem(), 100010));
    }

    private static void modifyStatButton1(Map<Integer, GuiItem> buttons, double progress) {
        buttons.get(1).setItem(setCustomModelData(buttons.get(1).getItem(), 100004));

        int progressChunk = (int) progress / 3;
        buttons.get(2).setItem(setCustomModelData(buttons.get(2).getItem(), 100005 + progressChunk));

        for (int i = 3; i < 7; i++) {
            buttons.get(i).setItem(setCustomModelData(buttons.get(i).getItem(), 100005));
        }
        buttons.get(7).setItem(setCustomModelData(buttons.get(7).getItem(), 100010));
    }

    private static void modifyStatButton2(Map<Integer, GuiItem> buttons, double progress) {
        buttons.get(1).setItem(setCustomModelData(buttons.get(1).getItem(), 100004));
        buttons.get(2).setItem(setCustomModelData(buttons.get(2).getItem(), 100009));

        int progressChunk = (int) progress / 3;
        buttons.get(3).setItem(setCustomModelData(buttons.get(3).getItem(), 100005 + progressChunk));

        for (int i = 4; i < 7; i++) {
            buttons.get(i).setItem(setCustomModelData(buttons.get(i).getItem(), 100005));
        }
        buttons.get(7).setItem(setCustomModelData(buttons.get(7).getItem(), 100010));
    }

    private static void modifyStatButton3(Map<Integer, GuiItem> buttons, double progress) {
        buttons.get(1).setItem(setCustomModelData(buttons.get(1).getItem(), 100004));

        for (int i = 2; i < 4; i++) {
            buttons.get(i).setItem(setCustomModelData(buttons.get(i).getItem(), 100009));
        }

        int progressChunk = (int) progress / 3;
        buttons.get(4).setItem(setCustomModelData(buttons.get(4).getItem(), 100005 + progressChunk));

        for (int i = 5; i < 7; i++) {
            buttons.get(i).setItem(setCustomModelData(buttons.get(i).getItem(), 100005));
        }
        buttons.get(7).setItem(setCustomModelData(buttons.get(7).getItem(), 100010));
    }

    private static void modifyStatButton4(Map<Integer, GuiItem> buttons, double progress) {
        buttons.get(1).setItem(setCustomModelData(buttons.get(1).getItem(), 100004));

        for (int i = 2; i < 5; i++) {
            buttons.get(i).setItem(setCustomModelData(buttons.get(i).getItem(), 100009));
        }

        int progressChunk = (int) progress / 3;
        buttons.get(5).setItem(setCustomModelData(buttons.get(5).getItem(), 100005 + progressChunk));
        buttons.get(6).setItem(setCustomModelData(buttons.get(6).getItem(), 100005));
        buttons.get(7).setItem(setCustomModelData(buttons.get(7).getItem(), 100010));
    }

    private static void modifyStatButton5(Map<Integer, GuiItem> buttons, double progress) {
        buttons.get(1).setItem(setCustomModelData(buttons.get(1).getItem(), 100004));

        for (int i = 2; i < 6; i++) {
            buttons.get(i).setItem(setCustomModelData(buttons.get(i).getItem(), 100009));
        }

        int progressChunk = (int) progress / 3;
        buttons.get(6).setItem(setCustomModelData(buttons.get(6).getItem(), 100005 + progressChunk));
        buttons.get(7).setItem(setCustomModelData(buttons.get(7).getItem(), 100010));
    }

    private static void modifyStatButton6(Map<Integer, GuiItem> buttons, double progress) {
        buttons.get(1).setItem(setCustomModelData(buttons.get(1).getItem(), 100004));

        for (int i = 2; i < 7; i++) {
            buttons.get(i).setItem(setCustomModelData(buttons.get(i).getItem(), 100009));
        }

        int progressChunk = (int) progress / 2;
        buttons.get(7).setItem(setCustomModelData(buttons.get(7).getItem(), 100010 + progressChunk));
    }

    private static ItemStack setCustomModelData(ItemStack item, int modelData) {
        if (item == null || item.getType() == Material.AIR) {
            return item;
        }

        ItemStack clonedItem = item.clone();
        ItemMeta meta = clonedItem.getItemMeta();

        if (meta != null) {
            meta.setCustomModelData(modelData);
            clonedItem.setItemMeta(meta);
        }

        return clonedItem;
    }
}

package asia.virtualmc.vLibrary.utilities.core.items;

import asia.virtualmc.vLibrary.utilities.files.YAMLUtils;
import asia.virtualmc.vLibrary.utilities.items.LoreUtils;
import asia.virtualmc.vLibrary.utilities.items.MetaUtils;
import asia.virtualmc.vLibrary.utilities.items.PDCUtils;
import asia.virtualmc.vLibrary.utilities.messages.ConsoleUtils;
import asia.virtualmc.vLibrary.utilities.text.TextUtils;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ResourceUtils {

    public static class Rarity {
        public String tag;
        public String color;
        public double exp;
        public double weight;
        public double price;

        public Rarity(String tag, String color, double exp, double weight, double price) {
            this.tag = tag;
            this.color = color;
            this.exp = exp;
            this.weight = weight;
            this.price = price;
        }
    }

    public static Map<String, ItemStack> load(@NotNull Plugin plugin,
                                              @NotNull String dirPath,
                                              @NotNull NamespacedKey resourceKey,
                                              Map<Integer, Rarity> rarityMap) {

        Map<String, ItemStack> cache = new LinkedHashMap<>();
        int itemID = 1;

        List<YamlDocument> files = YAMLUtils.getFiles(plugin, dirPath);
        if (files.isEmpty()) {
            ConsoleUtils.severe("[" + plugin.getName() + "] ", dirPath + " is empty! Skipping resource creation..");
            return null;
        }

        YamlDocument settings = YAMLUtils.getYaml(plugin, "resources/settings.yml");
        if (settings == null) {
            ConsoleUtils.severe("[" + plugin.getName() + "] ", "settings.yml is empty! Skipping resource creation..");
            return null;
        }

        String materialName = settings.getString("material");
        Material material;
        try {
            material = Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().severe("Invalid material: " + materialName + " defaulting to PAPER.");
            material = Material.PAPER;
        }

        for (YamlDocument yaml : files) {
            Section resourceSection = yaml.getSection("itemsList");
            if (resourceSection == null) {
                ConsoleUtils.severe("[" + plugin.getName() + "] ", "Looks like " + yaml.getNameAsString() + " is empty. Skipping resource creation..");
                return null;
            }

            Set<String> keys = new HashSet<>(resourceSection.getRoutesAsStrings(false));
            int modelData = settings.getInt("starting-model-data");
            int limit = settings.getInt("lore-settings.character-limit");
            boolean limitLore = settings.getBoolean("lore-settings.auto-format");
            Map<Integer, String> regions = YAMLUtils.getStringMap(plugin, settings, "region-names");

            for (String key : keys) {
                String path = "itemsList." + key;
                String name = TextUtils.format(key);
                Map<String, Integer> intMap = ItemCoreUtils.getInt(yaml, path);
                Map<String, Double> doubleMap = ItemCoreUtils.getDouble(yaml, path);

                // Lore Creation
                List<String> lore = new ArrayList<>();
                lore.add(rarityMap.get(intMap.get("rarity_id")).tag);
                if (limitLore) {
                    lore.addAll(LoreUtils.applyCharLimit(yaml.getStringList(path + ".lore"), limit));
                } else {
                    lore.add(yaml.getString(path + ".lore"));
                }
                if (!regions.isEmpty()) {
                    lore.add("");
                    lore.add(regions.get(intMap.get("region_id")));
                }

                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();

                if (meta != null) {
                    MetaUtils.setDisplayName(meta, rarityMap.get(intMap.get("rarity_id")).color + name);
                    MetaUtils.setLore(meta, lore);
                    MetaUtils.setCustomModelData(meta, modelData);

                    // Apply PDC Data
                    PDCUtils.addInteger(meta, resourceKey, itemID);

                    for (Map.Entry<String, Integer> entry : intMap.entrySet()) {
                        NamespacedKey namespacedKey = new NamespacedKey(plugin, entry.getKey().replace("-", "_"));
                        PDCUtils.addInteger(meta, namespacedKey, entry.getValue());
                    }

                    for (Map.Entry<String, Double> entry : doubleMap.entrySet()) {
                        NamespacedKey namespacedKey = new NamespacedKey(plugin, entry.getKey().replace("-", "_"));
                        PDCUtils.addDouble(meta, namespacedKey, entry.getValue());
                    }

                    item.setItemMeta(meta);
                    cache.put(name + "_" + intMap.get("rarity_id"), item.clone());
                    itemID++;
                }
            }
        }

        return cache;
    }

    public static Map<Integer, Rarity> loadRarities(@NotNull Plugin plugin,
                                                    @NotNull String fileName) {

        Map<Integer, Rarity> cache = new LinkedHashMap<>();
        List<String> rarities = Arrays.asList("common", "uncommon", "rare", "unique", "epic", "mythical", "exotic");

        YamlDocument yaml = YAMLUtils.getYaml(plugin, fileName);
        if (yaml == null) {
            ConsoleUtils.severe("[" + plugin.getName() + "] ", "Couldn't find " + fileName + ". Skipping resource creation..");
            return null;
        }

        cache.put(0, new Rarity("None", "None", yaml.getDouble("exp.none"), 0, 0));
        int rarityID = 1;

        for (String rarityName : rarities) {
            String tag = yaml.getString("rarity-tags." + rarityName);
            String color = yaml.getString("rarity-color." + rarityName);
            double exp = yaml.getDouble("exp." + rarityName);
            double weight = yaml.getDouble("weight." + rarityName);
            double price = yaml.getDouble("price." + rarityName);

            cache.put(rarityID, new Rarity(tag, color, exp, weight, price));
            rarityID++;
        }

        ConsoleUtils.info("Loaded " + cache.size() + " rarities from settings.yml");
        return cache;
    }
}

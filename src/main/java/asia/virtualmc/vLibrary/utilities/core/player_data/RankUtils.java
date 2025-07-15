package asia.virtualmc.vLibrary.utilities.core.player_data;

import asia.virtualmc.vLibrary.utilities.files.YAMLUtils;
import asia.virtualmc.vLibrary.utilities.messages.ConsoleUtils;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class RankUtils {

    public static class Rank {
        public String name;
        public double points;

        public Rank(String name, double points) {
            this.name = name;
            this.points = points;
        }
    }

    public static void load(@NotNull Plugin plugin,
                            @NotNull String fileName,
                            Map<InnateTraitUtils, Rank> points,
                            Map<String, Double> values) {

        YamlDocument yaml = YAMLUtils.getYaml(plugin, fileName);

        if (yaml == null) {
            ConsoleUtils.severe(plugin.getName(), "Couldn't find " + fileName + ". Skipping rank creation..");
            return;
        }

        Section settings = YAMLUtils.getSection(yaml, "settings");
        if (settings == null) {
            ConsoleUtils.severe(plugin.getName(), "Looks like " + fileName + " is empty. Skipping rank creation..");
            return;
        }



//        Set<String> keys = section.getRoutesAsStrings(false);
//        for (String traitName : keys) {
//            String path = "traitList." + traitName + ".";
//
//            String materialName = yaml.getString(path + "material");
//            String displayName = yaml.getString(path + "name");
//            List<String> lore = yaml.getStringList(path + "lore");
//
//            int modelData = yaml.getInt(path + "custom_model_data");
//            int slot = yaml.getInt(path + "slot");
//            Map<String, Double> values = YAMLUtils.getMap(plugin, fileName, path + "effects");
//
//            if (materialName == null || displayName == null) {
//                ConsoleUtils.severe(plugin.getName(), "Invalid configuration for trait name: " + traitName);
//                continue;
//            }
//
//            ItemStack item;
//            try {
//                Material material = Material.valueOf(materialName.toUpperCase());
//                item = new ItemStack(material);
//            } catch (IllegalArgumentException e) {
//                ConsoleUtils.severe(plugin.getName(), "Invalid material '" + materialName + "' for trait name: " + traitName);
//                continue;
//            }
//
//            ItemMeta meta = item.getItemMeta();
//            if (meta == null) {
//                ConsoleUtils.severe("Could not retrieve item meta for trait name: " + traitName);
//                continue;
//            }
//
//            MetaUtils.setDisplayName(meta, displayName);
//            MetaUtils.setCustomModelData(meta, modelData);
//            MetaUtils.setLore(meta, lore);
//
//            item.setItemMeta(meta);
//            traitCache.put(traitName, new InnateTraitUtils.InnateTrait(traitName, slot, item.clone(), values));
//        }
//
//        return traitCache;
    }
}

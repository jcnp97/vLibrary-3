package asia.virtualmc.vLibrary.utilities.core.guis;

import asia.virtualmc.vLibrary.VLibrary;
import asia.virtualmc.vLibrary.utilities.files.YAMLUtils;
import asia.virtualmc.vLibrary.utilities.items.ItemStackUtils;
import asia.virtualmc.vLibrary.utilities.messages.ConsoleUtils;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GUIConfig {
    private static ItemStack invisibleItem;
    private static final Map<String, String> guiUnicodes = new HashMap<>();

    public static void initialize() {
        YamlDocument yaml = YAMLUtils.getYaml(VLibrary.getInstance(), "skills-core/default-gui.yml");
        if (yaml == null) {
            ConsoleUtils.severe("Couldn't find skills-core/default-gui.yml or it was empty!");
            return;
        }

        int modelData = yaml.getInt("invisible-item.model-data");
        String materialName = yaml.getString("invisible-item.material");
        Material material = Material.valueOf(materialName);
        ItemStack item = ItemStackUtils.create(material, modelData);
        if (item != null) {
            invisibleItem = item.clone();
        }

        Section section = YAMLUtils.getSection(yaml, "guiTitles");
        Set<String> keys = section.getRoutesAsStrings(false);
        for (String key : keys) {
            String unicode = section.getString(key);
            guiUnicodes.put(key, unicode);
        }
    }

    public static ItemStack getItem() {
        if (invisibleItem == null) {
            initialize();
        }

        return invisibleItem.clone();
    }

    public static String get(String title) {
        if (guiUnicodes.isEmpty()) {
            initialize();
        }

        return guiUnicodes.get(title);
    }
}

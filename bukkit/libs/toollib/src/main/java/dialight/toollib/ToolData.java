package dialight.toollib;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.ListIterator;

public class ToolData {

    private final String prefix;
    private final int limit;

    public ToolData(String prefix, int limit) {
        this.prefix = prefix;
        this.limit = limit;
    }

    @Nullable public String parse(ItemStack item) {
        if(item == null) return null;
        if(!item.hasItemMeta()) return null;

        ItemMeta im = item.getItemMeta();
        if(!im.hasLore()) return null;
        List<String> lore = im.getLore();
        if (lore == null) return null;
        if (lore.isEmpty()) return null;
        ListIterator<String> it = lore.listIterator(lore.size());
        int i = 0;
        while (it.hasPrevious() && i++ < limit) {
            String line = it.previous();
            if(!line.startsWith(prefix)) continue;
            return line.substring(prefix.length());
        }
        return null;
    }

    public String build(String data) {
        return prefix + data;
    }
}

package dialight.toollib;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.toollib.events.ToolInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Tool {

    private static final String prefix = Colorizer.apply("|dgr|Tool ID: |gr|");
    private static final Pattern pattern = Pattern.compile("^" + prefix + "(.*)$");

    @Nullable public static String parseId(ItemStack item) {
        if(item == null) return null;
        if(!item.hasItemMeta()) return null;

        ItemMeta im = item.getItemMeta();
        if(!im.hasLore()) return null;
        List<String> lore = im.getLore();
        if (lore == null) return null;
        if (lore.isEmpty()) return null;

        return parseId(lore);
    }
    @Nullable public static String parseId(List<String> lore) {
        if(lore.isEmpty()) return null;
        String last = lore.get(lore.size() - 1);
        Matcher matcher = pattern.matcher(last);
        if(!matcher.matches()) return null;
        return matcher.group(1);
    }

    private final String id;

    public Tool(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public ItemStack applyId(ItemStack item) {
        return new ItemStackBuilder(item)
                .addLore(prefix + id)
                .build();
    }

    public abstract void onClick(ToolInteractEvent event);

    public abstract ItemStack createItem();

}

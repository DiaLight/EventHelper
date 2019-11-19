package dialight.toollib;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.toollib.events.ToolInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class Tool {

    private static final ToolData ID_DATA = new ToolData(Colorizer.apply("|dgr|Tool ID: |gr|"), 1);

    @Nullable public static String parseId(ItemStack item) {
        return ID_DATA.parse(item);
    }

    private final String id;

    public Tool(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void applyId(ItemStackBuilder isb) {
        isb.addLore(ID_DATA.build(id));
    }

    public abstract void onClick(ToolInteractEvent e);

    public abstract ItemStack createItem();

}

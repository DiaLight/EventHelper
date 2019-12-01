package dialight.maingui;

import dialight.compatibility.PlayerInventoryBc;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.toollib.Tool;
import dialight.toollib.events.ToolInteractEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MainGuiTool extends Tool {

    public static final String ID = "maingui";
    private final MainGuiProject proj;
    private final ItemStack item;

    public MainGuiTool(MainGuiProject proj) {
        super(ID);
        this.proj = proj;
        item = new ItemStackBuilder(Material.EMERALD)
                .displayName(Colorizer.apply("|a|Вещь всея Майнкрафта"))
                .lore(Colorizer.asList(
                        "|a|ПКМ|y|: Открыть «Инвентарь EventHelper»",
                        "|a|Shift|y|+|a|ЛКМ|y|: Открыть ранее открытый",
                        "|y| инентарь",
                        "|y|Аналог: |g|/eh"
                ))
                .let(this::applyId)
                .build();
    }

    @Override public void onClick(ToolInteractEvent e) {
        switch (e.getAction()) {
            case LEFT_CLICK: if(!e.isSneaking()) {

            } else {

            } break;
            case RIGHT_CLICK: if(!e.isSneaking()) {
                proj.getGuilib().clearStory(e.getPlayer());
                proj.getGuilib().openGui(e.getPlayer(), proj.getGui());
            } else {
                if(proj.getGuilib().openLast(e.getPlayer()) == null) {
                    proj.getGuilib().openGui(e.getPlayer(), proj.getGui());
                }
            } break;
            case DROP: {
                PlayerInventoryBc.of(e.getPlayer().getInventory()).setItemInMainHand(null);
                proj.getGuilib().clearStory(e.getPlayer());
            } break;
        }
    }

    @Override public ItemStack createItem() {
        return item;
    }

}

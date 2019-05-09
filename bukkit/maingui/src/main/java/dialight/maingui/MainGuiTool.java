package dialight.maingui;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.nms.ItemStackNms;
import dialight.toollib.Tool;
import dialight.toollib.events.ToolInteractEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MainGuiTool extends Tool {

    public static final String ID = "maingui";
    private final MainGuiProject proj;

    public MainGuiTool(MainGuiProject proj) {
        super(ID);
        this.proj = proj;
    }

    @Override
    public void onClick(ToolInteractEvent event) {
        switch (event.getAction()) {
            case LEFT_CLICK: if(!event.isSneaking()) {

            } else {
                ItemStack is = event.getPlayer().getInventory().getItem(8);
                if(is != null && is.getType() != Material.AIR) {
                    ItemStackNms.dump(is);
                }
            } break;
            case RIGHT_CLICK: if(!event.isSneaking()) {
                proj.getGuilib().clearStory(event.getPlayer());
                proj.getGuilib().openGui(event.getPlayer(), proj.getGui());
            } else {
                if(proj.getGuilib().openLast(event.getPlayer()) != null) {
                    proj.getGuilib().openGui(event.getPlayer(), proj.getGui());
                }
            } break;
        }
    }

    @Override
    public ItemStack createItem() {
        return new ItemStackBuilder(Material.EMERALD)
                .displayName(Colorizer.apply("|a|Вещь всея Майнкрафта"))
                .lore(Colorizer.asList(
                        "|a|ПКМ|y|: Открыть «Инвентарь EventHelper»",
                        "|a|Shift|y|+|a|ПКМ|y|: Открыть ранее открытый",
                        "|y| инентарь",
                        "|y|Аналог: |g|/eh"
                ))
                .build();
    }

}

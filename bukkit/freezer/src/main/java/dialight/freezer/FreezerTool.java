package dialight.freezer;

import dialight.compatibility.PlayerInventoryBc;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.maingui.MainGuiTool;
import dialight.toollib.Tool;
import dialight.toollib.events.ToolInteractEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

public class FreezerTool extends Tool {

    public static final String ID = "freezer";
    private final Freezer proj;
    private final ItemStack item;

    public FreezerTool(Freezer proj) {
        super(ID);
        this.proj = proj;
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        this.item = new ItemStackBuilder(Material.ICE)
                .displayName(Colorizer.apply("|a|Замораживатель игроков"))
                .lore(Colorizer.asList(
                        "|w|Заморозка",
                        "|a|ЛКМ по игроку|y|: заморозить/разморозить игрока",
                        "|w|Выбор игроков",
                        "|a|ПКМ|y|: открыть замораживатель",
                        "|a|Shift|y|+|a|ПКМ|y|: добавить замороженных",
                        "|y| в список телепортируемых",
                        "",
                        "|g|Плагин: |y|Замораживатель",
                        "|g|Версия: |y|v" + desc.getVersion()
                ))
                .let(this::applyId)
                .build();
    }

    @Override
    public void onClick(ToolInteractEvent e) {
        switch (e.getAction()) {
            case LEFT_CLICK: if(!e.isSneaking()) {

            } else {

            } break;
            case RIGHT_CLICK: if(!e.isSneaking()) {

            } else {

            } break;
            case DROP: {
                if(proj.getMaingui() != null) {
                    MainGuiTool tool = proj.getToollib().get(MainGuiTool.class);
                    if(tool != null) {
                        PlayerInventoryBc.of(e.getPlayer().getInventory()).setItemInMainHand(tool.createItem());
                    }
                }
                proj.getGuilib().clearStory(e.getPlayer());
            } break;
        }
    }

    @Override public ItemStack createItem() {
        return item;
    }

}

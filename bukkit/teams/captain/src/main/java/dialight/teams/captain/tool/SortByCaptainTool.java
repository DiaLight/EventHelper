package dialight.teams.captain.tool;

import dialight.compatibility.PlayerInventoryBc;
import dialight.misc.ActionInvoker;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.maingui.MainGuiTool;
import dialight.misc.player.UuidPlayer;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.observable.ObservableTeam;
import dialight.toollib.Tool;
import dialight.toollib.events.ToolInteractEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

public class SortByCaptainTool extends Tool {

    public static final String ID = "captain";

    private final SortByCaptain proj;
    private final ItemStack item;

    private final SelectMemberTool teamTool;

    public SortByCaptainTool(SortByCaptain proj) {
        super(ID);
        this.proj = proj;
        teamTool = new SelectMemberTool(proj);
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        this.item = new ItemStackBuilder(Material.BEACON)
                .displayName(Colorizer.apply("|a|Мастер сортировки по капитанам"))
                .addLore(Colorizer.apply(
//                        "|a|ЛКМ|y|: запустить распределение по капитанам",
                        "|a|Shift|y|+|a|ЛКМ|y|: насильно остановить распределение",
                        "|a|ПКМ|y|: открыть гуи",
//                "|a|Shift|y|+|a|ПКМ|y|: что-то еще",
//                        "|g|state: |y|" + proj.getStateEngine().getHandler().getValue().getState(),
                        ""
                ))
                .addLore(proj.getItemSuffix())
                .let(this::applyId)
                .build();
    }

    @Override public void onClick(ToolInteractEvent e) {
        String teamName = teamTool.parse(e.getItem());
        if(teamName != null) {
            teamTool.onClick(teamName, e);
            return;
        }
        switch (e.getAction()) {
            case LEFT_CLICK: if(!e.isSneaking()) {

            } else {
                UuidPlayer up = proj.getOfflineLib().getUuidPlayer(e.getPlayer());
                proj.getStateEngine().kill(new ActionInvoker(up));
            } break;
            case RIGHT_CLICK: if(!e.isSneaking()) {
                proj.getGuilib().openGui(e.getPlayer(), proj.getControlGui());
            } else {

            } break;
            case DROP: {
                MainGuiTool tool = proj.getToollib().get(MainGuiTool.class);
                if(tool != null) {
                    PlayerInventoryBc.of(e.getPlayer().getInventory()).setItemInMainHand(tool.createItem());
                }
                proj.getGuilib().clearStory(e.getPlayer());
            } break;
        }
    }

    @Override public ItemStack createItem() {
        return item;
    }

    public ItemStack createItem(ObservableTeam team) {
        return new ItemStackBuilder(teamTool.createItem(team))
                .let(this::applyId)
                .build();
    }

}

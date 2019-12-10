package dialight.teams.captain.tool;

import dialight.compatibility.BannerMetaBc;
import dialight.compatibility.ItemStackBuilderBc;
import dialight.compatibility.PlayerInventoryBc;
import dialight.misc.ActionInvoker;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.misc.player.UuidPlayer;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.observable.ObservableTeam;
import dialight.toollib.SubTool;
import dialight.toollib.ToolData;
import dialight.toollib.events.ToolInteractEvent;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

public class SelectMemberTool extends SubTool {

    private static final ToolData TEAM_DATA = new ToolData(Colorizer.apply("|dgr|Team name: |gr|"), 2);
    private final SortByCaptain proj;
    private final ItemStack item;


    public SelectMemberTool(SortByCaptain proj) {
        this.proj = proj;
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        this.item = new ItemStackBuilder()
                .let(isb -> {
                    ItemStackBuilderBc.of(isb).banner(DyeColor.WHITE);
                })
                .lore(Colorizer.asList(
                        "|a|ЛКМ по игроку|y|: выбрать игрока",
                        "|a|Shift|y|+|a|ЛКМ по игроку|y|: выбрать игрока и подтвердить",
                        "|a|ПКМ по игроку|y|: выбрать игрока",
                        "|a|ПКМ по воздуху|y|: открыть гуи выбора игрока",
                        "|a|Shift|y|+|a|ПКМ|y|: открыть гуи выбора игрока",
                        "|a|Q|y|: подтвердить выбор",
                        "",
                        "|g|Плагин: |y|Сортировка по капитанам",
                        "|g|Версия: |y|v" + desc.getVersion()
                ))
                .hideMiscellaneous(true)
                .build();
    }


    public String parse(ItemStack item) {
        return TEAM_DATA.parse(item);
    }

    public void onClick(String teamName, ToolInteractEvent e) {
        ObservableTeam team = proj.getScoreboard().teamsByName().get(teamName);
        if(team == null) {
            PlayerInventoryBc.of(e.getPlayer().getInventory()).setItemInMainHand(null);
            return;
        }
        ActionInvoker invoker = new ActionInvoker(proj.getOfflineLib().getUuidPlayer(e.getPlayer()));
        switch (e.getAction()) {
            case LEFT_CLICK: if(!e.isSneaking()) {
                UuidPlayer value = proj.getMemberHandler().getLookingAt().getValue();
                proj.select(invoker, value);
            } else {
                UuidPlayer value = proj.getMemberHandler().getLookingAt().getValue();
                proj.selectAndConfirm(invoker, value);
            } break;
            case RIGHT_CLICK: if(!e.isSneaking()) {
                UuidPlayer value = proj.getMemberHandler().getLookingAt().getValue();
                if(value != null) {
                    proj.select(invoker, value);
                } else {
                    proj.runTask(() -> {
                        proj.getGuilib().openGui(e.getPlayer(), proj.getSelectMemberGui());
                    });
                }
            } else {
                proj.runTask(() -> {
                    proj.getGuilib().openGui(e.getPlayer(), proj.getSelectMemberGui());
                });
            } break;
            case DROP:
                proj.runTask(() -> {
                    proj.getMemberHandler().tryConfirm();
                });
                e.setCancelled(true);
                break;
        }
    }

    public ItemStack createItem(ObservableTeam team) {
        return new ItemStackBuilder(item.clone())
                .displayName(Colorizer.apply("|a|Команда " + team.getName()))
                .bannerMeta(bm -> {
                    BannerMetaBc.of(bm).setBaseColor(team.getDyeColor());
                })
                .addLore(TEAM_DATA.build(team.getName()))
                .build();
    }

}

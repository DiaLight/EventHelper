package dialight.teams;

import dialight.compatibility.BannerMetaBc;
import dialight.compatibility.ItemStackBuilderBc;
import dialight.compatibility.PlayerInventoryBc;
import dialight.compatibility.TeamBc;
import dialight.extensions.ColorConverter;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.gui.Gui;
import dialight.toollib.SubTool;
import dialight.toollib.ToolData;
import dialight.toollib.events.ToolInteractEvent;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamTool extends SubTool {

    private static final ToolData TEAM_DATA = new ToolData(Colorizer.apply("|dgr|Team name: |gr|"), 2);
    private final Teams proj;
    private final ItemStack item;
    private final Scoreboard scoreboard;

    public TeamTool(Teams proj) {
        this.proj = proj;
        this.scoreboard = proj.getPlugin().getServer().getScoreboardManager().getMainScoreboard();
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        this.item = new ItemStackBuilder()
                .let(isb -> {
                    ItemStackBuilderBc.of(isb).banner(DyeColor.WHITE);
                })
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: добавить/убрать игрока из команды",
                        "|a|ПКМ|y|: открыть редактор команды",
                        "|a|Shift|y|+|a|ПКМ|y|: убрать выделение команды",
                        "",
                        "|g|Плагин: |y|Распределитель команд",
                        "|g|Версия: |y|v" + desc.getVersion()
                ))
                .hideMiscellaneous(true)
                .build();
    }

    public String parse(ItemStack item) {
        return TEAM_DATA.parse(item);
    }

    public void onClick(String teamName, ToolInteractEvent e) {
        Team team = this.scoreboard.getTeam(teamName);
        if(team == null) {
            PlayerInventoryBc.of(e.getPlayer().getInventory()).setItemInMainHand(proj.getTool().createItem());
            return;
        }
        switch (e.getAction()) {
            case LEFT_CLICK: if(!e.isSneaking()) {

            } else {

            } break;
            case RIGHT_CLICK: if(!e.isSneaking()) {
                Gui teamGui = proj.getGui().teamGui(team.getName());
                if(teamGui != null) {
                    proj.getGuilib().openGui(e.getPlayer(), teamGui);
                }
            } else {

            } break;
            case DROP:
                PlayerInventoryBc.of(e.getPlayer().getInventory()).setItemInMainHand(proj.getTool().createItem());
                break;
        }
    }

    public ItemStack createItem(Team team) {
        return new ItemStackBuilder(item)
                .displayName(Colorizer.apply("|a|Команда " + team.getName()))
                .bannerMeta(bm -> {
                    DyeColor color = ColorConverter.toWoolColor(TeamBc.of(team).getColor());
                    BannerMetaBc.of(bm).setBaseColor(color);
                })
                .addLore(TEAM_DATA.build(team.getName()))
                .build();
    }

}

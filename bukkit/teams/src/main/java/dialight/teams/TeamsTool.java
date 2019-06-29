package dialight.teams;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.compatibility.PlayerInventoryBc;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.maingui.MainGuiTool;
import dialight.toollib.Tool;
import dialight.toollib.events.ToolInteractEvent;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scoreboard.Team;

public class TeamsTool extends Tool {

    public static final String ID = "teams";

    private final Teams proj;
    private final ItemStack item;

    private final TeamTool teamTool;

    public TeamsTool(Teams proj) {
        super(ID);
        this.proj = proj;
        teamTool = new TeamTool(proj);
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        this.item = new ItemStackBuilder()
                .let(isb -> {
                    ItemStackBuilderBc.of(isb).banner(DyeColor.WHITE);
                })
                .displayName(Colorizer.apply("|a|Команды"))
                .lore(Colorizer.asList(
                        "|a|ПКМ|y|: открыть редактор команд",
                        ""
                ))
                .addLore(proj.getItemSuffix())
                .nbt("{BlockEntityTag:{Patterns:[" +
                        "{Color:11,Pattern:\"vhr\"}," +
                        "{Color:10,Pattern:\"vh\"}," +
                        "{Color:14,Pattern:\"tr\"}," +
                        "{Color:12,Pattern:\"bl\"}," +
                        "{Color:5,Pattern:\"br\"}," +
                        "{Color:1,Pattern:\"tl\"}" +
                        "]}}")
                .hideMiscellaneous(true)
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

            } break;
            case RIGHT_CLICK: if(!e.isSneaking()) {
                proj.getGuilib().openGui(e.getPlayer(), proj.getGui());
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

    public ItemStack createItem(Team team) {
        return new ItemStackBuilder(teamTool.createItem(team))
                .let(this::applyId)
                .build();
    }

}

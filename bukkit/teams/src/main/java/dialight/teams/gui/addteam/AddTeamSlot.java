package dialight.teams.gui.addteam;

import dialight.compatibility.TeamBc;
import dialight.extensions.ColorConverter;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.teams.Teams;
import dialight.teams.TeamsMessages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class AddTeamSlot implements Slot {

    private final ChatColor color;
    private final Teams proj;
    private final Collection<ChatColor> coloredTeams;
    @NotNull private final Scoreboard scoreboard;
    private final String name;

    public AddTeamSlot(ChatColor color, Teams proj, Collection<ChatColor> coloredTeams) {
        this.color = color;
        this.proj = proj;
        this.coloredTeams = coloredTeams;
        this.scoreboard = proj.getPlugin().getServer().getScoreboardManager().getMainScoreboard();
        String team_name = color.name().toLowerCase();
        if (team_name.length() > 16) {
            team_name = team_name.substring(0, 16);
        }
        this.name = team_name;
    }

    @Override public void onClick(SlotClickEvent e) {
        if (coloredTeams.contains(color)) {
            e.getPlayer().sendMessage(TeamsMessages.thisColorAlreadyInUse);
            return;
        }
        Team team = scoreboard.getTeam(name);
        if (team != null) {
            e.getPlayer().sendMessage(TeamsMessages.thisNameAlreadyInUse);
            return;
        }
        proj.getTeamWhiteList().add(name);
        team = scoreboard.registerNewTeam(name);
        TeamBc.of(team).setColor(color);
        team.setSuffix(ChatColor.RESET.toString());
        e.getPlayer().sendMessage(TeamsMessages.addTeam(team));
    }

    @NotNull @Override public ItemStack createItem() {
        Material material;
        if (coloredTeams.contains(color)) {
            material = Material.LEATHER_BOOTS;
        } else {
            material = Material.LEATHER_CHESTPLATE;
        }
        ItemStackBuilder isb = new ItemStackBuilder(material);
        isb.leatherArmorColor(ColorConverter.toLeatherColor(color));
        isb.displayName(color + Colorizer.apply("⬛ |w|" + name));
        isb.hideAttributes(true);
        isb.hideMiscellaneous(true);
        if (coloredTeams.contains(color)) {
            isb.lore(Colorizer.asList(
                    "|r|Команда с таким цветом уже создана"
            ));
        } else {
            isb.lore(Colorizer.asList(
                    "|a|ЛКМ|y|: создать команду"
            ));
        }
        return isb.build();
    }

}

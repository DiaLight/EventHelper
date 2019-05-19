package dialight.teams.gui.addteam;

import dialight.compatibility.TeamBc;
import dialight.extensions.ColorConverter;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.indexcache.IndexCache;
import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import dialight.teams.TeamsMessages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AddTeamLayout extends CachedPageLayout<ChatColor> {

    private static final List<ChatColor> COLORS = Arrays.asList(
            ChatColor.WHITE,
            ChatColor.BLACK,
            ChatColor.DARK_GRAY,
            ChatColor.GRAY,
            ChatColor.DARK_RED,
            ChatColor.RED,
            ChatColor.GREEN,
            ChatColor.DARK_GREEN,
            ChatColor.BLUE,
            ChatColor.DARK_BLUE,
            ChatColor.YELLOW,
            ChatColor.GOLD,
            ChatColor.AQUA,
            ChatColor.DARK_AQUA,
            ChatColor.LIGHT_PURPLE,
            ChatColor.DARK_PURPLE
    );

    private class AddTeamSlot implements Slot {

        private final ChatColor color;

        public AddTeamSlot(ChatColor color) {
            this.color = color;
        }

        @Override public void onClick(SlotClickEvent e) {
            if(coloredTeams.contains(color)) {
                e.getPlayer().sendMessage(TeamsMessages.thisColorAlreadyInUse);
                return;
            }
            String team_name = color.name() + "_team";
            if(team_name.length() > 16) {
                team_name = team_name.substring(0, 16);
            }
            Team team = scoreboard.getTeam(team_name);
            if(team != null) {
                e.getPlayer().sendMessage(TeamsMessages.thisNameAlreadyInUse);
                return;
            }
            team = scoreboard.registerNewTeam(team_name);
            TeamBc.of(team).setColor(color);
            team.setSuffix(ChatColor.RESET.toString());
            e.getPlayer().sendMessage(TeamsMessages.addTeam(team));
            proj.getGuilib().openPrev(e.getPlayer());
        }

        @NotNull @Override public ItemStack createItem() {
            Material material;
            if(coloredTeams.contains(color)) {
                material = Material.LEATHER_BOOTS;
            } else {
                material = Material.LEATHER_CHESTPLATE;
            }
            ItemStackBuilder isb = new ItemStackBuilder(material);
            isb.leatherArmorColor(ColorConverter.toLeatherColor(color));
            isb.displayName(color.name());
            isb.hideAttributes(true);
            isb.hideMiscellaneous(true);
            if(coloredTeams.contains(color)) {
                isb.lore(Colorizer.asList(
                        "|y|Команда с таким цветом уже создана"
                ));
            } else {
                isb.lore(Colorizer.asList(
                        "|a|ЛКМ|y|: создать команду"
                ));
            }
            return isb.build();
        }

    }

    private final Set<ChatColor> coloredTeams = EnumSet.noneOf(ChatColor.class);
    @NotNull private final Teams proj;
    @NotNull private final Scoreboard scoreboard;

    public AddTeamLayout(Teams proj, IndexCache cache) {
        super(cache);
        this.proj = proj;
        this.scoreboard = proj.getPlugin().getServer().getScoreboardManager().getMainScoreboard();

        proj.onTeamUpdate(this::onTeamUpdate);
        this.setNameFunction(Enum::name);
        this.setSlotFunction(AddTeamSlot::new);

        coloredTeams.addAll(proj.getTeamsImmutable().stream()
                .map(oteam -> TeamBc.of(oteam.getTeam()).getColor())
                .collect(Collectors.toList()));

        COLORS.forEach(this::add);
    }

    private void onTeamUpdate(ObservableTeam oteam) {
//        coloredTeams.clear();
//        coloredTeams.addAll(proj.getTeamsImmutable().stream()
//                .map(ot -> TeamEx.of(ot.getTeam()).getChatColor())
//                .collect(Collectors.toList()));
    }

}

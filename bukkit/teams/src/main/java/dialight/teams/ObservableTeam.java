package dialight.teams;

import dialight.observable.collection.ObservableCollection;
import dialight.offlinelib.UuidPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.scoreboard.Team;

public interface ObservableTeam {

    ObservableCollection<UuidPlayer> getMembers();

    Team getTeam();

    void clearOfflines();

    String getName();

    ChatColor getColor();
    Color getLeatherColor();

}

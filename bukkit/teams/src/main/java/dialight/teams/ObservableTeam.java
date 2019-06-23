package dialight.teams;

import dialight.observable.collection.ObservableCollection;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Team;

public interface ObservableTeam {

    ObservableCollection<OfflinePlayer> getMembers();

    Team getTeam();

    void clear();

    String getName();

    ChatColor getColor();
    Color getLeatherColor();

}

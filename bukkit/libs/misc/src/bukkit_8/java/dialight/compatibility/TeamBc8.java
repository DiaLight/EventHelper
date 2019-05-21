package dialight.compatibility;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public class TeamBc8 extends TeamBc {

    public TeamBc8(Team team) {
        super(team);
    }

    @Override
    public ChatColor getColor() {
        ChatColor color = ChatColor.WHITE;
        String prefix = team.getPrefix();
        for (int i = prefix.length() - 1; i > -1; i--) {
            char c = prefix.charAt(i);
            ChatColor cur = ChatColor.getByChar(c);
            if (cur == null) continue;
            // Once we find a color or reset we can stop searching
            if (cur.isColor() || cur.equals(ChatColor.RESET)) {
                color = cur;
                break;
            }
        }
        return color;
    }

    @Override
    public void setColor(ChatColor color) {
        team.setPrefix(color.toString());
    }

}

package dialight.compatibility;

import dialight.misc.Colorizer;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public class TeamBc8 extends TeamBc {

    public TeamBc8(Team team) {
        super(team);
    }

    public static ChatColor parseColor(String prefix) {
        char lastChar = ' ';
        for (int i = prefix.length() - 1; i >= 0; i--) {
            char curChar = prefix.charAt(i);
            if(curChar == ChatColor.COLOR_CHAR) {
                ChatColor color = ChatColor.getByChar(lastChar);
                if (color != null) {
                    if (color.isColor() || color.equals(ChatColor.RESET)) return color;
                }
            }
            lastChar = curChar;
        }
        return ChatColor.WHITE;
    }
    public static String rstripColor(String prefix) {
        int endIndex = prefix.length();
        while(endIndex >= 2) {
            char colorChar = prefix.charAt(endIndex - 2);
            if(colorChar != ChatColor.COLOR_CHAR) break;
            endIndex -= 2;
        }
        return prefix.substring(0, endIndex);
    }

    public static void main(String[] args) {
        System.out.println(rstripColor(Colorizer.apply("|g|cool|y|: |r||gr|")));
    }
    @Override public ChatColor getColor() {
        return parseColor(team.getPrefix());
    }

    @Override
    public void setColor(ChatColor color) {
        team.setPrefix(color.toString());
    }

}

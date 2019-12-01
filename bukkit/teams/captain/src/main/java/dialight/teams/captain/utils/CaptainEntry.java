package dialight.teams.captain.utils;

import dialight.misc.player.UuidPlayer;

public class CaptainEntry {

    private final UuidPlayer player;
    private final String teamName;


    public CaptainEntry(UuidPlayer player, String teamName) {
        this.player = player;
        this.teamName = teamName;
    }

    public UuidPlayer getCaptain() {
        return player;
    }

    public String getTeamName() {
        return teamName;
    }

}

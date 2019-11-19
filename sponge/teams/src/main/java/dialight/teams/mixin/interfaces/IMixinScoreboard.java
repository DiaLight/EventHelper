package dialight.teams.mixin.interfaces;

import net.minecraft.scoreboard.ScorePlayerTeam;

import javax.annotation.Nullable;

public interface IMixinScoreboard {

    boolean removePlayerFromTeams(String playerName);

    @Nullable
    ScorePlayerTeam getPlayersTeam(String username);

}

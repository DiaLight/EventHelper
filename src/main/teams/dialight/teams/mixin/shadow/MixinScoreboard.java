package dialight.teams.mixin.shadow;

import dialight.teams.event.ScoreboardEvent;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.common.mixin.core.entity.player.MixinEntityPlayerMP;

@Mixin(Scoreboard.class)
public abstract class MixinScoreboard {

    @Shadow
    public abstract ScorePlayerTeam getTeam(String teamName);

    @Inject(method = "addPlayerToTeam", at = @At("RETURN"))
    protected  void onAddPlayer(String player, String newTeam, CallbackInfoReturnable<Boolean> ci) {
        if(!ci.getReturnValue()) return;
        Team scoreplayerteam = (Team) this.getTeam(newTeam);
        Event event = new ScoreboardEvent.TeamMember.Add(scoreplayerteam, player);
        Sponge.getEventManager().post(event);
    }

    @Inject(method = "removePlayerFromTeam", at = @At("RETURN"))
    protected void onRemovePlayer(String username, ScorePlayerTeam playerTeam, CallbackInfo ci) {
        Event event = new ScoreboardEvent.TeamMember.Remove((Team) playerTeam, username);
        Sponge.getEventManager().post(event);
    }

}

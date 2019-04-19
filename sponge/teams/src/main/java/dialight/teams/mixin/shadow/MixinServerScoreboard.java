package dialight.teams.mixin.shadow;

import dialight.teams.event.ScoreboardEvent;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ServerScoreboard;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerScoreboard.class)
public abstract class MixinServerScoreboard {

    @Inject(method = "broadcastTeamCreated", at = @At("HEAD"))
    protected  void onTeamCreated(ScorePlayerTeam playerTeam, CallbackInfo ci) {
        Event event = new ScoreboardEvent.Team.Add((Team) playerTeam);
        Sponge.getEventManager().post(event);
    }
    @Inject(method = "broadcastTeamInfoUpdate", at = @At("HEAD"))
    protected  void onTeamUpdate(ScorePlayerTeam playerTeam, CallbackInfo ci) {
        Event event = new ScoreboardEvent.Team.UpdateInfo((Team) playerTeam);
        Sponge.getEventManager().post(event);
    }
    @Inject(method = "broadcastTeamRemove", at = @At("HEAD"))
    protected  void onTeamRemove(ScorePlayerTeam playerTeam, CallbackInfo ci) {
        Event event = new ScoreboardEvent.Team.Remove((Team) playerTeam);
        Sponge.getEventManager().post(event);
    }

}

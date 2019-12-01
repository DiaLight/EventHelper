package dialight.teams.captain;

import dialight.fake.HotBarMessageFk;
import dialight.freezer.Frozen;
import dialight.misc.ActionInvoker;
import dialight.misc.Colorizer;
import dialight.misc.player.UuidPlayer;
import dialight.observable.map.ObservableMap;
import dialight.stateengine.StateEngine;
import dialight.teams.captain.state.SelectNextMemberHandler;
import dialight.teams.captain.utils.BlockData;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.material.Wool;

import java.util.*;

public class SortByCaptainHandler {

    private final SortByCaptain proj;
    private BlockData emptyBlock = new BlockData(Material.AIR);
    private BlockData unselectedBlock = new BlockData(Material.STAINED_GLASS);
    private BlockData captainBlock;
    private boolean isDay = true;
    private String title = Colorizer.apply(" ");

    public SortByCaptainHandler(SortByCaptain proj) {
        this.proj = proj;
    }


    public void setup() {
        // state engine control

        StateEngine<SortByCaptainState> stateEngine = proj.getStateEngine();
        stateEngine.onStart(this, () -> broadcast(CaptainMessages.initializing));
        stateEngine.onStop(this, () -> broadcast(CaptainMessages.finish));
        stateEngine.onKill(this, invoker -> broadcast(CaptainMessages.kill));
        stateEngine.onError(this, e -> {
            proj.getStateEngine().getInvoker().sendMessage(CaptainMessages.error(e.getMessage()));
        });
        stateEngine.onCleanup(this, this::onCleanup);
        stateEngine.getHandler().onChange(this, (oldHandler, newHandler) -> {
            switch (newHandler.getState()) {
                case NONE:
                    break;
                case COLLECT_MEMBERS:
                    broadcast(CaptainMessages.collectingMembers);
                    break;
                case BUILD_ARENA:
                    broadcast(CaptainMessages.arenaBuilding);
                    break;
                case NEXT_CAPTAIN:
                    break;
                case NEXT_MEMBER:
                    break;
            }
        });

        proj.getMembersHandler().onDone(this, this::onMembersCollected);

        proj.getArenaHandler().onDone(this, this::onArenaBuilt);

        proj.getCaptainHandler().onDone(this, this::onNextCaptainSelected);

        SelectNextMemberHandler memberHandler = proj.getMemberHandler();
        memberHandler.getDonePercent().onChange(this, this::onPercent);
        memberHandler.secondsLeft().onChange(this, this::onSeconds);
        memberHandler.getLookingAt().onChange(this, this::onActiveLookAt);
        memberHandler.getSelected().onChange(this, this::onSelect);
        memberHandler.getPause().onChange(this, this::onPause);
        memberHandler.onDone(this, this::onMemberConfirmed);
    }

    private void onMembersCollected() {
        proj.getStateEngine().setHandler(SortByCaptainState.BUILD_ARENA);
        proj.getMembersHandler().forEachMember(uuidPlayer -> {
            proj.getFakeBossBar().add(uuidPlayer.getUuid());
        });
        proj.getFakeBossBar().spawn(" ", 1f);
    }

    private void onArenaBuilt() {
        proj.getStateEngine().setHandler(SortByCaptainState.NEXT_CAPTAIN);
        ActionInvoker actionInvoker = new ActionInvoker(proj.getPlugin());
        List<Frozen> toFreeze = new ArrayList<>();
        proj.getMembersHandler().forEachMember(member -> {
            Location loc = proj.getArenaHandler().getMemberLocation(member);
            if(loc == null) throw new IllegalStateException();
            toFreeze.add(new Frozen(member, loc, actionInvoker, "sort by captain"));
        });
        proj.getFreezer().register(toFreeze);
    }

    private void onNextCaptainSelected() {
        proj.getStateEngine().setHandler(SortByCaptainState.NEXT_MEMBER);
        ObservableTeam team = proj.getCaptainHandler().getCurrentTeam().getValue();
        UuidPlayer uuidPlayer = proj.getCaptainHandler().getCurrentCaptain().getValue();
        captainBlock = new BlockData(Material.WOOL, new Wool(team.getDyeColor()));
        Player player = uuidPlayer.getPlayer();
        if(player != null) {
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1.0f, 1.0f);
        }
    }


    private void onSeconds(Integer oldSeconds, Integer newSeconds) {
        ObservableTeam team = proj.getCaptainHandler().getCurrentTeam().getValue();
        UuidPlayer player = proj.getCaptainHandler().getCurrentCaptain().getValue();
        title = "Выбирает " + team.color().getValue() + player.getName() + Colorizer.apply("|`|. Осталось |a|" + newSeconds + "|`| секунд");
        proj.getFakeBossBar().updateText(title);
    }
    private void onPercent(Integer oldPercent, Integer newPercent) {
        float percent = 1f - ((float) newPercent / 100);
        proj.getFakeBossBar().updateBar(title, percent);
        if(isDay) {
            // 0 - 12000
            proj.getNoneHandler().getWorld().setFullTime(12000 - (int) (12000 * newPercent / 100));
        } else {
            // 12000 - 24000
            proj.getNoneHandler().getWorld().setFullTime(24000 - (int) (12000 * newPercent / 100));
        }
    }

    private void onActiveLookAt(UuidPlayer oldPlayer, UuidPlayer newPlayer) {
        if(oldPlayer != null) {
            Location memberLocation = proj.getArenaHandler().getMemberLocation(oldPlayer);
            unselectedBlock.apply(memberLocation.clone().subtract(0, 1, 0));
        }
        if(newPlayer != null) {
            UuidPlayer value = proj.getCaptainHandler().getCurrentCaptain().getValue();
            Player captain = value.getPlayer();
            if(captain != null) {
                ObservableTeam team = proj.getCaptainHandler().getCurrentTeam().getValue();
                HotBarMessageFk.of(captain).sendMessage(Colorizer.apply("|*|") + team.color().getValue() + newPlayer.getName());
            }
            Location memberLocation = proj.getArenaHandler().getMemberLocation(newPlayer);
            captainBlock.apply(memberLocation.clone().subtract(0, 1, 0));
            Location location = newPlayer.getLocation();
            location.getWorld().playEffect(location.clone().add(0, 2, 0), Effect.SMOKE, 2, 2);
        }
    }

    private void onSelect(UuidPlayer oldTarget, UuidPlayer newTarget) {
        if(oldTarget != null) {
            Location memberLocation = proj.getArenaHandler().getMemberLocation(oldTarget);
            oldTarget.teleport(memberLocation);
        }
        Location selectedLocation = proj.getArenaHandler().getSelectedLocation();
        if(newTarget == null) {
            this.emptyBlock.apply(selectedLocation.clone().add(0, -1, 0));
        } else {
            newTarget.teleport(selectedLocation);
            this.captainBlock.apply(selectedLocation.clone().add(0, -1, 0));
            Player player = proj.getCaptainHandler().getCurrentCaptain().getValue().getPlayer();
            if(player != null) {
                player.sendMessage(CaptainMessages.nowConfirm(newTarget, proj.getCaptainHandler().getCurrentTeam().getValue().color().getValue()));
            }
        }
    }

    private void onPause(ActionInvoker invoker, Boolean fr, Boolean to) {
        switch (invoker.getType()) {
            case PLAYER:
                if(to) {
                    broadcast(CaptainMessages.paused(invoker));
                } else {
                    broadcast(CaptainMessages.unpaused(invoker));
                }
                break;
            case CONSOLE:
                break;
            case PLUGIN:
                break;
        }
    }

    private void onMemberConfirmed() {
        UuidPlayer captain = proj.getCaptainHandler().getCurrentCaptain().getValue();
        ObservableTeam team = proj.getCaptainHandler().getCurrentTeam().getValue();
        UuidPlayer selected = proj.getMemberHandler().getSelected().getValue();
        if(selected == null) {
            selected = proj.getMembersHandler().popRandomUnsorted();
            broadcast(CaptainMessages.captainRandomSelected(captain, selected, team.color().getValue()));
        } else {
            broadcast(CaptainMessages.captainSelected(captain, selected, team.color().getValue()));
            Location memberLocation = proj.getArenaHandler().getMemberLocation(selected);
            selected.teleport(memberLocation);
            Location selectedLocation = proj.getArenaHandler().getSelectedLocation();
            this.emptyBlock.apply(selectedLocation.clone().add(0, -1, 0));
        }
        team.getMembers().add(selected);
        Location memberLocation = proj.getArenaHandler().getMemberLocation(selected);
        proj.runTask(() -> {
            captainBlock.apply(memberLocation.clone().subtract(0, 1, 0));
        });
        proj.getMembersHandler().select(captain, selected);
        if(proj.getMembersHandler().getUnsorted().isEmpty()) {
            proj.getStateEngine().setHandler(SortByCaptainState.NONE);
        } else {
            proj.getStateEngine().setHandler(SortByCaptainState.NEXT_CAPTAIN);
        }
        isDay = !isDay;
    }

    private void onCleanup() {
        ActionInvoker actionInvoker = new ActionInvoker(proj.getPlugin());
        proj.getFakeBossBar().clear();
        proj.getFreezer().unregisterAll(actionInvoker);

        Map<String, TeamSortResult> results = new HashMap<>();
        for (Map.Entry<UuidPlayer, String> entry : proj.getMembersHandler().getResult().entrySet()) {
            String teamName = entry.getValue();
            TeamSortResult teamResult = results.get(teamName);
            if(teamResult == null) {
                UuidPlayer captain = proj.getMembersHandler().getCaptainMap().getCaptainByTeam(teamName);
                ObservableTeam team = proj.getScoreboard().teamsByName().get(teamName);
                Objects.requireNonNull(team);
                teamResult = new TeamSortResult(team, captain, new ArrayList<>());
                results.put(teamName, teamResult);
            }
            teamResult.getMembers().add(entry.getKey());
        }
        ObservableMap<String, TeamSortResult> oresults = proj.getSortResult();
        oresults.clear();
        for (Map.Entry<String, TeamSortResult> entry : results.entrySet()) {
            oresults.put(entry.getKey(), entry.getValue());
        }
    }

    public void broadcast(String msg) {
        proj.getPlugin().getServer().broadcastMessage(msg);
    }

}

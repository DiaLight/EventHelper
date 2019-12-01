package dialight.teams;

import dialight.observable.map.ObservableMap;
import dialight.observable.map.ValuesImmutableObservable;
import dialight.misc.player.UuidPlayer;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.UUID;

public class MembersObservable extends ValuesImmutableObservable<UUID, UuidPlayer> {

    private final Team team;

    public MembersObservable(Team team, ObservableMap<UUID, UuidPlayer> map) {
        super(map, UuidPlayer::getUuid);
        this.team = team;
    }

    @Override public boolean add(UuidPlayer element) {
        if(contains(element)) return false;
        String name = element.getName();
        if(name == null) return false;
        team.addEntry(name);
        return true;
    }

    @Override public boolean remove(Object element) {
        if(!contains(element)) return false;
        String name = ((UuidPlayer) element).getName();
        if(name == null) return false;
        team.removeEntry(name);
        return true;
    }

    @Override public void clear() {
        for (String name : new ArrayList<>(team.getEntries())) {
            team.removeEntry(name);
        }
    }

}

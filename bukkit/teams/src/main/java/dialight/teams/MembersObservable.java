package dialight.teams;

import dialight.observable.map.ObservableMap;
import dialight.observable.map.ValuesImmutableObservable;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.UUID;

public class MembersObservable extends ValuesImmutableObservable<UUID, OfflinePlayer> {

    private final Team team;

    public MembersObservable(Team team, ObservableMap<UUID, OfflinePlayer> map) {
        super(map, OfflinePlayer::getUniqueId);
        this.team = team;
    }

    @Override
    public boolean add(OfflinePlayer element) {
        if(contains(element)) return false;
        team.addEntry(element.getName());
        return true;
    }

    @Override
    public boolean remove(Object element) {
        if(!contains(element)) return false;
        team.removeEntry(((OfflinePlayer) element).getName());
        return true;
    }

    @Override
    public void clear() {
        for (String name : new ArrayList<>(team.getEntries())) {
            team.removeEntry(name);
        }
    }
}

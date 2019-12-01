package dialight.teams.captain.utils;

import dialight.misc.player.UuidPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CaptainMap implements Iterable<CaptainEntry> {

    private final List<CaptainEntry> list = new ArrayList<>();

    public UuidPlayer getCaptainByTeam(String teamName) {
        for (CaptainEntry entry : list) {
            if(!entry.getTeamName().equals(teamName)) continue;
            return entry.getCaptain();
        }
        return null;
    }

    @Nullable public String getTeamByCaptain(UuidPlayer captain) {
        for (int i = 0; i < list.size(); i++) {

        }
        for (CaptainEntry entry : list) {
            if(!entry.getCaptain().equals(captain)) continue;
            return entry.getTeamName();
        }
        return null;
    }

    public UuidPlayer getCaptain(int index) {
        return list.get(index).getCaptain();
    }

    public String getTeam(int index) {
        return list.get(index).getTeamName();
    }
    public int getCaptainIndex(UuidPlayer player) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCaptain().equals(player)) return i;
        }
        return -1;
    }

    public int getTeamIndex(String teamName) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTeamName().equals(teamName)) return i;
        }
        return -1;
    }

    public boolean containsCaptain(UuidPlayer captain) {
        return getTeamByCaptain(captain) != null;
    }

    public boolean containsTeam(String teamName) {
        return getCaptainByTeam(teamName) != null;
    }

    @Nullable public CaptainEntry put(UuidPlayer captain, String teamName) {
        int captainIndex = getCaptainIndex(captain);
        int teamIndex = getTeamIndex(teamName);
        if(captainIndex != -1) {
            if(teamIndex != -1) list.remove(teamIndex);
            return list.set(captainIndex, new CaptainEntry(captain, teamName));
        }
        if(teamIndex != -1) return list.set(teamIndex, new CaptainEntry(captain, teamName));
        list.add(new CaptainEntry(captain, teamName));
        return null;
    }

    public Iterable<UuidPlayer> captains() {
        return CaptainIterator::new;
    }
    public Iterable<String> teams() {
        return TeamIterator::new;
    }

    @NotNull @Override public Iterator<CaptainEntry> iterator() {
        return list.iterator();
    }

    public void clear() {
        list.clear();
    }

    public int size() {
        return list.size();
    }

    public CaptainEntry get(int index) {
        return list.get(index);
    }

    private class CaptainIterator implements Iterator<UuidPlayer> {

        private final Iterator<CaptainEntry> it = list.iterator();

        @Override public boolean hasNext() {
            return it.hasNext();
        }

        @Override public UuidPlayer next() {
            CaptainEntry next = it.next();
            if(next == null) return null;
            return next.getCaptain();
        }
    }
    private class TeamIterator implements Iterator<String> {

        private final Iterator<CaptainEntry> it = list.iterator();

        @Override public boolean hasNext() {
            return it.hasNext();
        }

        @Override public String next() {
            CaptainEntry next = it.next();
            if(next == null) return null;
            return next.getTeamName();
        }
    }

}

package dialight.teams.captain.state;

import dialight.extensions.LocationEx;
import dialight.extensions.ServerEx;
import dialight.misc.ActionInvoker;
import dialight.misc.player.UuidPlayer;
import dialight.observable.list.ObservableList;
import dialight.stateengine.StateEngineHandler;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.captain.SortByCaptainState;
import dialight.teams.captain.utils.*;
import dialight.teams.observable.ObservableScoreboard;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.Wool;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BuildArenaHandler extends StateEngineHandler<SortByCaptainState> {

    private final Set<Location> blocks = new HashSet<>();
    private final Map<UuidPlayer, Location> users = new HashMap<>();
    private Location active;
    private Location selected;

    public BuildArenaHandler(SortByCaptain proj) {
        super(proj, SortByCaptainState.BUILD_ARENA);
    }

    @Override public void enter() {
        // build scheme
        Schematic schematic = buildSchematic(
                proj.getMembersHandler().getCaptainMap(),
                proj.getMembersHandler().getUnsorted()
        );

        // search space to place building
        Location floc = searchSpaceToPlaceBuilding(resolveSearchLocation(), schematic);
        if(floc == null) throw new IllegalStateException("Can't find place for arena");

        // apply building
        applyBuilding(schematic, floc);
        fireDone();
    }

    @Override public void tick(int tick) {

    }

    @Override public void leave() {

    }

    public void clear() {
        for (Location loc : blocks) {
            loc.getBlock().setType(Material.AIR);
        }
        blocks.clear();
    }

    private Schematic buildSchematic(CaptainMap captains, ObservableList<UuidPlayer> members) {
        Schematic schematic = new Schematic();

        schematic.block(new Vector(0, -1, 0), new BlockData(Material.SEA_LANTERN));
        schematic.active = new Vector(0, 0, 0);
        CirclePointIterator<UuidPlayer> pointIterator = new CirclePointIterator<>(members, 1.6, 6.0);
        for (PointVector<UuidPlayer> vector : pointIterator) {
            if((vector.getIndex() % 2) != 0) {
                Vector ipos = vector.getLoc().clone().add(vector.getForward());
                ipos = new Vector(ipos.getBlockX(), ipos.getBlockY(), ipos.getBlockZ());
                schematic.block(ipos, new BlockData(Material.STAINED_GLASS));
                schematic.user(ipos.clone().add(new Vector(0, 1, 0)), vector.getValue());
            } else {
                Vector ipos = vector.getLoc().clone().subtract(vector.getUp());
                ipos = new Vector(ipos.getBlockX(), ipos.getBlockY(), ipos.getBlockZ());
                schematic.block(ipos, new BlockData(Material.STAINED_GLASS));
                schematic.user(ipos.clone().add(new Vector(0, 1, 0)), vector.getValue());
            }
        }

        ObservableScoreboard scoreboard = proj.getScoreboard();
        Vector pos = new Vector(-2, 1, -captains.size() + 1);
        for (CaptainEntry entry : captains) {
            ObservableTeam team = scoreboard.teamsByName().get(entry.getTeamName());
            if(team == null) throw new IllegalStateException("Team with name " + entry.getTeamName() + " not found");
            schematic.block(pos, new BlockData(Material.WOOL, new Wool(team.getDyeColor())));
            schematic.user(pos.clone().add(new Vector(0, 1, 0)), entry.getCaptain());
            pos = pos.clone().add(new Vector(0, 0, 2));
        }
        return schematic;
    }

    private void applyBuilding(Schematic schematic, Location floc) {
        for (Map.Entry<Vector, BlockData> entry : schematic.getBlocks().entrySet()) {
            Vector dl = entry.getKey();
            BlockData block = entry.getValue();
            Location loc = floc.clone().add(dl);
            block.apply(loc);
            blocks.add(loc);
        }
        this.active = LocationEx.of(floc.clone().add(schematic.active)).cloneAsBlockOffset(0.5, 0.1, 0.5);
        this.selected = this.active.clone().add(2, 0, 0);
        blocks.add(this.selected);  // todo remove me
        Location active_head = this.active.clone().add(0, 0.5, 0);
        for (Map.Entry<Vector, UuidPlayer> entry : schematic.getUsers().entrySet()) {
            Vector vec = entry.getKey();
            UuidPlayer user = entry.getValue();
            Location loc = LocationEx.of(floc.clone().add(vec)).cloneAsBlockOffset(0.5, 0.1, 0.5);
            LocationEx.of(loc).lookAt(active_head);
            users.put(user, loc);
            user.setLocation(loc);
        }
    }

    private static Location searchSpaceToPlaceBuilding(Location startLoc, Schematic schematic) {
        Vector size = schematic.getSize();
        Location cur = startLoc.clone().subtract(size.getX() / 2.0, 0.0, size.getZ() / 2.0);
        int firstGood = cur.getBlockY();
        int maxY = cur.getWorld().getMaxHeight();
        boolean found = false;
        while(cur.getBlockY() < maxY) {
            boolean goodLayer = true;
            for (int x = 0; x < size.getBlockX(); x++) {
                for (int z = 0; z < size.getBlockZ(); z++) {
                    Material btype = cur.clone().add(x, 0.0, z).getBlock().getType();
                    if (btype != Material.AIR) {
                        goodLayer = false;
                        break;
                    }
                }
                if(!goodLayer) break;
            }
            if(goodLayer) {
                if((cur.getBlockY() - firstGood) >= size.getBlockY()) {
                    found = true;
                    break;
                }
            }
            cur = cur.add(0.0, 1.0, 0.0);
            if(!goodLayer) {
                firstGood = cur.getBlockY();
            }
        }
        if(!found) return null;
        return cur.clone().subtract(0.0, size.getY(), 0.0).add(schematic.centerOffset());
    }

    private Location resolveSearchLocation() {
        Location location;
        location = tryResolveSearchLocationByInvoker();
        if(location != null) return location;
        location = tryResolveSearchLocationByCaptains();
        if(location != null) return location;
        location = tryResolveSearchLocationByPlayers();
        if(location != null) return location;
        return ServerEx.of(proj.getPlugin().getServer()).getMainWorld().getSpawnLocation();
    }


    @Nullable private Location tryResolveSearchLocationByInvoker() {
        ActionInvoker invoker = this.proj.getStateEngine().getInvoker();
        if(invoker == null) return null;
        UuidPlayer up = invoker.getPlayer();
        if(up == null) return null;
        return up.getLocation();
    }

    @Nullable private Location tryResolveSearchLocationByCaptains() {
        CollectMembersHandler membersHandler = proj.getMembersHandler();
        for (UuidPlayer uuidPlayer : membersHandler.getCaptainMap().captains()) {
            Player player = uuidPlayer.getPlayer();
            if(player == null) continue;
            return player.getLocation();
        }
        return null;
    }
    @Nullable private Location tryResolveSearchLocationByPlayers() {
        for (Player player : proj.getPlugin().getServer().getOnlinePlayers()) {
            return player.getLocation();
        }
        return null;
    }

    public Location getLocation() {
        return this.active.clone().add(0, 2, 0);
    }


    public Location getSelectLocation() {
        return this.active.clone();
    }
    public Location getSelectedLocation() {
        return this.selected.clone();
    }
    public Location getMemberLocation(UuidPlayer player) {
        return this.users.get(player);
    }

}

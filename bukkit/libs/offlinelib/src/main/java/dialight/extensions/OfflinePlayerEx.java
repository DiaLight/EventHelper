package dialight.extensions;

import dialight.nms.NBTTagCompoundNms;
import dialight.nms.NBTTagListNms;
import dialight.nms.OfflinePlayerNms;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class OfflinePlayerEx {

    private final Server server;
    private final UUID uuid;
    private NBTTagCompoundNms nbt;

    public OfflinePlayerEx(Server server, UUID uuid) {
        this.server = server;
        this.uuid = uuid;
        load();
    }

    public void load() {
        nbt = OfflinePlayerNms.getData(uuid);
    }

    public void save() {
        OfflinePlayerNms.setData(uuid, nbt);
    }

    @Nullable public Player getPlayer() {
        return server.getPlayer(uuid);
    }

    public boolean isOnline() {
        return getPlayer() != null;
    }

    public String getName() {
        Player player = getPlayer();
        if(player != null) return player.getName();
        return nbt.getString("lastKnownName");
    }
    private UUID getWorldUniqueId() {
        return new UUID(nbt.getLong("WorldUUIDMost"), nbt.getLong("WorldUUIDLeast"));
    }

    private void setWorldUniqueId(UUID uuid) {
        nbt.setLong("WorldUUIDMost", uuid.getMostSignificantBits());
        nbt.setLong("WorldUUIDLeast", uuid.getLeastSignificantBits());
    }

    @Nullable public World getWorld() {
        Player player = getPlayer();
        if(player != null) return player.getWorld();
        return server.getWorld(getWorldUniqueId());
    }

    @NotNull public Location getLocation() {
        Player player = getPlayer();
        if(player != null) return player.getLocation();
        World world = getWorld();
        if(world == null) world = server.getWorlds().get(0);
        NBTTagListNms location = nbt.getList("Pos", NBTTagCompoundNms.Type.Double);
        if(location == null) return world.getSpawnLocation();
        NBTTagListNms rotation = nbt.getList("Rotation", NBTTagCompoundNms.Type.Float);
        if(rotation == null) {
            return new Location(
                    world,
                    location.getDouble(0),
                    location.getDouble(1),
                    location.getDouble(2)
            );
        }
        return new Location(
                world,
                location.getDouble(0),
                location.getDouble(1),
                location.getDouble(2),
                rotation.getFloat(0),
                rotation.getFloat(1)
        );
    }

    public void setLocation(Location loc) {
        Player player = server.getPlayer(uuid);
        if(player != null) {
            player.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
            return;
        }
        NBTTagListNms location = nbt.getList("Pos", NBTTagCompoundNms.Type.Double);
        location.setDouble(0, loc.getX());
        location.setDouble(1, loc.getY());
        location.setDouble(2, loc.getZ());
        NBTTagListNms rotation = nbt.getList("Rotation", NBTTagCompoundNms.Type.Float);
        rotation.setFloat(0, loc.getYaw());
        rotation.setFloat(1, loc.getPitch());
        UUID uuid = loc.getWorld().getUID();
        nbt.setLong("WorldUUIDMost", uuid.getMostSignificantBits());
        nbt.setLong("WorldUUIDLeast", uuid.getLeastSignificantBits());
    }

    public UUID getUniqueId() {
        return uuid;
    }

}

/* {
  HurtByTimestamp:0,
  bukkit:{
    newLevel:0,
    newExp:0,
    newTotalExp:0,
    firstPlayed:1557243027991L,
    lastKnownName:"bang",
    keepLevel:0b,
    expToDrop:0,
    lastPlayed:1557248778694L
  },
  SleepTimer:0s,
  Attributes:[
    {
      Base:20.0d,
      Name:"generic.maxHealth"
    },
    {
      Base:0.0d,
      Name:"generic.knockbackResistance"
    },
    {
      Base:0.10000000149011612d,
      Name:"generic.movementSpeed"
    },
    {
      Base:0.0d,
      Name:"generic.armor"
    },
    {
      Base:0.0d,
      Name:"generic.armorToughness"
    },
    {
      Base:1.0d,
      Name:"generic.attackDamage"
    },
    {
      Base:4.0d,
      Name:"generic.attackSpeed"
    },
    {
      Base:0.0d,
      Name:"generic.luck"
    }
  ],
  Invulnerable:0b,
  FallFlying:0b,
  PortalCooldown:0,
  AbsorptionAmount:0.0f,
  abilities:{
    invulnerable:0b,
    mayfly:0b,
    instabuild:0b,
    walkSpeed:0.1f,
    mayBuild:1b,
    flying:0b,
    flySpeed:0.05f
  },
  FallDistance:0.0f,
  recipeBook:{
    recipes:[],
    isFilteringCraftable:0b,
    toBeDisplayed:[],
    isFurnaceGuiOpen:0b,
    isGuiOpen:0b,
    isFurnaceFilteringCraftable:0b
  },
  DeathTime:0s,
  XpSeed:-1826567077,
  WorldUUIDMost:-2170918356427127870L,
  Spigot.ticksLived:113691,
  XpTotal:0,
  playerGameType:0,
  seenCredits:0b,
  Motion:[
    0.0d,
    -0.0784000015258789d,
    0.0d
  ],
  UUIDLeast:-6520352556703398749L,
  Health:20.0f,
  Bukkit.updateLevel:2,
  foodSaturationLevel:5.0f,
  Paper.SpawnReason:"DEFAULT",
  Air:300s,
  OnGround:1b,
  Dimension:0,
  Rotation:[
    0.0015166996f,0.0f
  ],
  XpLevel:0,
  Score:0,
  UUIDMost:-9103285899585047256L,
  Sleeping:0b,
  Pos:[
    -70.5d,
    64.0d,
    174.5d
  ],
  Fire:-20s,
  XpP:0.0f,
  EnderItems:[],
  Paper:{LastLogin:1557243070839L,LastSeen:1557248778694L},
  DataVersion:1631,
  foodLevel:20,
  foodExhaustionLevel:0.0f,
  HurtTime:0s,
  SelectedItemSlot:0,
  SpawnWorld:"world",
  WorldUUIDLeast:-7666598352036241909L,
  Inventory:[],
  foodTickTimer:0,
  Paper.Origin:[-70.5d,64.0d,174.5d]
}
*/
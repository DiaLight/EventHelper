package dialight.misc.player;

import dialight.extensions.ServerEx;
import dialight.nms.NbtTagCompoundNms;
import dialight.nms.NbtTagListNms;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NbtPlayer {

    private final Server server;
    private NbtTagCompoundNms nbt;

    NbtPlayer(Server server) {
        this.server = server;
    }

    public NbtTagCompoundNms getNbt() {
        return nbt;
    }

    public void setNbt(NbtTagCompoundNms nbt) {
        this.nbt = nbt;
    }

    public String getName() {
        NbtTagCompoundNms bukkit = nbt.getCompound("bukkit");
        String name = bukkit.getString("lastKnownName");
        return name.isEmpty() ? null : name;
    }
    private UUID getWorldUniqueId() {
        return new UUID(nbt.getLong("WorldUUIDMost"), nbt.getLong("WorldUUIDLeast"));
    }

    private void setWorldUniqueId(UUID uuid) {
        nbt.setLong("WorldUUIDMost", uuid.getMostSignificantBits());
        nbt.setLong("WorldUUIDLeast", uuid.getLeastSignificantBits());
    }

    @Nullable public World getWorld() {
        return server.getWorld(getWorldUniqueId());
    }

    @NotNull public Location getLocation() {
        World world = getWorld();
        if(world == null) world = ServerEx.of(server).getMainWorld();
        NbtTagListNms location = nbt.getList("Pos", NbtTagCompoundNms.Type.Double);
        if(location == null) return world.getSpawnLocation();
        NbtTagListNms rotation = nbt.getList("Rotation", NbtTagCompoundNms.Type.Float);
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
        NbtTagListNms location = nbt.getList("Pos", NbtTagCompoundNms.Type.Double);
        location.setDouble(0, loc.getX());
        location.setDouble(1, loc.getY());
        location.setDouble(2, loc.getZ());
        NbtTagListNms rotation = nbt.getList("Rotation", NbtTagCompoundNms.Type.Float);
        rotation.setFloat(0, loc.getYaw());
        rotation.setFloat(1, loc.getPitch());
        UUID uuid = loc.getWorld().getUID();
        nbt.setLong("WorldUUIDMost", uuid.getMostSignificantBits());
        nbt.setLong("WorldUUIDLeast", uuid.getLeastSignificantBits());
    }

    @NotNull public Vector getVelocity() {
        NbtTagListNms location = nbt.getList("Motion", NbtTagCompoundNms.Type.Double);
        if(location == null) return new Vector(0, 0, 0);
        return new Vector(
                location.getDouble(0),
                location.getDouble(1),
                location.getDouble(2)
        );
    }
    public void setVelocity(Vector vec) {
        NbtTagListNms location = nbt.getList("Motion", NbtTagCompoundNms.Type.Double);
        location.setDouble(0, vec.getX());
        location.setDouble(1, vec.getY());
        location.setDouble(2, vec.getZ());
    }

    public boolean isFlying() {
        NbtTagCompoundNms abilities = nbt.getCompound("abilities");
        return abilities.getBoolean("flying");
    }
    public void setFlying(boolean value) {
        NbtTagCompoundNms abilities = nbt.getCompound("abilities");
        abilities.setBoolean("flying", value);
    }

    public float getFlySpeed() {
        NbtTagCompoundNms abilities = nbt.getCompound("abilities");
        return abilities.getFloat("flySpeed");
    }
    public void setFlySpeed(float value) {
        NbtTagCompoundNms abilities = nbt.getCompound("abilities");
        abilities.setFloat("flySpeed", value);
    }

    public boolean getAllowFlight() {
        NbtTagCompoundNms abilities = nbt.getCompound("abilities");
        return abilities.getBoolean("mayfly");
    }
    public void setAllowFlight(boolean value) {
        NbtTagCompoundNms abilities = nbt.getCompound("abilities");
        abilities.setBoolean("mayfly", value);
    }

    public GameMode getGameMode() {
        int playerGameType = nbt.getInt("playerGameType");
        switch(playerGameType) {
            case -1: return server.getDefaultGameMode();
            case 0: return GameMode.SURVIVAL;
            case 1: return GameMode.CREATIVE;
            case 2: return GameMode.ADVENTURE;
            case 3: return GameMode.SPECTATOR;
            default: throw new IllegalStateException();
        }
    }
    public void setGameMode(GameMode value) {
        int intValue = -1;
        switch (value) {
            case SURVIVAL:
                intValue = 0;
                break;
            case CREATIVE:
                intValue = 1;
                break;
            case ADVENTURE:
                intValue = 2;
                break;
            case SPECTATOR:
                intValue = 3;
                break;
        }
        nbt.setInt("playerGameType", intValue);
    }

    public NbtTagListNms getInventory() {
        return nbt.getList("Inventory", NbtTagCompoundNms.Type.Compound);
    }

    public void setInventory(NbtTagListNms nbt) {
        this.nbt.set("Inventory", nbt);
    }


    public static NbtPlayer of(Server server) {
        return new NbtPlayer(server);
    }

    public void setItemInInventory(int index, NbtTagCompoundNms item) {
        NbtTagListNms inventory = nbt.getList("Inventory", NbtTagCompoundNms.Type.Compound);
        int size = inventory.size();
        for (int i = 0; i < size; i++) {
            NbtTagCompoundNms compound = inventory.getCompound(i);
            int slot = compound.getByte("Slot");
            if(slot == index) {
                item.setByte("Slot", (byte) index);
                inventory.set(i, item);
                return;
            }
        }
        item.setByte("Slot", (byte) index);
        inventory.add(item);
    }

    public void setItemInMainHand(NbtTagCompoundNms item) {
        int selectedItemSlot = nbt.getInt("SelectedItemSlot");
        setItemInInventory(selectedItemSlot, item);
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
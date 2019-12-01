package dialight.nms;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerNms8 extends PlayerNms {

    public PlayerNms8(Player player) {
        super(player);
    }

    @Override public void sendPacket(Object packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet) packet);
//        try {
//            Object craftPlayer = cl_CraftPlayer.cast(player);
//            Object handle = m_CraftPlayer_getHandle.invoke(craftPlayer);
//            Object playerConnection = m_playerConnection.get(handle);
//            m_sendPacket.invoke(playerConnection, packet);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    public static NbtTagCompoundNms createNbt(org.bukkit.World world, GameProfileNms gameProfile) {
        NbtTagCompoundNms nbt = NbtTagCompoundNms.create();
        MyEntityHuman human = new MyEntityHuman((World) WorldNms.of(world).getHandle(), (GameProfile) gameProfile.getHandle());
        human.e((NBTTagCompound) nbt.getNms());
        return nbt;
    }

    static class MyEntityHuman extends EntityHuman {

        public MyEntityHuman(World world, GameProfile gameprofile) {
            super(world, gameprofile);
        }

        public void b(NBTTagCompound nbttagcompound) {
            super.b(nbttagcompound);
            nbttagcompound.setInt("playerGameType", MinecraftServer.getServer().getGamemode().getId());
        }

        @Override public boolean isSpectator() {
            return MinecraftServer.getServer().getGamemode() == WorldSettings.EnumGamemode.SPECTATOR;
        }

    }

}

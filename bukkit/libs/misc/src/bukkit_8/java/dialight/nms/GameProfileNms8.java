package dialight.nms;

import com.mojang.authlib.GameProfile;

import java.util.UUID;

public class GameProfileNms8 extends GameProfileNms {

    private final GameProfile prof;

    public GameProfileNms8(Object profile) {
        super(profile);
        prof = (GameProfile) profile;
    }

    @Override public Object getHandle() {
        return prof;
    }

    @Override public String getName() {
        return prof.getName();
    }

    @Override public UUID getUuid() {
        return prof.getId();
    }

    public static GameProfileNms create(UUID uuid, String name) {
        return new GameProfileNms8(new GameProfile(uuid, name));
    }

}

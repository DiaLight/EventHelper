package dialight.offlinelib;

import dialight.extensions.OfflinePlayerEx;

import java.util.UUID;

public class UuidPlayer {

    private final OfflineLib proj;
    private final UUID uuid;

    public UuidPlayer(OfflineLib proj, UUID uuid) {
        this.proj = proj;
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        OfflinePlayerEx op = proj.getOrLoad(uuid);
        if(op != null) return op.getName();
        return uuid.toString();
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UuidPlayer that = (UuidPlayer) o;
        return uuid.equals(that.uuid);
    }

    @Override public int hashCode() {
        return uuid.hashCode();
    }

}

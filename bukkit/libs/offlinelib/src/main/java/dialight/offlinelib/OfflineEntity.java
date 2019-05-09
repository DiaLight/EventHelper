package dialight.offlinelib;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.UUID;

public class OfflineEntity {

    public static final EntityType TYPE = EntityType.VILLAGER;

    public UUID getPlayerUniqueId() {
        throw new NotImplementedException();
    }
}

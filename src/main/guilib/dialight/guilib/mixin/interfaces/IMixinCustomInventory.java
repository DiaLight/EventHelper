package dialight.guilib.mixin.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import java.util.Set;

public interface IMixinCustomInventory {

    Set<EntityPlayer> getViewers();

    void setNativeTitle(String name);

    String getNativeTitle();

}

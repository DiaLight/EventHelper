package dialight.guilib.mixin.shadow;

import dialight.guilib.mixin.interfaces.IMixinCustomInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.common.item.inventory.custom.CustomInventory;

import java.util.Set;

@Mixin(CustomInventory.class)
public abstract class MixinCustomInventory implements IMixinCustomInventory {

    @Shadow
    private Set<EntityPlayer> viewers;

    @Shadow
    private InventoryBasic inv;

    @Override
    public Set<EntityPlayer> getViewers() {
        return viewers;
    }

    public void setNativeTitle(String name) {
        inv.setCustomName(name);
    }

    public String getNativeTitle() {
        return inv.getName();
    }

}

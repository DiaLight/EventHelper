package dialight.guilib.mixin.shadow;

import dialight.guilib.mixin.interfaces.IMixinEntityPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({EntityPlayerMP.class})
public abstract class MixinEntityPlayerMP extends MixinEntityPlayer implements IMixinEntityPlayerMP {

    @Shadow public int currentWindowId;

    @Override
    public int getCurrentWindowId() {
        return currentWindowId;
    }

}

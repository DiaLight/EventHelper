package dialight.guilib.mixin.shadow;


import dialight.guilib.mixin.interfaces.IMixinEntityPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({EntityPlayer.class})
public abstract class MixinEntityPlayer implements IMixinEntityPlayer {

    @Shadow
    public Container openContainer;

}

package dialight.guilib.mixin.shadow;

import dialight.guilib.mixin.interfaces.IMixinAbstractInventoryProperty;
import org.spongepowered.api.item.inventory.property.AbstractInventoryProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractInventoryProperty.class)
public abstract class MixinAbstractInventoryProperty<K, V> implements IMixinAbstractInventoryProperty<K, V> {

    @Shadow
    protected V value;

    @Override
    public void setValue(V value) {
        this.value = value;
    }

}

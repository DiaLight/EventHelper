package dialight.compatibility;

import dialight.extensions.ItemStackBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;

public class ItemStackBuilderBc8 extends ItemStackBuilderBc {

    public ItemStackBuilderBc8(ItemStackBuilder builder) {
        super(builder);
    }

    @Override public ItemStackBuilderBc stainedGlassPane(DyeColor color) {
        builder.reset(Material.STAINED_GLASS_PANE);
        builder.durability(color.getWoolData());
        return this;
    }

    @Override public ItemStackBuilderBc bed(DyeColor color) {
        builder.reset(Material.BED);
        builder.durability(color.getWoolData());
        return this;
    }

    @Override public ItemStackBuilderBc banner(DyeColor color) {
        builder.reset(Material.BANNER);
        builder.durability(color.getWoolData());
        return this;
    }

    public ItemStackBuilderBc playerHead() {
        builder.reset(Material.SKULL_ITEM);
        builder.durability(3);  // player head
        return this;
    }

}

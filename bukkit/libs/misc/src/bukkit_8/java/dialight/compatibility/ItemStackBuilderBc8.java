package dialight.compatibility;

import dialight.misc.ItemStackBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;

public class ItemStackBuilderBc8 extends ItemStackBuilderBc {

    public ItemStackBuilderBc8(ItemStackBuilder builder) {
        super(builder);
    }

    @Override public ItemStackBuilderBc wool(DyeColor color) {
        builder.reset(Material.WOOL);
        builder.durability(color.getWoolData());
        return this;
    }

    @Override public ItemStackBuilderBc carpet(DyeColor color) {
        builder.reset(Material.CARPET);
        builder.durability(color.getWoolData());
        return this;
    }

    @Override public ItemStackBuilderBc stainedGlass(DyeColor color) {
        builder.reset(Material.STAINED_GLASS);
        builder.durability(color.getWoolData());
        return this;
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

    @Override public ItemStackBuilderBc playerHead() {
        builder.reset(Material.SKULL_ITEM);
        builder.durability(3);  // player head
        return this;
    }

    @Override
    public ItemStackBuilderBc enderEye() {
        builder.reset(Material.EYE_OF_ENDER);
        return this;
    }

}

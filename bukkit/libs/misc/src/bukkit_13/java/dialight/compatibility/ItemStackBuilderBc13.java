package dialight.compatibility;

import dialight.extensions.ItemStackBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;

public class ItemStackBuilderBc13 extends ItemStackBuilderBc {

    public ItemStackBuilderBc13(ItemStackBuilder builder) {
        super(builder);
    }

    @Override
    public ItemStackBuilderBc stainedGlassPane(DyeColor color) {
        if(color == DyeColor.WHITE) {
            builder.reset(Material.WHITE_STAINED_GLASS_PANE);
        } else if(color == DyeColor.ORANGE) {
            builder.reset(Material.ORANGE_STAINED_GLASS_PANE);
        } else if(color == DyeColor.MAGENTA) {
            builder.reset(Material.MAGENTA_STAINED_GLASS_PANE);
        } else if(color == DyeColor.LIGHT_BLUE) {
            builder.reset(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        } else if(color == DyeColor.YELLOW) {
            builder.reset(Material.YELLOW_STAINED_GLASS_PANE);
        } else if(color == DyeColor.LIME) {
            builder.reset(Material.LIME_STAINED_GLASS_PANE);
        } else if(color == DyeColor.PINK) {
            builder.reset(Material.PINK_STAINED_GLASS_PANE);
        } else if(color == DyeColor.GRAY) {
            builder.reset(Material.GRAY_STAINED_GLASS_PANE);
        } else if(color == DyeColor.LIGHT_GRAY) {
            builder.reset(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        } else if(color == DyeColor.CYAN) {
            builder.reset(Material.CYAN_STAINED_GLASS_PANE);
        } else if(color == DyeColor.PURPLE) {
            builder.reset(Material.PURPLE_STAINED_GLASS_PANE);
        } else if(color == DyeColor.BLUE) {
            builder.reset(Material.BLUE_STAINED_GLASS_PANE);
        } else if(color == DyeColor.BROWN) {
            builder.reset(Material.BROWN_STAINED_GLASS_PANE);
        } else if(color == DyeColor.GREEN) {
            builder.reset(Material.GREEN_STAINED_GLASS_PANE);
        } else if(color == DyeColor.RED) {
            builder.reset(Material.RED_STAINED_GLASS_PANE);
        } else if(color == DyeColor.BLACK) {
            builder.reset(Material.BLACK_STAINED_GLASS_PANE);
        } else throw new IllegalArgumentException();
        return this;
    }

    public ItemStackBuilderBc bed(DyeColor color) {
        if(color == DyeColor.WHITE) {
            builder.reset(Material.WHITE_BED);
        } else if(color == DyeColor.ORANGE) {
            builder.reset(Material.ORANGE_BED);
        } else if(color == DyeColor.MAGENTA) {
            builder.reset(Material.MAGENTA_BED);
        } else if(color == DyeColor.LIGHT_BLUE) {
            builder.reset(Material.LIGHT_BLUE_BED);
        } else if(color == DyeColor.YELLOW) {
            builder.reset(Material.YELLOW_BED);
        } else if(color == DyeColor.LIME) {
            builder.reset(Material.LIME_BED);
        } else if(color == DyeColor.PINK) {
            builder.reset(Material.PINK_BED);
        } else if(color == DyeColor.GRAY) {
            builder.reset(Material.GRAY_BED);
        } else if(color == DyeColor.LIGHT_GRAY) {
            builder.reset(Material.LIGHT_GRAY_BED);
        } else if(color == DyeColor.CYAN) {
            builder.reset(Material.CYAN_BED);
        } else if(color == DyeColor.PURPLE) {
            builder.reset(Material.PURPLE_BED);
        } else if(color == DyeColor.BLUE) {
            builder.reset(Material.BLUE_BED);
        } else if(color == DyeColor.BROWN) {
            builder.reset(Material.BROWN_BED);
        } else if(color == DyeColor.GREEN) {
            builder.reset(Material.GREEN_BED);
        } else if(color == DyeColor.RED) {
            builder.reset(Material.RED_BED);
        } else if(color == DyeColor.BLACK) {
            builder.reset(Material.BLACK_BED);
        } else throw new IllegalArgumentException();
        return this;
    }

    @Override
    public ItemStackBuilderBc playerHead() {
        builder.reset(Material.PLAYER_HEAD);
        return this;
    }

}

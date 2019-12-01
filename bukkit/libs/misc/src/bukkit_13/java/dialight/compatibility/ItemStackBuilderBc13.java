package dialight.compatibility;

import dialight.misc.ItemStackBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;

public class ItemStackBuilderBc13 extends ItemStackBuilderBc {

    public ItemStackBuilderBc13(ItemStackBuilder builder) {
        super(builder);
    }

    @Override public ItemStackBuilderBc wool(DyeColor color) {
        if(color == DyeColor.WHITE) {
            builder.reset(Material.WHITE_WOOL);
        } else if(color == DyeColor.ORANGE) {
            builder.reset(Material.ORANGE_WOOL);
        } else if(color == DyeColor.MAGENTA) {
            builder.reset(Material.MAGENTA_WOOL);
        } else if(color == DyeColor.LIGHT_BLUE) {
            builder.reset(Material.LIGHT_BLUE_WOOL);
        } else if(color == DyeColor.YELLOW) {
            builder.reset(Material.YELLOW_WOOL);
        } else if(color == DyeColor.LIME) {
            builder.reset(Material.LIME_WOOL);
        } else if(color == DyeColor.PINK) {
            builder.reset(Material.PINK_WOOL);
        } else if(color == DyeColor.GRAY) {
            builder.reset(Material.GRAY_WOOL);
        } else if(color == DyeColor.LIGHT_GRAY) {
            builder.reset(Material.LIGHT_GRAY_WOOL);
        } else if(color == DyeColor.CYAN) {
            builder.reset(Material.CYAN_WOOL);
        } else if(color == DyeColor.PURPLE) {
            builder.reset(Material.PURPLE_WOOL);
        } else if(color == DyeColor.BLUE) {
            builder.reset(Material.BLUE_WOOL);
        } else if(color == DyeColor.BROWN) {
            builder.reset(Material.BROWN_WOOL);
        } else if(color == DyeColor.GREEN) {
            builder.reset(Material.GREEN_WOOL);
        } else if(color == DyeColor.RED) {
            builder.reset(Material.RED_WOOL);
        } else if(color == DyeColor.BLACK) {
            builder.reset(Material.BLACK_WOOL);
        } else throw new IllegalArgumentException();
        return this;
    }

    @Override public ItemStackBuilderBc carpet(DyeColor color) {
        if(color == DyeColor.WHITE) {
            builder.reset(Material.WHITE_CARPET);
        } else if(color == DyeColor.ORANGE) {
            builder.reset(Material.ORANGE_CARPET);
        } else if(color == DyeColor.MAGENTA) {
            builder.reset(Material.MAGENTA_CARPET);
        } else if(color == DyeColor.LIGHT_BLUE) {
            builder.reset(Material.LIGHT_BLUE_CARPET);
        } else if(color == DyeColor.YELLOW) {
            builder.reset(Material.YELLOW_CARPET);
        } else if(color == DyeColor.LIME) {
            builder.reset(Material.LIME_CARPET);
        } else if(color == DyeColor.PINK) {
            builder.reset(Material.PINK_CARPET);
        } else if(color == DyeColor.GRAY) {
            builder.reset(Material.GRAY_CARPET);
        } else if(color == DyeColor.LIGHT_GRAY) {
            builder.reset(Material.LIGHT_GRAY_CARPET);
        } else if(color == DyeColor.CYAN) {
            builder.reset(Material.CYAN_CARPET);
        } else if(color == DyeColor.PURPLE) {
            builder.reset(Material.PURPLE_CARPET);
        } else if(color == DyeColor.BLUE) {
            builder.reset(Material.BLUE_CARPET);
        } else if(color == DyeColor.BROWN) {
            builder.reset(Material.BROWN_CARPET);
        } else if(color == DyeColor.GREEN) {
            builder.reset(Material.GREEN_CARPET);
        } else if(color == DyeColor.RED) {
            builder.reset(Material.RED_CARPET);
        } else if(color == DyeColor.BLACK) {
            builder.reset(Material.BLACK_CARPET);
        } else throw new IllegalArgumentException();
        return this;
    }

    @Override public ItemStackBuilderBc stainedGlass(DyeColor color) {
        if(color == DyeColor.WHITE) {
            builder.reset(Material.WHITE_STAINED_GLASS);
        } else if(color == DyeColor.ORANGE) {
            builder.reset(Material.ORANGE_STAINED_GLASS);
        } else if(color == DyeColor.MAGENTA) {
            builder.reset(Material.MAGENTA_STAINED_GLASS);
        } else if(color == DyeColor.LIGHT_BLUE) {
            builder.reset(Material.LIGHT_BLUE_STAINED_GLASS);
        } else if(color == DyeColor.YELLOW) {
            builder.reset(Material.YELLOW_STAINED_GLASS);
        } else if(color == DyeColor.LIME) {
            builder.reset(Material.LIME_STAINED_GLASS);
        } else if(color == DyeColor.PINK) {
            builder.reset(Material.PINK_STAINED_GLASS);
        } else if(color == DyeColor.GRAY) {
            builder.reset(Material.GRAY_STAINED_GLASS);
        } else if(color == DyeColor.LIGHT_GRAY) {
            builder.reset(Material.LIGHT_GRAY_STAINED_GLASS);
        } else if(color == DyeColor.CYAN) {
            builder.reset(Material.CYAN_STAINED_GLASS);
        } else if(color == DyeColor.PURPLE) {
            builder.reset(Material.PURPLE_STAINED_GLASS);
        } else if(color == DyeColor.BLUE) {
            builder.reset(Material.BLUE_STAINED_GLASS);
        } else if(color == DyeColor.BROWN) {
            builder.reset(Material.BROWN_STAINED_GLASS);
        } else if(color == DyeColor.GREEN) {
            builder.reset(Material.GREEN_STAINED_GLASS);
        } else if(color == DyeColor.RED) {
            builder.reset(Material.RED_STAINED_GLASS);
        } else if(color == DyeColor.BLACK) {
            builder.reset(Material.BLACK_STAINED_GLASS);
        } else throw new IllegalArgumentException();
        return this;
    }

    @Override public ItemStackBuilderBc stainedGlassPane(DyeColor color) {
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

    @Override public ItemStackBuilderBc bed(DyeColor color) {
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
    @Override public ItemStackBuilderBc banner(DyeColor color) {
        if(color == DyeColor.WHITE) {
            builder.reset(Material.WHITE_BANNER);
        } else if(color == DyeColor.ORANGE) {
            builder.reset(Material.ORANGE_BANNER);
        } else if(color == DyeColor.MAGENTA) {
            builder.reset(Material.MAGENTA_BANNER);
        } else if(color == DyeColor.LIGHT_BLUE) {
            builder.reset(Material.LIGHT_BLUE_BANNER);
        } else if(color == DyeColor.YELLOW) {
            builder.reset(Material.YELLOW_BANNER);
        } else if(color == DyeColor.LIME) {
            builder.reset(Material.LIME_BANNER);
        } else if(color == DyeColor.PINK) {
            builder.reset(Material.PINK_BANNER);
        } else if(color == DyeColor.GRAY) {
            builder.reset(Material.GRAY_BANNER);
        } else if(color == DyeColor.LIGHT_GRAY) {
            builder.reset(Material.LIGHT_GRAY_BANNER);
        } else if(color == DyeColor.CYAN) {
            builder.reset(Material.CYAN_BANNER);
        } else if(color == DyeColor.PURPLE) {
            builder.reset(Material.PURPLE_BANNER);
        } else if(color == DyeColor.BLUE) {
            builder.reset(Material.BLUE_BANNER);
        } else if(color == DyeColor.BROWN) {
            builder.reset(Material.BROWN_BANNER);
        } else if(color == DyeColor.GREEN) {
            builder.reset(Material.GREEN_BANNER);
        } else if(color == DyeColor.RED) {
            builder.reset(Material.RED_BANNER);
        } else if(color == DyeColor.BLACK) {
            builder.reset(Material.BLACK_BANNER);
        } else throw new IllegalArgumentException();
        return this;
    }

    @Override public ItemStackBuilderBc playerHead() {
        builder.reset(Material.PLAYER_HEAD);
        return this;
    }

    @Override public ItemStackBuilderBc enderEye() {
        builder.reset(Material.ENDER_EYE);
        return this;
    }

}

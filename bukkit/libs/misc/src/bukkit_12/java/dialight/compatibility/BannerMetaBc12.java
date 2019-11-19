package dialight.compatibility;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.meta.BannerMeta;

public class BannerMetaBc12 extends BannerMetaBc {

    public BannerMetaBc12(BannerMeta meta) {
        super(meta);
    }

    @Override public void setBaseColor(DyeColor color) {
        meta.addPattern(new Pattern(color, PatternType.BASE));
    }

}

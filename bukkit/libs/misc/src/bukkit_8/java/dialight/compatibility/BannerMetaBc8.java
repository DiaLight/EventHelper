package dialight.compatibility;

import org.bukkit.DyeColor;
import org.bukkit.inventory.meta.BannerMeta;

public class BannerMetaBc8 extends BannerMetaBc {

    public BannerMetaBc8(BannerMeta meta) {
        super(meta);
    }

    @Override
    public void setBaseColor(DyeColor color) {
        meta.setBaseColor(color);
    }

}

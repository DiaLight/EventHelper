package dialight.teams.captain.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;

public class BlockData {

    private final Material material;
    private final MaterialData data;

    public BlockData(Material material, MaterialData data) {
        this.material = material;
        this.data = data;
    }
    public BlockData(Material material) {
        this(material, null);
    }

    public void apply(Location location) {
        BlockState state = location.getBlock().getState();
        state.setType(material);
        if(data != null) {
            state.setData(data);
        }
        state.update(true);
    }

    public Material getMaterial() {
        return material;
    }

    public MaterialData getData() {
        return data;
    }

}

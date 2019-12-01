package dialight.nms;

import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.PlayerInventory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class PlayerInventoryNms8 extends PlayerInventoryNms {

    public PlayerInventoryNms8(Inventory inv) {
        super(inv);
    }

    public CraftInventory asCraft() {
        return (CraftInventory) inv;
    }

    public PlayerInventory asNms() {
        return (PlayerInventory) asCraft().getInventory();
    }

    @Override public NbtTagListNms getNbtContent() {
        return NbtTagListNms.of(asNms().a(new NBTTagList()));
    }
    @Override public void setNbtContent(NbtTagListNms nbt) {
        asNms().b((NBTTagList) nbt.getNms());
    }

}

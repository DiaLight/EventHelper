package dialight.guilib.slot;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Nullable;

public class SlotClickEvent {

    @Nullable private final Vec2i layoutPos;
    private final Slot slot;
    private final Player player;
    private final InventoryClickEvent event;

    public SlotClickEvent(Vec2i layoutPos, Slot slot, Player player, InventoryClickEvent event) {
        this.layoutPos = layoutPos;
        this.slot = slot;
        this.player = player;
        this.event = event;
    }

    @Nullable public Vec2i getLayoutPos() {
        return layoutPos;
    }

    public Slot getSlot() {
        return slot;
    }

    public Player getPlayer() {
        return player;
    }

    public InventoryClickEvent getEvent() {
        return event;
    }

}

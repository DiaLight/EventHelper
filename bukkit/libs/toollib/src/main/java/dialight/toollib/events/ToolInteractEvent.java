package dialight.toollib.events;

import dialight.extensions.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ToolInteractEvent {

    @Getter private final Player player;
    @Getter private final ItemStack item;
    @Getter private final Action action;
    @Getter private final boolean sneaking;
    @Getter protected Target target;
    @Getter @Setter private boolean cancelled = true;

    public ToolInteractEvent(Player player, ItemStack item, Action action) {
        this.player = player;
        this.item = item;
        this.action = action;
        this.sneaking = player.isSneaking();
    }

    public Location lookingAtLoc() {
        return Utils.getTargetBlock(
                player.getEyeLocation(),
                player.getLocation().getDirection().normalize(),
                50
        ).toBlockLocation().add(.5, 1, .5);
    }

    @Override public String toString() {
        return String.format("%s, %s, %s", player.getName(), item.getType(), action);
    }

    public static class Air extends ToolInteractEvent {

        public Air(Player player, ItemStack item, Action action) {
            super(player, item, action);
            target = Target.AIR;
        }

        @Override public String toString() {
            return String.format("ToolInteractEvent.Air{%s}", super.toString());
        }

    }

    public static class Block extends ToolInteractEvent {

        private final org.bukkit.block.Block block;

        public Block(Player player, ItemStack item, org.bukkit.block.Block block, Action action) {
            super(player, item, action);
            this.block = block;
            target = Target.BLOCK;
        }

        @Override public String toString() {
            return String.format("ToolInteractEvent.Block{%s}", super.toString());
        }

        @Override public Location lookingAtLoc() {
            return block.getLocation().add(.5, 1.0, .5);
        }

    }

    public static class Entity extends ToolInteractEvent {

        private final org.bukkit.entity.Entity entity;

        public Entity(Player player, ItemStack item, org.bukkit.entity.Entity entity, Action action) {
            super(player, item, action);
            this.entity = entity;
            target = Target.ENTITY;
        }

        @Override public String toString() {
            return String.format("ToolInteractEvent.Entity{%s}", super.toString());
        }
    }

    public enum Target {
        AIR,
        BLOCK,
        ENTITY
    }

    public enum Action {
        LEFT_CLICK,
        RIGHT_CLICK,
        DROP
    }

}

package dialight.toollib.events;

import dialight.compatibility.LocationBc;
import dialight.extensions.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ToolInteractEvent {

    private final Player player;
    private final ItemStack item;
    private final Action action;
    private final boolean sneaking;
    protected Target target;
    private boolean cancelled = true;

    public ToolInteractEvent(Player player, ItemStack item, Action action) {
        this.player = player;
        this.item = item;
        this.action = action;
        this.sneaking = player.isSneaking();
    }

    public Location lookingAtLoc() {
        Location block = Utils.getTargetBlock(
                player.getEyeLocation(),
                player.getLocation().getDirection().normalize(),
                50
        );
        return LocationBc.of(block).toBlockLocation().add(.5, 1, .5);
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItem() {
        return item;
    }

    public Action getAction() {
        return action;
    }

    public boolean isSneaking() {
        return sneaking;
    }

    public Target getTarget() {
        return target;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
            if(block.getType().isSolid()) return block.getLocation().add(.5, 1.0, .5);
            return super.lookingAtLoc();
        }

    }

    public static class Entity extends ToolInteractEvent {

        private final org.bukkit.entity.Entity entity;

        public Entity(Player player, ItemStack item, org.bukkit.entity.Entity entity, Action action) {
            super(player, item, action);
            this.entity = entity;
            target = Target.ENTITY;
        }

        public org.bukkit.entity.Entity getEntity() {
            return entity;
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

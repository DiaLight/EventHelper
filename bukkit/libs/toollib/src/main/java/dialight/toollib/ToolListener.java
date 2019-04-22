package dialight.toollib;

import dialight.toollib.events.ToolInteractEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ToolListener implements Listener {

    private final ToolCore core;

    public ToolListener(ToolCore core) {
        this.core = core;
    }


    @Nullable
    private Tool identifyTool(ItemStack itemStack) {
        String id = Tool.parseId(itemStack);
        if(id == null) return null;
        return core.getTool(id);
    }

    private boolean interactEntity = false;
    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent e) {
        if(interactEntity) {
            interactEntity = false;
            return;
        }
        ItemStack item = e.getItem();
        if(item == null) return;
        Tool tool = identifyTool(item);
        if (tool == null) return;
        if (!core.hasAccess(e.getPlayer())) return;
        if (item.getType().isBlock()) return;
        ToolInteractEvent toolEvent = null;
        switch(e.getAction()) {
            case LEFT_CLICK_AIR: {
                toolEvent = new ToolInteractEvent.Air(e.getPlayer(), item, ToolInteractEvent.Action.LEFT_CLICK);
            } break;
            case LEFT_CLICK_BLOCK: {
                toolEvent = new ToolInteractEvent.Block(e.getPlayer(), item, e.getClickedBlock(), ToolInteractEvent.Action.LEFT_CLICK);
            } break;
            case RIGHT_CLICK_AIR: {
                toolEvent = new ToolInteractEvent.Air(e.getPlayer(), item, ToolInteractEvent.Action.RIGHT_CLICK);
            } break;
            case RIGHT_CLICK_BLOCK: {
                toolEvent = new ToolInteractEvent.Block(e.getPlayer(), item, e.getClickedBlock(), ToolInteractEvent.Action.RIGHT_CLICK);
            } break;
            default:
                return;
        }
        tool.onClick(toolEvent);
        if (toolEvent.isCancelled()) e.setCancelled(true);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();
        if (!core.hasAccess(player)) return;
        ItemStack item = null;
        EntityEquipment equipment = player.getEquipment();
        if(equipment == null) return;
        switch (e.getHand()) {
            case HAND:
                item = equipment.getItemInMainHand();
                break;
            case OFF_HAND:
                item = equipment.getItemInOffHand();
                break;
        }
        if(item == null) return;
        Tool tool = identifyTool(item);
        if (tool == null) return;
        ToolInteractEvent toolEvent = new ToolInteractEvent.Entity(player, item, e.getRightClicked(), ToolInteractEvent.Action.RIGHT_CLICK);
        tool.onClick(toolEvent);
        if (toolEvent.isCancelled()) e.setCancelled(true);
        interactEntity = true;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (e.getDamager().getType() != EntityType.PLAYER) return;
        Player player = (Player) e.getDamager();
        if (!core.hasAccess(player)) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        Tool tool = identifyTool(item);
        if (tool == null) return;
        ToolInteractEvent toolEvent = new ToolInteractEvent.Entity(player, item, e.getEntity(), ToolInteractEvent.Action.LEFT_CLICK);
        tool.onClick(toolEvent);
        if (toolEvent.isCancelled()) e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (!core.hasAccess(player)) return;
        ItemStack item = e.getItemInHand();
        Tool tool = identifyTool(item);
        if (tool == null) return;
        if (!item.getType().isBlock()) return;
//        tool.blockPlace(player, e.getBlockPlaced());
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (!core.hasAccess(player)) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        Tool tool = identifyTool(item);
        if (tool == null) return;
        if (!item.getType().isBlock()) return;
//        tool.blockBreak(player, e.getBlock());
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (!core.hasAccess(player)) return;
        ItemStack item = e.getItemDrop().getItemStack();
        Tool tool = identifyTool(item);
        if (tool == null) return;
        ToolInteractEvent toolEvent = new ToolInteractEvent.Entity(player, item, e.getItemDrop(), ToolInteractEvent.Action.DROP);
        tool.onClick(toolEvent);
        if (toolEvent.isCancelled()) {
            e.setCancelled(true);
            e.getItemDrop().remove();
        }
    }


}

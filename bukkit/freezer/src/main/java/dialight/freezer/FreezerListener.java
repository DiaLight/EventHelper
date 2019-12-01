package dialight.freezer;

import dialight.extensions.PlayerEx;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.PluginManager;

public class FreezerListener implements Listener {

    private final Freezer proj;
    private boolean enabled = false;

    public FreezerListener(Freezer proj) {
        this.proj = proj;
    }

    public void enable() {
        if(enabled) return;
        PluginManager pluginManager = proj.getPlugin().getServer().getPluginManager();
        pluginManager.registerEvents(this, proj.getPlugin());
        enabled = true;
    }

    public void disable() {
        if(!enabled) return;
        HandlerList.unregisterAll(this);
        enabled = false;
    }

    @EventHandler public void onTeleport(PlayerTeleportEvent e) {
        Frozen frozen = proj.get(e.getPlayer().getUniqueId());
        if(frozen != null) {
            frozen.setLocation(e.getTo());
        }
    }

    @EventHandler public void onMove(PlayerMoveEvent e) {
        Player trg = e.getPlayer();
        Frozen frozen = this.proj.get(trg.getUniqueId());
        if(frozen == null) return;
        if(!blockMoved(e.getFrom(), e.getTo())) return;
        PlayerEx.of(trg).teleport(frozen.getLocation());
        if(frozen.getLocation().clone().add(0, -1, 0).getBlock().getType().isBlock()) {
            proj.getFreezeFlayers().removeFly(frozen.getTarget());
        } else {
            proj.getFreezeFlayers().setFly(frozen.getTarget());
        }
    }

    private boolean blockMoved(Location f, Location t) {
        if(f.getBlockX() != t.getBlockX()) return true;
        if(f.getBlockY() != t.getBlockY()) return true;
        if(f.getBlockZ() != t.getBlockZ()) return true;
        return false;
    }

    @EventHandler public void onPluginDisable(PluginDisableEvent e) {
        if (!e.getPlugin().getName().equals(proj.getPlugin().getName())) return;
        for (Frozen frozen : proj.getFrozens()) {
            Player trg = Bukkit.getPlayer(frozen.getTarget().getUuid());
            if(trg == null) continue;
            trg.sendMessage(FreezerMessages.unfreezeByReload);
        }
    }

    @EventHandler public void onJoin(PlayerJoinEvent e) {
        Player trg = e.getPlayer();
        Frozen frozen = this.proj.get(trg.getUniqueId());
        if(frozen == null) return;
        PlayerEx.of(trg).teleport(frozen.getLocation());
        trg.sendMessage(FreezerMessages.youFrozen);
    }
    @EventHandler public void onQuit(PlayerQuitEvent e) {

    }

    //////////////////////////////HOLDER/////////////////////////////////

    //ломать и ставить блоки
    @EventHandler public void onBlockBreak(BlockBreakEvent e) {
        Frozen frozen = this.proj.get(e.getPlayer().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }
    @EventHandler public void onBlockPlace(BlockPlaceEvent e) {
        Frozen frozen = this.proj.get(e.getPlayer().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    //садиться на шифт
    @EventHandler public void toggleSneak(PlayerToggleSneakEvent e) {
        Frozen frozen = this.proj.get(e.getPlayer().getUniqueId());
        if(frozen == null) return;
        e.getPlayer().setSneaking(false);
        e.setCancelled(true);
    }

    //наносить и получать урон
    @EventHandler public void onDamage(EntityDamageEvent e) {
        if(e.getEntity().getType() != EntityType.PLAYER) return;
        Frozen frozen = this.proj.get(e.getEntity().getUniqueId());
        if(frozen == null) return;
        e.setDamage(0d);
        e.setCancelled(true);
    }
    @EventHandler public void onDealDamage(EntityDamageByEntityEvent e) {
        if(e.getDamager().getType() != EntityType.PLAYER) return;
        Frozen frozen = this.proj.get(e.getDamager().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    //выкидывать и подбирать вещи
    @EventHandler public void onDrop(PlayerDropItemEvent e) {
        Frozen frozen = this.proj.get(e.getPlayer().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }
    @EventHandler public void onPickup(PlayerPickupItemEvent e) {
        Frozen frozen = this.proj.get(e.getPlayer().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    //использовать транспорт
    @EventHandler public void enterVehicle(VehicleEnterEvent e) {
        if(e.getEntered().getType() != EntityType.PLAYER) return;
        Frozen frozen = this.proj.get(e.getEntered().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }
    @EventHandler public void exitVehicle(VehicleExitEvent e) {
        if(e.getExited().getType() != EntityType.PLAYER) return;
        Frozen frozen = this.proj.get(e.getExited().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    //выливать и набирать жидкости в ведра
    @EventHandler public void onEmpty(PlayerBucketEmptyEvent e) {
        Frozen frozen = this.proj.get(e.getPlayer().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }
    @EventHandler public void onFill(PlayerBucketFillEvent e) {
        Frozen frozen = this.proj.get(e.getPlayer().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    //использовать кровать
    @EventHandler
    public void onEnterBed(PlayerBedEnterEvent e) {
        Frozen frozen = this.proj.get(e.getPlayer().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    //открывать и использовать инвентарь
    @EventHandler public void onInventory(InventoryClickEvent e) {
        if(e.getWhoClicked().getType() != EntityType.PLAYER) return;
        Frozen frozen = this.proj.get(e.getWhoClicked().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }
    @EventHandler public void onOpen(InventoryOpenEvent e) {
        Frozen frozen = this.proj.get(e.getPlayer().getUniqueId());
        if(frozen == null) return;
        if(e.getPlayer().isOp()) return;
        e.setCancelled(true);
    }

    @EventHandler public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if(e.getEntityType() != EntityType.PLAYER) return;
        Frozen frozen = this.proj.get(e.getEntity().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    @EventHandler public void sign(SignChangeEvent e) {
        Frozen frozen = this.proj.get(e.getPlayer().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    @EventHandler public void blockDamage(BlockDamageEvent e) {
        Frozen frozen = this.proj.get(e.getPlayer().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    @EventHandler public void enchantItem(EnchantItemEvent e) {
        Frozen frozen = this.proj.get(e.getEnchanter().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    @EventHandler public void onPrepareEnchant(PrepareItemEnchantEvent e) {
        Frozen frozen = this.proj.get(e.getEnchanter().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    @EventHandler public void onBreakHanging(HangingBreakByEntityEvent e) {
        if(e.getRemover().getType() != EntityType.PLAYER) return;
        Frozen frozen = this.proj.get(e.getRemover().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    @EventHandler public void onPlaceHanging(HangingPlaceEvent e) {
        Frozen frozen = this.proj.get(e.getPlayer().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    @EventHandler public void onCraft(CraftItemEvent e) {
        if(e.getWhoClicked().getType() != EntityType.PLAYER) return;
        Frozen frozen = this.proj.get(e.getWhoClicked().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    @EventHandler public void onFish(PlayerFishEvent e) {
        Frozen frozen = this.proj.get(e.getPlayer().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    @EventHandler public void onItemConsume(PlayerItemConsumeEvent e) {
        Frozen frozen = this.proj.get(e.getPlayer().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    @EventHandler public void onShear(PlayerShearEntityEvent e) {
        Frozen frozen = this.proj.get(e.getPlayer().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

    @EventHandler public void onInteractEntity(PlayerInteractEntityEvent e) {
        Frozen frozen = this.proj.get(e.getPlayer().getUniqueId());
        if(frozen == null) return;
        e.setCancelled(true);
    }

}

package dialight.freezer

import dialight.extensions.dump
import dialight.teleporter.PlayerTeleportEvent
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.cause.Cause
import org.spongepowered.api.event.cause.EventContext
import org.spongepowered.api.event.entity.MoveEntityEvent
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.text.Text
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

class FreezerListener(val plugin: FreezerPlugin) {

    @Listener
    fun onTeleport(event: PlayerTeleportEvent, @First player: Player) {
        event.dump()
        val frozen = plugin.freezer.frozen.idFrozen[player.uniqueId] ?: return
        frozen.setLocation(event.getTo())
    }

    @Listener
    fun onMove(e: MoveEntityEvent, @First trg: Player) {
        val frozen = plugin.freezer.frozen.idFrozen[trg.uniqueId] ?: return
        if (!blockMoved(e.fromTransform.location, e.toTransform.location)) return
//        this.teleportFrozen(frozen, trg)
    }

    private fun blockMoved(f: Location<World>, t: Location<World>): Boolean {
        if (f.blockX != t.blockX) return true
        if (f.blockY != t.blockY) return true
        return f.blockZ != t.blockZ
    }

//    @EventHandler
//    fun onPluginDisable(e: PluginDisableEvent) {
//        if (e.getPlugin().getName() != eventHelper.plugin.getName()) return
//        for (frozen in this.idFrozen.values) {
//            val trg = Bukkit.getPlayer(frozen.getUniqueId()) ?: continue
//            FreezerMessages.unfreezeByReload(trg)
//        }
//    }
//
//    @EventHandler
//    fun onJoin(e: PlayerJoinEvent) {
//        val trg = e.getPlayer()
//        val frozen = this.offlineFrozen.remove(trg.getUniqueId()) ?: return
//        frozen.updateAll(trg)
//        this.idFrozen.put(trg.getEntityId(), frozen)
//        this.onFreezeOnline(frozen, trg)
//        FreezerMessages.frozen(trg)
//    }
//
//    @EventHandler
//    fun onQuit(e: PlayerQuitEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        this.offlineFrozen.put(e.getPlayer().getUniqueId(), frozen)
//    }
//
//    protected fun teleportFrozen(frozen: Frozen, trg: Player) {
//        trg.teleport(Utils.tpFix(frozen.getLocation(), trg.getLocation()))
//    }
//
//    protected override fun onFreezeOnline(frozen: Frozen, trg: Player) {
//        super.onFreezeOnline(frozen, trg)
//        this.teleportFrozen(frozen, trg)
//        this.freezeFlayers.setFly(trg)
//    }
//
//    protected override fun onUnfreezeOnline(frozen: Frozen, trg: Player) {
//        super.onUnfreezeOnline(frozen, trg)
//        this.freezeFlayers.removeFly(trg)
//        frozen.recoverVelocity(trg)
//    }
//
//    protected override fun onFreezeOffline(frozen: Frozen) {
//        super.onFreezeOffline(frozen)
//        this.freezeFlayers.setFlyFuture(frozen.getUniqueId())
//    }
//
//    protected override fun onUnfreezeOffline(frozen: Frozen) {
//        super.onUnfreezeOffline(frozen)
//        this.freezeFlayers.removeFlyFuture(frozen.getUniqueId())
//    }
//
//    //////////////////////////////HOLDER/////////////////////////////////
//
//    //ломать и ставить блоки
//    @EventHandler
//    fun onBlockBreak(e: BlockBreakEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onBlockPlace(e: BlockPlaceEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    //садиться на шифт
//    @EventHandler
//    fun toggleSneak(e: PlayerToggleSneakEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        e.getPlayer().setSneaking(false)
//        e.setCancelled(true)
//    }
//
//    //наносить и получать урон
//    @EventHandler
//    fun onDamage(e: EntityDamageEvent) {
//        if (e.getEntity().getType() != EntityType.PLAYER) return
//        val frozen = this.idFrozen.get(e.getEntity().getEntityId()) ?: return
//        e.setDamage(0.0)
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onDealDamage(e: EntityDamageByEntityEvent) {
//        if (e.getDamager().getType() != EntityType.PLAYER) return
//        val frozen = this.idFrozen.get(e.getDamager().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    //выкидывать и подбирать вещи
//    @EventHandler
//    fun onDrop(e: PlayerDropItemEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onPickup(e: PlayerPickupItemEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    //использовать транспорт
//    @EventHandler
//    fun enterVehicle(e: VehicleEnterEvent) {
//        if (e.getEntered().getType() != EntityType.PLAYER) return
//        val frozen = this.idFrozen.get(e.getEntered().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun exitVehicle(e: VehicleExitEvent) {
//        if (e.getExited().getType() != EntityType.PLAYER) return
//        val frozen = this.idFrozen.get(e.getExited().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    //выливать и набирать жидкости в ведра
//    @EventHandler
//    fun onEmpty(e: PlayerBucketEmptyEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onFill(e: PlayerBucketFillEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    //использовать кровать
//    @EventHandler
//    fun onEnterBed(e: PlayerBedEnterEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    //открывать и использовать инвентарь
//    @EventHandler
//    fun onInventory(e: InventoryClickEvent) {
//        if (e.getWhoClicked().getType() != EntityType.PLAYER) return
//        val frozen = this.idFrozen.get(e.getWhoClicked().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onOpen(e: InventoryOpenEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onFoodLevelChange(e: FoodLevelChangeEvent) {
//        if (e.getEntityType() != EntityType.PLAYER) return
//        val frozen = this.idFrozen.get(e.getEntity().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun sign(e: SignChangeEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun blockDamage(e: BlockDamageEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun enchantItem(e: EnchantItemEvent) {
//        val frozen = this.idFrozen.get(e.getEnchanter().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onPrepareEnchant(e: PrepareItemEnchantEvent) {
//        val frozen = this.idFrozen.get(e.getEnchanter().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onBreakHanging(e: HangingBreakByEntityEvent) {
//        if (e.getRemover().getType() != EntityType.PLAYER) return
//        val frozen = this.idFrozen.get(e.getRemover().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onPlaceHanging(e: HangingPlaceEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onCraft(e: CraftItemEvent) {
//        if (e.getWhoClicked().getType() != EntityType.PLAYER) return
//        val frozen = this.idFrozen.get(e.getWhoClicked().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onFish(e: PlayerFishEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onItemConsume(e: PlayerItemConsumeEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onShear(e: PlayerShearEntityEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onInteractEntity(e: PlayerInteractEntityEvent) {
//        val frozen = this.idFrozen.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }



}
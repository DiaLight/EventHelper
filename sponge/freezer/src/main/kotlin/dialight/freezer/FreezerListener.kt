package dialight.freezer

import dialight.teleporter.event.PlayerTeleportEvent
import org.spongepowered.api.data.DataTransactionResult
import org.spongepowered.api.data.key.Key
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.value.immutable.ImmutableValue
import org.spongepowered.api.entity.EntityTypes
import org.spongepowered.api.entity.Item
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.action.InteractEvent
import org.spongepowered.api.event.block.ChangeBlockEvent
import org.spongepowered.api.event.data.ChangeDataHolderEvent
import org.spongepowered.api.event.entity.*
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.item.inventory.DropItemEvent
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

class FreezerListener(val plugin: FreezerPlugin) {

    @Listener
    fun onTeleport(event: PlayerTeleportEvent, @First player: Player) {
        val frozen = plugin[player.uniqueId] ?: return
        frozen.location = event.getTo()
    }

    @Listener
    fun onMove(event: MoveEntityEvent, @First trg: Player) {
        val frozen = plugin[trg.uniqueId] ?: return
        if (!blockMoved(frozen.location, event.toTransform.location)) return
        trg.setLocationAndRotation(frozen.location, event.toTransform.rotation)
    }

    private fun blockMoved(f: Location<World>, t: Location<World>): Boolean {
        if (f.blockX != t.blockX) return true
        if (f.blockY != t.blockY) return true
        return f.blockZ != t.blockZ
    }

//    @EventHandler
//    fun onPluginDisable(e: PluginDisableEvent) {
//        if (e.getPlugin().getName() != eventHelper.plugin.getName()) return
//        for (frozen in this.map.values) {
//            val trg = Bukkit.getPlayer(frozen.getUuid()) ?: continue
//            FreezerMessages.unfreezeByReload(trg)
//        }
//    }

//    @Listener
//    fun onJoin(event: ClientConnectionEvent.Join, @First player: Player) {
//        val frozen = this.map.remove(player.uniqueId) ?: return
//        frozen.updateAll(player)
//        this.map.put(player.getEntityId(), frozen)
//        this.onFreezeOnline(frozen, player)
//        FreezerMessages.frozen(player)
//    }
//    @Listener
//    fun onQuit(event: ClientConnectionEvent.Disconnect, @First player: Player) {
//        val frozen = this.map.get(player.getEntityId()) ?: return
//        this.map.put(player.getUniqueId(), frozen)
//    }

//    protected fun onFreezeOnline(frozen: FrozenPlayers.Frozen, trg: Player) {
//        this.teleportFrozen(frozen, trg)
//        this.freezeFlayers.setFly(trg)
//    }
//
//    protected fun onUnfreezeOnline(frozen: FrozenPlayers.Frozen, trg: Player) {
//        this.freezeFlayers.removeFly(trg)
//        frozen.recoverVelocity(trg)
//    }
//
//    protected fun onFreezeOffline(frozen: FrozenPlayers.Frozen) {
//        this.freezeFlayers.setFlyFuture(frozen.uuid)
//    }
//
//    protected fun onUnfreezeOffline(frozen: FrozenPlayers.Frozen) {
//        this.freezeFlayers.removeFlyFuture(frozen.uuid)
//    }

    //////////////////////////////HOLDER/////////////////////////////////

    //любые взаимодействия
    @Listener
    fun onInteract(e: InteractEvent, @First player: Player) {
        val frozen = plugin[player.uniqueId] ?: return
        e.isCancelled = true
    }

    //ломать и ставить блоки
    @Listener
    fun onBlockBreak(e: ChangeBlockEvent, @First player: Player) {
        val frozen = plugin[player.uniqueId] ?: return
        e.isCancelled = true
    }

    //садиться на шифт
    fun DataTransactionResult.containsKey(vararg keys: Key<*>): Boolean {
        for(data in successfulData) {
            if(!keys.contains(data.key)) continue
            data as ImmutableValue<Boolean>
            if(!data.get()) continue
            return true
        }
        return false
    }
    @Listener
    fun onValueChange(e: ChangeDataHolderEvent.ValueChange, @First player: Player) {
        val frozen = plugin[player.uniqueId] ?: return
        if(e.endResult.containsKey(Keys.IS_SNEAKING, Keys.FOOD_LEVEL)) return
        e.isCancelled = true
    }

    //наносить и получать урон
    @Listener
    fun onAttack(e: AttackEntityEvent, @First player: Player) {
        val sourceFrozen = plugin[player.uniqueId]
        if(sourceFrozen != null) {
            e.isCancelled = true
            return
        }
        if(e.targetEntity.type != EntityTypes.PLAYER) return
        val target = e.targetEntity as Player
        val targetFrozen = plugin[target.uniqueId]
        if(targetFrozen != null) {
            e.isCancelled = true
            return
        }
    }
    @Listener
    fun onDamage(e: DamageEntityEvent, @First player: Player) {
        val frozen = plugin[player.uniqueId] ?: return
        e.isCancelled = true
    }


    //выкидывать и подбирать вещи
    @Listener
    fun onDrop(e: DropItemEvent, @First player: Player) {
        val frozen = plugin[player.uniqueId] ?: return
        e.isCancelled = true
    }

    @Listener
    fun onPickup(event: CollideEntityEvent, @First player: Player) {
        val frozen = plugin[player.uniqueId] ?: return
        val entityItems = event.filterEntities { it !is Item }
    }

    //использовать транспорт
    @Listener
    fun enterVehicle(e: RideEntityEvent, @First player: Player) {
        val frozen = plugin[player.uniqueId] ?: return
        e.isCancelled = true
    }


//
//    //открывать и использовать инвентарь
//    @EventHandler
//    fun onInventory(e: InventoryClickEvent) {
//        if (e.getWhoClicked().getType() != EntityType.PLAYER) return
//        val frozen = this.map.get(e.getWhoClicked().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onOpen(e: InventoryOpenEvent) {
//        val frozen = this.map.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun sign(e: SignChangeEvent) {
//        val frozen = this.map.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun blockDamage(e: BlockDamageEvent) {
//        val frozen = this.map.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun enchantItem(e: EnchantItemEvent) {
//        val frozen = this.map.get(e.getEnchanter().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onPrepareEnchant(e: PrepareItemEnchantEvent) {
//        val frozen = this.map.get(e.getEnchanter().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onBreakHanging(e: HangingBreakByEntityEvent) {
//        if (e.getRemover().getType() != EntityType.PLAYER) return
//        val frozen = this.map.get(e.getRemover().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onPlaceHanging(e: HangingPlaceEvent) {
//        val frozen = this.map.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onCraft(e: CraftItemEvent) {
//        if (e.getWhoClicked().getType() != EntityType.PLAYER) return
//        val frozen = this.map.get(e.getWhoClicked().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onFish(e: PlayerFishEvent) {
//        val frozen = this.map.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }
//
//    @EventHandler
//    fun onItemConsume(e: PlayerItemConsumeEvent) {
//        val frozen = this.map.get(e.getPlayer().getEntityId()) ?: return
//        e.setCancelled(true)
//    }



}
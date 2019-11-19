package dialight.guilib

import dialight.extensions.*
import dialight.guilib.events.GuiOutsideClickEvent
import dialight.guilib.events.ItemClickEvent
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.entity.living.humanoid.player.PlayerChangeClientSettingsEvent
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.item.inventory.AffectSlotEvent
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.item.inventory.property.SlotIndex
import org.spongepowered.api.item.inventory.query.QueryOperationTypes
import org.spongepowered.api.scheduler.Task
import kotlin.streams.toList


class GuiListener(val plugin: GuiPlugin) {

    fun forEachItem(
        view: View,
        event: ClickInventoryEvent,
        op: (View.Item, Int) -> ItemClickEvent
    ): Boolean {
//        event.dump()
        val slots = event.transactions.stream().map{ it.slot }.toList()
        if (slots.isEmpty()) return false
        for(slot in slots) {
            val index = slot.getInventoryProperty(SlotIndex::class.java).getOrNull()?.value ?: continue
            if (index >= view.capacity) continue
            event.isCancelled = true
            val item = view[index] ?: continue
            val guiEvent = op(item, index)
            item.onClick(guiEvent)
            if(guiEvent.updateItem) {
                Task.builder().execute { task ->
                    view[index] = item
                    event.targetInventory[index] = item.item
                }.submit(plugin)
            }
        }
        return true
    }

    @Listener
    fun onItemDropSingleClick(event: ClickInventoryEvent.Drop.Single, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
        forEachItem(view, event) { item, index -> ItemClickEvent.DropSingle(event, player, gui, view, item, index) }
    }

    @Listener
    fun onItemDropOutsideLeftClick(event: ClickInventoryEvent.Drop.Outside.Primary, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
//        forEachItem(gui, event, player) { ItemClickEvent.DropOutsideLeft(event, player, gui, slot, index) }
//        println(player.get(Keys.IS_SNEAKING).orElse(false))
//        for(key in player.keys) {
//            val value = player.getValue(key as Key<BaseValue<Any>>)
//            println(" key: $key val: $value")
//        }
//        for(prop in player.applicableProperties) {
//            println(" prop: $prop  key: ${prop.key}  value: ${prop.value}")
//        }
//        for(cont in player.containers) {
//            println(" cont: $cont")
//        }
//        if(player.get(Keys.IS_SNEAKING).orElse(false)) {
//            view.onOutsideClick(GuiOutsideClickEvent.ShiftLeft(player))
//        } else {
//        }
        view.onOutsideClick(GuiOutsideClickEvent.Left(player))
    }

    @Listener
    fun onItemDropOutsideRightClick(event: ClickInventoryEvent.Drop.Outside.Secondary, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
//        forEachItem(gui, event, player) { ItemClickEvent.DropOutsideRight(event, player, gui, view, item, index) }
//        if(player.get(Keys.IS_SNEAKING).orElse(false)) {
//            view.onOutsideClick(GuiOutsideClickEvent.ShiftRight(player))
//        } else {
//        }
        view.onOutsideClick(GuiOutsideClickEvent.Right(player))
    }

    @Listener
    fun onItemDropFullClick(event: ClickInventoryEvent.Drop.Full, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
        forEachItem(view, event) { item, index -> ItemClickEvent.DropFull(event, player, gui, view, item, index) }
    }

    @Listener
    fun onItemRecipeSingleClick(event: ClickInventoryEvent.Recipe.Single, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
        forEachItem(view, event) { item, index -> ItemClickEvent.RecipeSingle(event, player, gui, view, item, index) }
    }

    @Listener
    fun onItemRecipeAllClick(event: ClickInventoryEvent.Recipe.All, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
        forEachItem(view, event) { item, index -> ItemClickEvent.RecipeAll(event, player, gui, view, item, index) }
    }

    @Listener
    fun onItemNumberPressClick(event: ClickInventoryEvent.NumberPress, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
        forEachItem(view, event) { item, index -> ItemClickEvent.NumberPress(event, player, gui, view, item, index, event.number) }
    }

    @Listener
    fun onCreativeClick(event: ClickInventoryEvent.Creative, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
        forEachItem(view, event) { item, index -> ItemClickEvent.Creative(event, player, gui, view, item, index) }
    }

    @Listener
    fun onDoubleClick(event: ClickInventoryEvent.Double, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
        forEachItem(view, event) { item, index -> ItemClickEvent.Double(event, player, gui, view, item, index) }
    }

    @Listener
    fun onDragMiddleClick(event: ClickInventoryEvent.Drag.Middle, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
        forEachItem(view, event) { item, index -> ItemClickEvent.DragMiddle(event, player, gui, view, item, index) }
    }

    @Listener
    fun onDragLeftClick(event: ClickInventoryEvent.Drag.Primary, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
        forEachItem(view, event) { item, index -> ItemClickEvent.DragLeft(event, player, gui, view, item, index) }
    }

    @Listener
    fun onDragRightClick(event: ClickInventoryEvent.Drag.Secondary, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
        forEachItem(view, event) { item, index -> ItemClickEvent.DragRight(event, player, gui, view, item, index) }
    }

    @Listener
    fun onMiddleClick(event: ClickInventoryEvent.Middle, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
        if(!forEachItem(view, event) { item, index -> ItemClickEvent.Middle(event, player, gui, view, item, index) }) {
            view.onOutsideClick(GuiOutsideClickEvent.Middle(player))
        }
    }

    @Listener
    fun onLeftClick(event: ClickInventoryEvent.Primary, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
        forEachItem(view, event) { item, index -> ItemClickEvent.Left(event, player, gui, view, item, index) }
    }

    @Listener
    fun onRightClick(event: ClickInventoryEvent.Secondary, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
        forEachItem(view, event) { item, index -> ItemClickEvent.Right(event, player, gui, view, item, index) }
    }

    @Listener
    fun onShiftPrimaryClick(event: ClickInventoryEvent.Shift.Primary, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
        forEachItem(view, event) { item, index -> ItemClickEvent.ShiftLeft(event, player, gui, view, item, index) }
    }

    @Listener
    fun onShiftRightClick(event: ClickInventoryEvent.Shift.Secondary, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        val view = gui.getViewOf(player)
        forEachItem(view, event) { item, index -> ItemClickEvent.ShiftRight(event, player, gui, view, item, index) }
    }

    @Listener
    fun onInventoryOpen(event: InteractInventoryEvent.Open) {
//        val player = event.targetInventory.viewers.firstOrNull() ?: return
//        val gui = plugin.currentGui(player, event.targetInventory) ?: return
    }

    @Listener
    fun onInventoryClose(event: InteractInventoryEvent.Close, @First player: Player) {
        val gui = plugin.currentGui(player, event.targetInventory) ?: return
        if(gui.onCloseView(player)) {
            plugin.guistory.onCloseGui(player, event.targetInventory)
            plugin.guimap.closeInventory(gui, player)
        }
    }

    @Listener
    fun onPlayerChangeSettings(event: PlayerChangeClientSettingsEvent) {

    }

//    @Listener
//    fun test(event: AffectSlotEvent) {
//        event.dump()
//    }

}
package dialight.guilib.events

import dialight.guilib.Gui
import dialight.guilib.View
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent
import org.spongepowered.api.item.inventory.Slot

open class ItemClickEvent private constructor(
    val event: ClickInventoryEvent,
    val player: Player,
    val gui: Gui,
    val view: View,
    val item: View.Item,
    val index: Int,
    val type: Type
) {

    var updateItem = false



    class DropSingle(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.DROP_SINGLE)
    class DropOutsideLeft(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.DROP_OUTSIDE_LEFT)
    class DropOutsideRight(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.DROP_OUTSIDE_RIGHT)
    class DropFull(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.DROP_FULL)
    class RecipeSingle(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.RECIPE_SINGLE)
    class RecipeAll(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.RECIPE_ALL)
    class NumberPress(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int, val number: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.NUMBER_PRESS)
    class Creative(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.CREATIVE)
    class Double(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.DOUBLE)
    class DragMiddle(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.DRAG_MIDDLE)
    class DragLeft(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.DRAG_LEFT)
    class DragRight(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.DRAG_RIGHT)
    class Middle(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.MIDDLE)
    class Left(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.LEFT)
    class Right(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.RIGHT)
    class ShiftLeft(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.SHIFT_LEFT)
    class ShiftRight(event: ClickInventoryEvent, player: Player, gui: Gui, view: View, item: View.Item, index: Int) : ItemClickEvent(event, player, gui, view, item, index, Type.SHIFT_RIGHT)

    enum class Type {
        DROP_SINGLE,
        DROP_OUTSIDE_LEFT,
        DROP_OUTSIDE_RIGHT,
        DROP_FULL,
        RECIPE_SINGLE,
        RECIPE_ALL,
        NUMBER_PRESS,
        CREATIVE,
        DOUBLE,
        DRAG_MIDDLE,
        DRAG_LEFT,
        DRAG_RIGHT,
        MIDDLE,
        LEFT,
        RIGHT,
        SHIFT_LEFT,
        SHIFT_RIGHT,
    }

    override fun toString(): String {
        return "ItemClickEvent(index=$index, type=$type)"
    }

}
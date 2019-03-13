package dialight.guilib.events

import org.spongepowered.api.entity.living.player.Player

abstract class GuiOutsideClickEvent(val player: Player, val type: Type) {

    class Left(player: Player) : GuiOutsideClickEvent(player, Type.LEFT)
    class Right(player: Player) : GuiOutsideClickEvent(player, Type.RIGHT)
    class Middle(player: Player) : GuiOutsideClickEvent(player, Type.MIDDLE)

//    class ShiftLeft(player: Player) : GuiOutsideClickEvent(player, Type.SHIFT_LEFT)
//    class ShiftRight(player: Player) : GuiOutsideClickEvent(player, Type.SHIFT_RIGHT)
//    class ShiftMiddle(player: Player) : GuiOutsideClickEvent(player, Type.SHIFT_MIDDLE)

    enum class Type {
        LEFT,
        MIDDLE,
        RIGHT,
//        SHIFT_LEFT,
//        SHIFT_MIDDLE,
//        SHIFT_RIGHT,
    }

}
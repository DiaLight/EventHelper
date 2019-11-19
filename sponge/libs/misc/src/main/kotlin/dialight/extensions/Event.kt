package dialight.extensions

import org.spongepowered.api.event.Event
import org.spongepowered.api.event.block.ChangeBlockEvent
import org.spongepowered.api.event.item.inventory.AffectSlotEvent
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent

fun Event.dumpClass() {
    println(this.javaClass.simpleName + " " + this)
}
fun Event.dumpSrc() {
    println(" src: ${this.source}")
}
fun Event.dumpCtx() {
    for((k, v) in this.context.asMap()) {
        println(" ctx: $k: $v")
    }
}
fun Event.dumpCause() {
    for(c in this.cause.all()){
        println(" cause: $c")
    }
}
fun InteractInventoryEvent.dumpCursor() {
    println(" cursor: $cursorTransaction")
}
fun AffectSlotEvent.dumpTransactions() {
    for(tr in this.transactions){
        println(" transaction: $tr")
    }
}

fun ChangeBlockEvent.dumpTransactions() {
    for(tr in transactions) {
        println(" transaction: $tr")
        println("  valid: " + tr.isValid + "  ver: " + tr.contentVersion)
        println("  orig: " + tr.original)
        println("  custom: " + tr.custom)
        println("  default: " + tr.default)
        println("  final: " + tr.final)
    }
}
fun Event.dump() {
    dumpClass()
    dumpCtx()
    dumpSrc()
    dumpCause()
}
fun ClickInventoryEvent.dump() {
    dumpClass()
    dumpCtx()
    dumpSrc()
    dumpCause()
    dumpCursor()
    dumpTransactions()
}
fun ChangeBlockEvent.dump() {
    dumpClass()
    dumpCtx()
    dumpSrc()
    dumpCause()
    dumpTransactions()
}

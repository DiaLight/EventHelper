package dialight.toollib

import com.google.common.reflect.TypeToken
import com.google.inject.Inject
import dialight.observable.map.observableMapOf
import jekarus.colorizer.Text_colorized
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.data.DataRegistration
import org.spongepowered.api.data.key.Key
import org.spongepowered.api.data.manipulator.DataManipulator
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator
import org.spongepowered.api.data.type.HandTypes
import org.spongepowered.api.data.value.BaseValue
import org.spongepowered.api.data.value.mutable.Value
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.GameRegistryEvent
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.plugin.PluginManager
import java.util.*

@Plugin(id = "toollib")
class ToolPlugin @Inject constructor(
    val container: PluginContainer,
    val logger: Logger
) {

    val toolregistry = observableMapOf<String, Tool>()

    fun registerTool(tool: Tool) {
        toolregistry[tool.id] = tool
    }

    fun getTool(id: String) = toolregistry[id]
    fun giveTool(player: Player, id: String): Boolean {
        val tool = getTool(id)
        if(tool == null) {
            player.sendMessage(Text_colorized("|r|Инструмент с ID «$id» не зарегистрирован"))
            return false
        }

        // TODO("Clear player inventory from item")
//                player.inventory.remove(tool.item().get())

        player.setItemInHand(HandTypes.MAIN_HAND, tool.buildItem())
        return true
    }

    @Listener
    fun onServerStart(event: GameStartedServerEvent) {
        Sponge.getEventManager().registerListeners(this, ToolListener(this))
        logger.info("ToolLib v${container.version.orElse("null")} has been Enabled")
    }

    private var UNIQUE_ID_REGISTRATION: DataRegistration<UniqueIdData, UniqueIdData.Immutable>? = null

    @Listener
    fun onKeyRegistration(e: GameRegistryEvent.Register<Key<*>>) {
        ITEM_ID = Key.builder()
            .type(object : TypeToken<Value<UUID>>() {})
            .id("item_id")
            .name("Item id")
            .query(DataQuery.of("ItemId"))
            .build()
//        println("onKeyRegistration: $ITEM_ID")
    }

    @Listener
    fun onDataRegistration(e: GameRegistryEvent.Register<DataRegistration<*, *>>) {
        this.UNIQUE_ID_REGISTRATION = DataRegistration.builder()
            .manipulatorId("item_id")
            .dataName("Item id data")
            .dataClass(UniqueIdData::class.java)
            .immutableClass(UniqueIdData.Immutable::class.java)
            .dataImplementation(UniqueIdDataImpl::class.java)
            .immutableImplementation(UniqueIdDataImpl.Immutable::class.java)
            .builder(UniqueIdDataImpl.Builder())
            .buildAndRegister(container)
//        println("onDataRegistration: $UNIQUE_ID_REGISTRATION")
    }

}

fun <E, V : BaseValue<E>> Key.Builder<E, V>.test(): Key.Builder<E, V> {
    println(this.javaClass.name)
    for(m in this.javaClass.methods) {
        println(String.format(" %s(%s)", m.name, m.parameters.map { String.format("%s: %s", it.name, it.type.name) }.joinToString(", ")))
    }
    return this
}

fun <E, V : BaseValue<E>> Key.Builder<E, V>.id_rfl(arg0: String): Key.Builder<E, V> {
    val id = this.javaClass.getDeclaredMethod("id", java.lang.String::class.java)
    id.invoke(this, arg0)
    return this
}

fun <E, V : BaseValue<E>> Key.Builder<E, V>.name_rfl(arg0: String): Key.Builder<E, V> {
    val name = this.javaClass.getDeclaredMethod("name", java.lang.String::class.java)
    name.invoke(this, arg0)
    return this
}

fun <T : DataManipulator<T, I>, I : ImmutableDataManipulator<I, T>> DataRegistration.Builder<T, I>.test(): DataRegistration.Builder<T, I> {
    println(this.javaClass.name)
    for(m in this.javaClass.methods) {
        println(String.format(" %s(%s)", m.name, m.parameters.joinToString(", ") { String.format("%s: %s", it.name, it.type.name) }))
    }
    return this
}

fun <T : DataManipulator<T, I>, I : ImmutableDataManipulator<I, T>> DataRegistration.Builder<T, I>.manipulatorId_rfl(arg0: String): DataRegistration.Builder<T, I> {
    val manipulatorId = this.javaClass.getDeclaredMethod("manipulatorId", java.lang.String::class.java)
    manipulatorId.invoke(this, arg0)
    return this
}

fun <T : DataManipulator<T, I>, I : ImmutableDataManipulator<I, T>> DataRegistration.Builder<T, I>.dataName_rfl(name: String): DataRegistration.Builder<T, I> {
    val dataName = this.javaClass.getDeclaredMethod("dataName", java.lang.String::class.java)
    dataName.invoke(this, name)
    return this
}

fun <T : DataManipulator<T, I>, I : ImmutableDataManipulator<I, T>> DataRegistration.Builder<T, I>.buildAndRegister_rfl(container: PluginContainer): DataRegistration<T, I> {
    val buildAndRegister = this.javaClass.getDeclaredMethod("buildAndRegister", PluginContainer::class.java)
    return buildAndRegister(this, container) as DataRegistration<T, I>
}

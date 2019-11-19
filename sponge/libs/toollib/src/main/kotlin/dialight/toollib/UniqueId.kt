package dialight.toollib

import org.spongepowered.api.Sponge
import org.spongepowered.api.data.*
import org.spongepowered.api.data.key.Key
import org.spongepowered.api.data.manipulator.DataManipulator
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData
import org.spongepowered.api.data.merge.MergeFunction
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import org.spongepowered.api.data.persistence.InvalidDataException
import java.util.*
import java.util.UUID
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData
import org.spongepowered.api.data.value.immutable.ImmutableValue
import org.spongepowered.api.data.value.mutable.Value
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider

var ITEM_ID: Key<Value<UUID>> = DummyObjectProvider.createExtendedFor(Key::class.java, "ITEM_ID")

fun uidprinln(o: Any) {
//    println(o)
}

class UniqueIdDataImpl(id: UUID) : AbstractSingleData<UUID, UniqueIdData, UniqueIdData.Immutable>(id, ITEM_ID), UniqueIdData {

    override fun toContainer(): DataContainer {
        val container = super.toContainer()
        container.set(ITEM_ID.query, value)
        return container
    }

    override fun getValueGetter(): Value<*> {
        return Sponge.getRegistry().valueFactory.createValue(ITEM_ID, value)
    }

    constructor() : this(UUID.randomUUID())

    override fun id(): Value<UUID> = Sponge.getRegistry().valueFactory.createValue(ITEM_ID, value)

    override fun asImmutable() = Immutable(value)

    override fun fill(dataHolder: DataHolder, overlap: MergeFunction): Optional<UniqueIdData> {
        val opt = dataHolder.get(UniqueIdData::class.java)
        if(opt.isPresent) {
            val data = opt.get()
            val merged: UniqueIdData = overlap.merge(this, data)
            value = merged.id().get()
    }
        uidprinln("Mut.fill $dataHolder")
        return Optional.of(this)
    }

    override fun from(container: DataContainer): Optional<UniqueIdData> {
        uidprinln("Mut.from $container")
        if (container.contains(ITEM_ID)) {
            value = container.getObject(ITEM_ID.query, UUID::class.java).get()
            return Optional.of(this)
        }
        return Optional.empty()
    }

    override fun copy(): UniqueIdData = UniqueIdDataImpl(value)

    override fun getContentVersion() = UniqueIdData.Builder.CONTENT_VERSION

    class Immutable(id: UUID) : AbstractImmutableSingleData<UUID, UniqueIdData.Immutable, UniqueIdData>(id, ITEM_ID), UniqueIdData.Immutable {

        val id_value: ImmutableValue<UUID> = Sponge.getRegistry().valueFactory.createValue(ITEM_ID, value).asImmutable()

        override fun id() = id_value

        override fun getValueGetter() = id_value
        override fun asMutable(): UniqueIdDataImpl = UniqueIdDataImpl(value)
        override fun getContentVersion(): Int = UniqueIdData.Builder.CONTENT_VERSION
    }

    class Builder : AbstractDataBuilder<UniqueIdData>(
        UniqueIdData::class.java,
        UniqueIdData.Builder.CONTENT_VERSION
    ), UniqueIdData.Builder {

        var id: UUID? = null

        override fun id(id: UUID) {
            this.id = id
        }

        override fun create(): UniqueIdDataImpl {
            uidprinln("Bld.create")
            return UniqueIdDataImpl(id ?: UUID.randomUUID())
        }

        override fun createFrom(dataHolder: DataHolder): Optional<UniqueIdData> {
            uidprinln("Bld.createFrom $dataHolder")
            return create().fill(dataHolder)
        }

        @Throws(InvalidDataException::class)
        override fun buildContent(container: DataView): Optional<UniqueIdData> {
            uidprinln("Bld.buildContent $container")
            if(container is DataContainer) return create().from(container)
            if (container.contains(ITEM_ID)) {
                this.id = container.getObject(ITEM_ID.query, UUID::class.java).get()
                return Optional.of(create())
            }
            return Optional.empty()
//            if (!container.contains(ITEM_ID)) return Optional.empty()
//            val id = container.getObject(ITEM_ID.query, UUID::class.java).get()
//            return Optional.of(UniqueIdDataImpl(id))
        }
    }

}


interface UniqueIdData : DataManipulator<UniqueIdData, UniqueIdData.Immutable> {
    fun id(): Value<UUID>

    interface Immutable : ImmutableDataManipulator<Immutable, UniqueIdData> {
        fun id(): ImmutableValue<UUID>
    }

    interface Builder : DataManipulatorBuilder<UniqueIdData, Immutable> {

        fun id(id: UUID)

        companion object {
            val CONTENT_VERSION = 1
        }
    }
}

package dialight.observable

import java.util.*

class ObservableObject<V>(value: V) {

    private val onChange: MutableCollection<(V, V) -> Unit> = LinkedList()

    protected fun fireChange(fr: V, to: V) {
        for(op in onChange) op(fr, to)
    }

    fun onChange(op: (V, V) -> Unit) : ObservableObject<V> {
        onChange += op
        return this
    }

    var value: V = value
        set(value) {
            val oldValue = field
            field = value
            fireChange(oldValue, value)
        }

}
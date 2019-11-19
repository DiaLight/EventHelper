package dialight.observable.map

import java.util.*

abstract class ObservableMap<K, V> {

    private val onPut: MutableCollection<(K, V) -> Unit> = LinkedList()
    private val onRemove: MutableCollection<(K, V) -> Unit> = LinkedList()
    private val onReplace: MutableCollection<(K, V, V) -> Unit> = LinkedList()


    protected fun firePut(k: K, v: V) {
        for(op in onPut) op(k, v)
    }
    protected fun fireRemove(k: K, v: V) {
        for(op in onRemove) op(k, v)
    }
    protected fun fireReplace(k: K, old: V, new: V) {
        for(op in onReplace) op(k, old, new)
    }


    fun onPut(op: (K, V) -> Unit) : ObservableMap<K, V> {
        onPut += op
        return this
    }
    fun onRemove(op: (K, V) -> Unit) : ObservableMap<K, V> {
        onRemove += op
        return this
    }
    fun onReplace(op: (key: K, old: V, new: V) -> Unit) : ObservableMap<K, V> {
        onReplace += op
        return this
    }

    abstract fun put(key: K, value: V): V?
    abstract fun remove(key: K): V?

}
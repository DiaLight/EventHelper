package dialight.observable.map

open class ObservableMapWrapper<K, V>(
    val map: MutableMap<K, V> = hashMapOf()
) : ObservableMap<K, V>(), MutableMap<K, V> {

    override val size = map.size
    override fun containsKey(key: K) = map.containsKey(key)
    override fun containsValue(value: V) = map.containsValue(value)
    override fun get(key: K) = map.get(key)

    override fun isEmpty() = map.isEmpty()

    override val entries = map.entries
    override val keys = map.keys
    override val values = map.values

    override fun clear() {
        for ((k, v) in map) {
            fireRemove(k, v)
        }
        return map.clear()
    }

    override fun put(key: K, value: V): V? {
        val old = map.put(key, value)
        if(old != null) {
            fireReplace(key, old, value)
        } else {
            firePut(key, value)
        }
        return old
    }

    override fun putAll(from: Map<out K, V>) {
        for((k, v) in from) {
            put(k, v)
        }
    }

    override fun remove(key: K): V? {
        val rem = map.remove(key)
        if(rem != null) {
            fireRemove(key, rem)
        }
        return rem
    }

}
fun <K, V> observableMapOf(vararg items: Pair<K, V>) = ObservableMapWrapper(mutableMapOf(*items))
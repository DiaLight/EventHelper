package dialight.observable.map;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ObservableMapWrapper<K, V> extends ObservableMap<K, V> {

    private final Map<K, V> map;

    public ObservableMapWrapper(Map<K, V> map) {
        this.map = map;
    }
    public ObservableMapWrapper() {
        this(new HashMap<>());
    }


    @Override public int size() { return map.size(); }
    @Override public boolean containsKey(Object key) { return map.containsKey(key); }
    @Override public boolean containsValue(Object value) { return map.containsValue(value); }
    @Override public V get(Object key) { return map.get(key); }

    @Override public boolean isEmpty() { return map.isEmpty(); }

    @NotNull @Override public Set<Entry<K, V>> entrySet() { return map.entrySet(); }
    @NotNull @Override public Set<K> keySet() { return map.keySet(); }
    @NotNull @Override public Collection<V> values() { return map.values(); }

    @Override public void clear() {
        for (Entry<K, V> entry : map.entrySet()) {
            fireRemove(entry.getKey(), entry.getValue());
        }
        map.clear();
    }

    @Override public V put(K key, V value) {
        V old = map.put(key, value);
        if(old != null) {
            fireReplace(key, old, value);
        } else {
            firePut(key, value);
        }
        return old;
    }

    @Override public void putAll(Map<? extends K, ? extends V> from) {
        for(Entry<? extends K, ? extends V> entry : from.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override public V remove(Object key) {
        V rem = map.remove(key);
        if(rem != null) {
            fireRemove((K) key, rem);
        }
        return rem;
    }

}

package dialight.observable.map;

import dialight.function.A3Consumer;
import dialight.observable.collection.ObservableCollection;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class ObservableMap<K, V> implements Map<K, V> {

    private final Map<Object, BiConsumer<K, V>> onPut = new LinkedHashMap<>();
    private final Map<Object, BiConsumer<K, V>> onRemove = new LinkedHashMap<>();
    private final Map<Object, A3Consumer<K, V, V>> onReplace = new LinkedHashMap<>();


    protected void firePut(K k, V v) {
        for(BiConsumer<K, V> op : onPut.values()) op.accept(k, v);
    }
    protected void fireRemove(K k, V v) {
        for(BiConsumer<K, V> op : onRemove.values()) op.accept(k, v);
    }
    protected void fireReplace(K k, V oldValue, V newValue) {
        for(A3Consumer<K, V, V> op : onReplace.values()) op.apply(k, oldValue, newValue);
    }

    public ObservableMap<K, V> onPut(Object key, BiConsumer<K, V> op) {
        onPut.put(key, op);
        return this;
    }
    public ObservableMap<K, V> onRemove(Object key, BiConsumer<K, V> op) {
        onRemove.put(key, op);
        return this;
    }
    public ObservableMap<K, V> onReplace(Object key, A3Consumer<K, V, V> op) {
        onReplace.put(key, op);
        return this;
    }

    public ObservableMap<K, V> removeListeners(Object key) {
        onPut.remove(key);
        onRemove.remove(key);
        onReplace.remove(key);
        return this;
    }

//    public abstract V put(K key, V value);
//    public abstract V remove(K key);

    public ObservableCollection<V> asCollectionObaervable(Function<V, K> getKey) {
        return new ValuesObservable<>(this, getKey);
    }
    public ObservableCollection<V> asImmutableCollectionObaervable(Function<V, K> getKey) {
        return new ValuesImmutableObservable<>(this, getKey);
    }

}

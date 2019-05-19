package dialight.observable.map;

import dialight.function.A3Consumer;
import dialight.observable.collection.ObservableCollection;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class ObservableMap<K, V> implements Map<K, V> {

    private final Collection<BiConsumer<K, V>> onPut = new LinkedList<>();
    private final Collection<BiConsumer<K, V>> onRemove = new LinkedList<>();
    private final Collection<A3Consumer<K, V, V>> onReplace = new LinkedList<>();


    protected void firePut(K k, V v) {
        for(BiConsumer<K, V> op : onPut) op.accept(k, v);
    }
    protected void fireRemove(K k, V v) {
        for(BiConsumer<K, V> op : onRemove) op.accept(k, v);
    }
    protected void fireReplace(K k, V oldValue, V newValue) {
        for(A3Consumer<K, V, V> op : onReplace) op.apply(k, oldValue, newValue);
    }


    public ObservableMap<K, V> onPut(BiConsumer<K, V> op) {
        onPut.add(op);
        return this;
    }
    public ObservableMap<K, V> onRemove(BiConsumer<K, V> op) {
        onRemove.add(op);
        return this;
    }
    public ObservableMap<K, V> onReplace(A3Consumer<K, V, V> op) {
        onReplace.add(op);
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

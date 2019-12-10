package dialight.observable.map;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;

public class WriteProxyObservableMap<K, V> extends ObservableMapWrapper<K, V> {

    private BiFunction<K, V, V> onProxyPut = null;
    private Function<K, V> onProxyRemove = null;

    public WriteProxyObservableMap(ObservableMap<K, V> collection) {
        super(collection);
        collection.onPut(this, this::firePut);
        collection.onRemove(this, this::fireRemove);
        collection.onReplace(this, this::fireReplace);
    }

    protected V fireProxyPut(K k,V v) {
        if(onProxyPut == null) return null;
        try {
            return onProxyPut.apply(k, v);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    protected V fireProxyRemove(K k) {
        if(onProxyRemove == null) return null;
        try {
            return onProxyRemove.apply(k);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ObservableMap<K, V> setProxyOnPut(BiFunction<K, V, V> onProxyPut) {
        this.onProxyPut = onProxyPut;
        return this;
    }
    public ObservableMap<K, V> setProxyOnRemove(Function<K, V> onProxyRemove) {
        this.onProxyRemove = onProxyRemove;
        return this;
    }

    @Override public V put(K key, V element) {
        return this.fireProxyPut(key, element);
    }

    @Override public V remove(Object element) {
        return this.fireProxyRemove((K) element);
    }

    @Override public void clear() {
        for (K key : new ArrayList<>(map.keySet())) {
            fireProxyRemove(key);  // can edit map
        }
    }

}

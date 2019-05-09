package dialight.observable.map;

import dialight.function.A3Consumer;
import dialight.observable.collection.ObservableCollection;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
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
        ValuesObservable observable = new ValuesObservable(getKey);
        onPut(observable.forwardAdd);
        onRemove(observable.forwardRemove);
        return observable;
    }

    private class ValuesObservable extends ObservableCollection<V> {

        private final Function<V, K> getKey;
        private final BiConsumer<K, V> forwardAdd = (k, v) -> this.fireAdd(v);
        private final BiConsumer<K, V> forwardRemove = (k, v) -> this.fireRemove(v);

        public ValuesObservable(Function<V, K> getKey) {
            this.getKey = getKey;
        }

        @Override
        public int size() {
            return ObservableMap.this.size();
        }

        @Override
        public boolean isEmpty() {
            return ObservableMap.this.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return ObservableMap.this.containsValue(o);
        }

        @NotNull
        @Override
        public Iterator<V> iterator() {
            return ObservableMap.this.values().iterator();
        }

        @NotNull
        @Override
        public Object[] toArray() {
            return ObservableMap.this.values().toArray();
        }

        @NotNull
        @Override
        public <T> T[] toArray(@NotNull T[] a) {
            return ObservableMap.this.values().toArray(a);
        }

        @Override
        public boolean add(V element) {
            ObservableMap.this.put(getKey.apply(element), element);
            return true;
        }

        @Override
        public boolean remove(Object element) {
            V oldValue = ObservableMap.this.remove(getKey.apply((V) element));
            return oldValue != null;
        }

        @Override
        public boolean containsAll(@NotNull Collection<?> c) {
            return ObservableMap.this.values().containsAll(c);
        }

        @Override
        public boolean addAll(@NotNull Collection<? extends V> c) {
            for (V e : c) add(e);
            return true;
        }

        @Override
        public boolean removeAll(@NotNull Collection<?> c) {
            for (Object e : c) remove(e);
            return false;
        }

        @Override
        public boolean retainAll(@NotNull Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            ObservableMap.this.clear();
        }

    }

}

package dialight.observable.set;

import dialight.observable.collection.ObservableCollection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;

public class WriteProxyObservableSet<V> extends ObservableSetWrapper<V> {

    private Function<V, Boolean> onProxyAdd = null;
    private Function<V, Boolean> onProxyRemove = null;

    public WriteProxyObservableSet(ObservableSet<V> collection) {
        super(collection);
        collection.onAdd(this, this::fireAdd);
        collection.onRemove(this, this::fireRemove);
    }

    protected boolean fireProxyAdd(V e) {
        if(onProxyAdd == null) return false;
        try {
            return onProxyAdd.apply(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    protected boolean fireProxyRemove(V e) {
        if(onProxyRemove == null) return false;
        try {
            return onProxyRemove.apply(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    public ObservableCollection<V> setProxyOnAdd(Function<V, Boolean> onProxyAdd) {
        this.onProxyAdd = onProxyAdd;
        return this;
    }
    public ObservableCollection<V> setProxyOnRemove(Function<V, Boolean> onProxyAdd) {
        this.onProxyRemove = onProxyAdd;
        return this;
    }

    @Override public boolean add(V element) {
        return this.fireProxyAdd(element);
    }

    @Override public boolean remove(Object element) {
        return this.fireProxyRemove((V) element);
    }

    @Override public void clear() {
        for (V e : new ArrayList<>(set)) {
            remove(e);  // can edit set
        }
    }

    @NotNull @Override public Iterator<V> iterator() {  // no remove iterator
        return new Iterator<V>() {
            private final Iterator<V> inner = set.iterator();
            private V lastElement = null;

            @Override public boolean hasNext() { return inner.hasNext(); }
            @Override public V next() {
                V element = inner.next();
                lastElement = element;
                return element;
            }

            @Override public void remove() {
                throw new UnsupportedOperationException("WriteProxyObservableSet does not support remove while iterating");
            }
        };
    }

}

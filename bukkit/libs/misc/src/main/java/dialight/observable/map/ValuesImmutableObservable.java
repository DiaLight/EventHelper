package dialight.observable.map;

import com.google.common.collect.ImmutableList;
import dialight.observable.collection.ObservableCollection;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

public class ValuesImmutableObservable<K, V> extends ObservableCollection<V> {

    private final ObservableMap<K, V> map;
    private final Function<V, K> getKey;

    public ValuesImmutableObservable(ObservableMap<K, V> map, Function<V, K> getKey) {
        this.map = map;
        this.getKey = getKey;
        map.onPut(this, (k, v) -> this.fireAdd(v));
        map.onRemove(this, (k, v) -> this.fireRemove(v));
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(getKey.apply((V) o));
    }

    @NotNull
    @Override
    public Iterator<V> iterator() {
        return ImmutableList.copyOf(map.values()).iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return map.values().toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return map.values().toArray(a);
    }

    @Override
    public boolean add(V element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (Object o : c) {
            if(!contains(o)) return false;
        }
        return true;
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
        throw new UnsupportedOperationException();
    }

}

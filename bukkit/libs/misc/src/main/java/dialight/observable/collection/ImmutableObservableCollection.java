package dialight.observable.collection;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;

public class ImmutableObservableCollection<V> extends ObservableCollection<V> {

    private final ObservableCollection<V> coll;

    public ImmutableObservableCollection(ObservableCollection<V> coll) {
        this.coll = coll;
        coll.onAdd(this::fireAdd);
        coll.onRemove(this::fireRemove);
    }

    @Override
    public int size() {
        return coll.size();
    }

    @Override
    public boolean isEmpty() {
        return coll.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return coll.contains(o);
    }

    @NotNull
    @Override
    public Iterator<V> iterator() {
        return ImmutableList.copyOf(coll).iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return coll.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return coll.toArray(a);
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

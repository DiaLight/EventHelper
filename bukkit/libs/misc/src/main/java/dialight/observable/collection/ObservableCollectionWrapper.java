package dialight.observable.collection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ObservableCollectionWrapper<E> extends ObservableCollection<E> {

    protected final Collection<E> collection;

    public ObservableCollectionWrapper(Collection<E> collection) {
        this.collection = collection;
    }
    public ObservableCollectionWrapper() {
        this(new ArrayList<>());
    }

    @Override public int size() { return collection.size(); }
    @Override public boolean contains(Object element) { return collection.contains(element); }
    @Override public boolean containsAll(Collection<?> elements) { return collection.containsAll(elements); }
    @Override public boolean isEmpty() { return collection.isEmpty(); }

    @Override public boolean add(E element) {
        boolean result = collection.add(element);
        if (result) {
            fireAdd(element);
        }
        return result;
    }

    @Override public boolean addAll(Collection<? extends E> elements) {
        boolean anyAdded = false;
        for (E e : elements) {
            if(add(e)) {
                anyAdded = true;
            }
        }
        return anyAdded;
    }

    @Override public void clear() {
        for (E e : collection) {
            fireRemove(e);
        }
        collection.clear();
    }


    @NotNull @Override public Iterator<E> iterator() {
        return new Iterator<E>() {
            private final Iterator<E> inner = collection.iterator();
            private E lastElement = null;

            @Override public boolean hasNext() { return inner.hasNext(); }
            @Override public E next() {
                E element = inner.next();
                lastElement = element;
                return element;
            }

            @Override public void remove() {
                inner.remove();
                fireRemove(lastElement);
            }

        };
    }

    @NotNull
    @Override public Object[] toArray() {
        return collection.toArray();
    }

    @NotNull
    @Override public <T> T[] toArray(@NotNull T[] a) {
        return collection.toArray(a);
    }

    @Override public boolean remove(Object element) {
        if(collection.remove(element)) {
            fireRemove((E) element);
            return true;
        }
        return false;
    }

    @Override public boolean removeAll(Collection<?> elements) {
        boolean anyRemoved = false;
        for (Object e : elements) {
            if(remove(e)) {
                anyRemoved = true;
            }
        }
        return anyRemoved;
    }

    @Override public boolean retainAll(Collection<?> elements) {
        throw new UnsupportedOperationException();
    }

}

package dialight.observable.set;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ObservableSetWrapper<E> extends ObservableSet<E> {

    protected final Set<E> set;

    public ObservableSetWrapper(Set<E> set) {
        this.set = set;
    }
    public ObservableSetWrapper() {
        this(new HashSet<>());
    }

    @Override public boolean add(E element) {
        boolean result = set.add(element);
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

    @SuppressWarnings("UNCHECKED_CAST")
    @Override public boolean remove(Object element) {
        boolean removed = set.remove(element);
        if(removed) {
            fireRemove((E) element);
        }
        return removed;
    }

    @SuppressWarnings("UNCHECKED_CAST")
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

    @Override public void clear() {
        for (E e : set) {
            fireRemove(e);
        }
        set.clear();
    }

    @Override public boolean isEmpty() { return set.isEmpty(); }
    @Override public boolean contains(Object element) { return set.contains(element); }
    @Override public boolean containsAll(Collection<?> elements) {
        return set.containsAll(elements);
    }

    /**
     * WARNING:
     * This iterator MAY have issues when it removes things, because there is no way to know if the iterator is done with its work.
     * It will not call onUpdate, and calls onRemove while iterating.
     *
     * YOU HAVE BEEN WARNED.
     */
    @NotNull @Override public Iterator<E> iterator() {
        return new Iterator<E>() {
            private final Iterator<E> inner = set.iterator();
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
    @Override public Object[] toArray() { return set.toArray(); }

    @NotNull
    @Override public <T> T[] toArray(@NotNull T[] a) { return set.toArray(a); }

    @Override public int size() { return set.size(); }

}

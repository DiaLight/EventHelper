package dialight.observable.list;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ObservableListWrapper<E> extends ObservableList<E> implements List<E> {

    private final List<E> list;

    public ObservableListWrapper(List<E> list) {
        this.list = list;
    }
    public ObservableListWrapper() {
        this(new ArrayList<>());
    }


    @Override public E set(int index, E element) {
        E old = list.get(index);
        list.set(index, element);
        fireChange(old, element, index);
        return element;
    }

    @Override public boolean add(E element) {
        boolean result = list.add(element);
        int index = list.size() - 1;
        if (result) {
            fireAdd(element, index);
        }
        return result;
    }

    @Override public void add(int index, E element) {
        list.add(index, element);
        fireAdd(element, index);
    }

    @Override public boolean addAll(Collection<? extends E> elements) {
        int index = list.size();
        for (E e : elements) {
            list.add(e);
            fireAdd(e, index);
            index++;
        }
        return true;
    }

    @Override public boolean addAll(int index, Collection<? extends E> elements) {
        int currentIndex = index;
        for (E e : elements) {
            list.add(currentIndex, e);
            fireAdd(e, currentIndex);
            currentIndex++;
        }
        return true;
    }

    @SuppressWarnings("UNCHECKED_CAST")
    @Override public boolean remove(Object element) {
        int index = indexOf(element);
        if (index == -1) return false;
        list.remove(index);
        fireRemove((E) element, index);
        return true;
    }

    @Override public E remove(int index) {
        E element = list.remove(index);
        fireRemove(element, index);
        return element;
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
        for (E e : list) {
            fireRemove(e, 0);
        }
        list.clear();
    }

    @Override public boolean isEmpty() { return list.isEmpty(); }
    @Override public boolean contains(Object element) { return list.contains(element); }
    @Override public boolean containsAll(Collection<?> elements) {
        return list.containsAll(elements);
    }
    @Override public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }
    @Override public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }
    /**
     * WARNING:
     * This iterator MAY have issues when it removes things, because there is no way to know if the iterator is done with its work.
     * It will not call onUpdate, and calls onRemove while iterating.
     *
     * YOU HAVE BEEN WARNED.
     */
    @Override public Iterator<E> iterator() {
        return new Iterator<E>() {
            private final Iterator<E> inner = list.iterator();
            private int lastIndex = -1;
            private E lastElement = null;

            @Override public boolean hasNext() { return inner.hasNext(); }
            @Override public E next() {
                E element = inner.next();
                lastElement = element;
                lastIndex++;
                return element;
            }

            @Override public void remove() {
                inner.remove();
                fireRemove(lastElement, lastIndex);
                lastIndex--;
            }

        };
    }

    @NotNull
    @Override public Object[] toArray() { return list.toArray(); }

    @NotNull
    @Override public <T> T[] toArray(@NotNull T[] a) { return list.toArray(a); }

    @Override public List<E> subList(int fromIndex, int toIndex) { return list.subList(fromIndex, toIndex); }
    @Override public E get(int index) { return list.get(index); }
    @Override public int indexOf(Object element) { return list.indexOf(element); }
    @Override public int lastIndexOf(Object element) { return list.lastIndexOf(element); }
    @Override public int size() { return list.size(); }

    @Override public void replace(List<E> list) {
        this.list.clear();
        this.list.addAll(list);
    }

    @Override public void move(int fromIndex, int toIndex) {
        E item = list.remove(fromIndex);
        list.add(toIndex, item);
        fireMove(item, fromIndex, toIndex);
    }

    @Override public void updateAt(int index) {
        this.set(index, this.get(index));
    }
    @Override public boolean update(E element) {
        int index = indexOf(element);
        if (index != -1)
            updateAt(index);
        return index != -1;
    }

}

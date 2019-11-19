package dialight.observable.list;

import dialight.function.A3Consumer;

import java.util.*;
import java.util.function.BiConsumer;

public abstract class ObservableList<E> implements List<E> {

    private final Map<Object, BiConsumer<E, Integer>> onAdd = new LinkedHashMap<>();
    private final Map<Object, BiConsumer<E, Integer>> onRemove = new LinkedHashMap<>();
    private final Map<Object, A3Consumer<E, E, Integer>> onChange = new LinkedHashMap<>();
    private final Map<Object, A3Consumer<E, Integer, Integer>> onMove = new LinkedHashMap<>();


    protected void fireAdd(E e, int i) {
        for(BiConsumer<E, Integer> op : onAdd.values()) op.accept(e, i);
    }
    protected void fireRemove(E e, int i) {
        for(BiConsumer<E, Integer> op : onRemove.values()) op.accept(e, i);
    }
    protected void fireChange(E oldValue, E newValue, int i) {
        for(A3Consumer<E, E, Integer> op : onChange.values()) op.apply(oldValue, newValue, i);
    }
    protected void fireMove(E e, int oldValue, int newValue) {
        for(A3Consumer<E, Integer, Integer> op : onMove.values()) op.apply(e, oldValue, newValue);
    }


    public ObservableList<E> onAdd(Object key, BiConsumer<E, Integer> op) {
        onAdd.put(key, op);
        return this;
    }
    public ObservableList<E> onRemove(Object key, BiConsumer<E, Integer> op) {
        onRemove.put(key, op);
        return this;
    }
    public ObservableList<E> onChange(Object key, A3Consumer<E, E, Integer> op) {
        onChange.put(key, op);
        return this;
    }
    public ObservableList<E> onMove(Object key, A3Consumer<E, Integer, Integer> op) {
        onMove.put(key, op);
        return this;
    }

    public ObservableList<E> removeListeners(Object key) {
        onAdd.remove(key);
        onRemove.remove(key);
        onChange.remove(key);
        onMove.remove(key);
        return this;
    }


    public abstract boolean add(E element);
    public abstract boolean remove(Object element);


    public abstract E set(int index, E element);
    public abstract E remove(int index);

    public abstract void move(int fromIndex, int toIndex);
    public abstract void replace(List<E> list);
    public abstract void updateAt(int index);
    public abstract boolean update(E element);

}

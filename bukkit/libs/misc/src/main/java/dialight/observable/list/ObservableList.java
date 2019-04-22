package dialight.observable.list;

import dialight.function.A3Consumer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class ObservableList<E> {

    private final Collection<BiConsumer<E, Integer>> onAdd = new LinkedList<>();
    private final Collection<BiConsumer<E, Integer>> onRemove = new LinkedList<>();
    private final Collection<A3Consumer<E, E, Integer>> onChange = new LinkedList<>();
    private final Collection<A3Consumer<E, Integer, Integer>> onMove = new LinkedList<>();


    protected void fireAdd(E e, int i) {
        for(BiConsumer<E, Integer> op : onAdd) op.accept(e, i);
    }
    protected void fireRemove(E e, int i) {
        for(BiConsumer<E, Integer> op : onRemove) op.accept(e, i);
    }
    protected void fireChange(E oldValue, E newValue, int i) {
        for(A3Consumer<E, E, Integer> op : onChange) op.apply(oldValue, newValue, i);
    }
    protected void fireMove(E e, int oldValue, int newValue) {
        for(A3Consumer<E, Integer, Integer> op : onMove) op.apply(e, oldValue, newValue);
    }


    public ObservableList<E> onAdd(BiConsumer<E, Integer> op) {
        onAdd.add(op);
        return this;
    }
    public ObservableList<E> onRemove(BiConsumer<E, Integer> op) {
        onRemove.add(op);
        return this;
    }
    public ObservableList<E> onChange(A3Consumer<E, E, Integer> op) {
        onChange.add(op);
        return this;
    }
    public ObservableList<E> onMove(A3Consumer<E, Integer, Integer> op) {
        onMove.add(op);
        return this;
    }


    public abstract boolean add(E element);
    public abstract boolean remove(E element);


    public abstract E set(int index, E element);
    public abstract E remove(int index);

    public abstract void move(int fromIndex, int toIndex);
    public abstract void replace(List<E> list);
    public abstract void updateAt(int index);
    public abstract boolean update(E element);

}

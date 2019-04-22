package dialight.observable.collection;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

public abstract class ObservableCollection<E> {

    private final Collection<Consumer<E>> onAdd = new LinkedList<>();
    private final Collection<Consumer<E>> onRemove = new LinkedList<>();


    protected void fireAdd(E e) {
        for(Consumer<E> op : onAdd) op.accept(e);
    }
    protected void fireRemove(E e) {
        for(Consumer<E> op : onRemove) op.accept(e);
    }


    public ObservableCollection<E> onAdd(Consumer<E> op) {
        onAdd.add(op);
        return this;
    }
    public ObservableCollection<E> onRemove(Consumer<E> op) {
        onRemove.add(op);
        return this;
    }


    abstract boolean add(E element);
    abstract boolean remove(E element);

}

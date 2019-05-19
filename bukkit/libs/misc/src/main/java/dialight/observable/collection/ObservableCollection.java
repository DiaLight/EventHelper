package dialight.observable.collection;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

public abstract class ObservableCollection<E> implements Collection<E> {

    private final Collection<Consumer<E>> onAdd = new LinkedList<>();
    private final Collection<Consumer<E>> onRemove = new LinkedList<>();


    protected void fireAdd(E e) {
        for(Consumer<E> op : onAdd) {
            try {
                op.accept(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    protected void fireRemove(E e) {
        for(Consumer<E> op : onRemove) {
            try {
                op.accept(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public ObservableCollection<E> onAdd(Consumer<E> op) {
        onAdd.add(op);
        return this;
    }
    public ObservableCollection<E> onRemove(Consumer<E> op) {
        onRemove.add(op);
        return this;
    }

    public ObservableCollection<E> unregisterOnAdd(Consumer<E> op) {
        onAdd.remove(op);
        return this;
    }
    public ObservableCollection<E> unregisterOnRemove(Consumer<E> op) {
        onRemove.remove(op);
        return this;
    }


//    public abstract boolean add(E element);
//    public abstract boolean remove(E element);

}
